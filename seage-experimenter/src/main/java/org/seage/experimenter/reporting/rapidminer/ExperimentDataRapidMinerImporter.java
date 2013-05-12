package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.seage.experimenter._obsolete.ProcessPerformer;
import org.seage.experimenter.reporting.rapidminer.repository.AlgorithmParamsTableCreator;
import org.seage.experimenter.reporting.rapidminer.repository.RMDataTableCreator;
import org.seage.experimenter.reporting.rapidminer.repository.SingleAlgorithmTableCreator;
import org.seage.thread.TaskRunner;
import org.seage.thread.TaskRunnerEx;
import org.w3c.dom.Document;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.RepositoryStorer;
import com.rapidminer.repository.Repository;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.OperatorService;

public class ExperimentDataRapidMinerImporter
{
	private static Logger _logger = Logger.getLogger(ExperimentDataRapidMinerImporter.class.getName());
	private String _logPath;
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "seage";
	private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "repository";
	
	private List<RMDataTableCreator> _rmDataTableCreators;
	
	public ExperimentDataRapidMinerImporter(String logPath, String repoName) throws RepositoryException
	{
		_logPath = logPath;
		_rmDataTableCreators = new ArrayList<RMDataTableCreator>();
		_rmDataTableCreators.add(new SingleAlgorithmTableCreator("/databases/experiments", "ExperimentValues"));
		_rmDataTableCreators.add(new AlgorithmParamsTableCreator.GeneticAlgorithm("/databases/experiments/parameters"));
		_rmDataTableCreators.add(new AlgorithmParamsTableCreator.TabuSearch("/databases/experiments/parameters"));
		_rmDataTableCreators.add(new AlgorithmParamsTableCreator.AntColony("/databases/experiments/parameters"));
		_rmDataTableCreators.add(new AlgorithmParamsTableCreator.SimulatedAnnealing("/databases/experiments/parameters"));
	}

	public void processLogs() throws OperatorException, OperatorCreationException, RepositoryException
	{
	    _logger.info("Processing experiment logs ...");

		long t0 = System.currentTimeMillis();

		File logDir = new File(_logPath);
		List<Runnable> tasks = new ArrayList<Runnable>();

		try
		{			
			for (File f : logDir.listFiles())
			{
				if (!f.getName().endsWith(".zip"))
					continue;				

				tasks.add(new ProcessZipTask(f));				
			}	
			
			//new TaskRunner().runTasks(tasks, Runtime.getRuntime().availableProcessors());
			new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(tasks.toArray(new Runnable[]{}));
		}
		catch (Exception ex)
		{
			_logger.log(Level.SEVERE, ex.getMessage());
		}
		
		writeDataTablesToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, "Processing experiment logs DONE - " + t1 + "s");
	}

	private void initRepository() throws RepositoryException
    {
        _logger.fine("initRepository");
        RepositoryManager rm = RepositoryManager.getInstance(null);

        List<Repository> reposToRemove = new ArrayList<Repository>();
        
        for(Repository repo : rm.getRepositories())
        {
            if(!(repo instanceof LocalRepository))
                continue;
            LocalRepository r = (LocalRepository)repo;
            if(r.getName().equals(RAPIDMINER_LOCAL_REPOSITORY_NAME))
            {
                if(!r.getFile().getPath().equals(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH))
                    reposToRemove.add(r);
                    
            }
        }
        
        for(Repository repo : reposToRemove)
            rm.removeRepository(repo);          
        
        try
        {
            rm.getRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME);
        }
        catch(RepositoryException re)
        {
            rm.addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
        }
        
        _logger.fine("initRepository: saving");
        rm.save();
        _logger.fine("initRepository: done");
    }
	
	private void writeDataTablesToRepository() throws OperatorException, OperatorCreationException, RepositoryException
	{
		System.setProperty("rapidminer.home", ".");
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RapidMiner.init();
		
		initRepository();
		
		for(RMDataTableCreator table : _rmDataTableCreators)
		{	
			MemoryExampleTable memoryTable = new MemoryExampleTable(table.getAttributes());
			
			/* Save model */
			Operator modelWriter = OperatorService
					.createOperator(RepositoryStorer.class);
			modelWriter.setParameter(
					RepositoryStorer.PARAMETER_REPOSITORY_ENTRY,
					"//"+RAPIDMINER_LOCAL_REPOSITORY_NAME+table.getRepositoryPath()+"/"+table.getTableName());
	
			Process process = new Process();
	
			process.getRootOperator().getSubprocess(0).addOperator(modelWriter);
			process.getRootOperator()
					.getSubprocess(0)
					.getInnerSources()
					.getPortByIndex(0)
					.connectTo(
							modelWriter.getInputPorts().getPortByName("input"));
			
			memoryTable.readExamples(new ListDataRowReader(table.getDataTable().iterator()));
	
			process.run(new IOContainer(memoryTable.createExampleSet()));
		}
		
	}
	
	private class ProcessZipTask implements Runnable
	{
		private File _zipFile;
		
		private ProcessZipTask(File zipFile)
		{
			_zipFile = zipFile;
		}

		@Override
		public void run()
		{
			try
			{
				_logger.info(Thread.currentThread().getName()+ " - importing file: " + _zipFile.getName());
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				
				ZipFile zf = new ZipFile(_zipFile);
				
				for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
				{
					ZipEntry ze = e.nextElement();
					if (!ze.isDirectory() && ze.getName().endsWith(".xml"))
					{
						InputStream in = zf.getInputStream(ze);
						// read from 'in'
						try
						{
							Document doc = builder.parse(in);
							String version = doc.getDocumentElement().getAttribute("version");
							if(version == "")
							{
								_logger.warning("Unsupported report version: " + _zipFile.getName());
								return;
							}
							//------------------------------------------------------
							for(RMDataTableCreator creator : _rmDataTableCreators)
							    if(creator.isInvolved(doc))
							        creator.processDocument(doc);
							
						}
						catch (NullPointerException ex)
						{
						    _logger.log(Level.SEVERE, ex.getMessage(), ex);
						}
						catch (Exception ex)
						{
						    _logger.warning(_zipFile.getName() +" - "+ze.getName()+" - "+ex.getMessage()+" - " +ex.toString());
						}
					}
				}
				
				zf.close();
			}
			catch(Exception ex)
			{
				_logger.warning(ex.getMessage()+": "+_zipFile.getName());
			}
		}
	}
}
