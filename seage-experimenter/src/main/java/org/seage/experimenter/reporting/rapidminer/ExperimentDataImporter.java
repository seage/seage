package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.rapidminer.old.ProcessPerformer;
import org.seage.thread.TaskRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DoubleArrayDataRow;
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
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;

public class ExperimentDataImporter
{
	private static Logger _logger = Logger.getLogger(ExperimentDataImporter.class.getName());
	private String _logPath;
	private String _repoName;
	
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "seage";
	private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "repository";
	
	private Hashtable<RMDataTableInfo, List<DataRow>> _rmDataTables;
	private RMDataTableInfo _experimentDataInfo;
	private Hashtable<String, RMDataTableInfo> _rmAlgParams;
	private Hashtable<String, Hashtable<String, XmlHelper.XPath> > _rmAlgParamsXPaths;
	private Hashtable<String, String> _rmAlgParamsConfigIDs;
	
	private Hashtable<String, Hashtable<String, XmlHelper.XPath>> _xPaths;
	
	public ExperimentDataImporter(String logPath, String repoName) throws RepositoryException
	{
		_logPath = logPath;
		_repoName = repoName;
		
		_rmDataTables = new Hashtable<RMDataTableInfo, List<DataRow>>();
		
		_experimentDataInfo = new RMDataTableInfo("/databases/experiments", "ExperimentValues");
		//_experimentDataInfo.Attributes.add(new RMAttributeInfo("ExperimentType", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("ExperimentID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("ProblemID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("AlgorithmID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("InstanceID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("ConfigID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("RunID", Ontology.NOMINAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("InitSolutionValue", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("BestSolutionValue", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("NrOfNewSolutions", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("NrOfIterations", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("LastIterNumberNewSol", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("DurationInSeconds", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("TimeoutInSeconds", Ontology.REAL));
		_experimentDataInfo.Attributes.add(new RMAttributeInfo("ComputerName", Ontology.NOMINAL));		
		_rmDataTables.put(_experimentDataInfo, new ArrayList<DataRow>());		
		
		_xPaths = new Hashtable<String, Hashtable<String,XmlHelper.XPath>>(); 
		
		Hashtable<String, XmlHelper.XPath> v01 = new Hashtable<String, XmlHelper.XPath>();
		//v01.put("ExperimentType", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
		v01.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
		v01.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
		v01.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
		v01.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v01.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
		v01.put("RunID", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/@created"));
		v01.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v01.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v01.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v01.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v01.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XmlHelper.XPath> v02 = new Hashtable<String, XmlHelper.XPath>();
		v02.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/@experimentID"));
		v02.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
		v02.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
		v02.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v02.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
		v02.put("RunID", new XmlHelper.XPath("/ExperimentTask/@runID"));
		v02.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v02.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v02.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v02.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v02.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XmlHelper.XPath> v03 = new Hashtable<String, XmlHelper.XPath>();
		v03.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/@experimentID"));
		v03.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
		v03.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
		v03.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v03.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
		v03.put("RunID", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
		v03.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v03.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v03.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v03.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v03.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XmlHelper.XPath> v04 = new Hashtable<String, XmlHelper.XPath>();
		v04.put("ExperimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
		v04.put("ProblemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
		v04.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
		v04.put("InstanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
		v04.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
		v04.put("RunID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
		v04.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
		v04.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
		v04.put("NrOfNewSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v04.put("LastIterNumberNewSol", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v04.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
		v04.put("DurationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
		v04.put("TimeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
		v04.put("ComputerName", new XmlHelper.XPath("/ExperimentTaskReport/@machineName"));
		
		_xPaths.put("0.1", v01);
        _xPaths.put("0.2", v02);
        _xPaths.put("0.3", v03);
        _xPaths.put("0.4", v04);
//      _XmlHelper.XPaths.put("0.5", v05);
		
		_rmAlgParams = new Hashtable<String, RMDataTableInfo> ();
	    _rmAlgParamsXPaths = new Hashtable<String, Hashtable<String, XmlHelper.XPath> >();
	    _rmAlgParamsConfigIDs = new Hashtable<String, String>();
		
		// GeneticAlgorithm		
		RMDataTableInfo algGAParams = new RMDataTableInfo("/databases/experiments/parameters", "GeneticAlgorithm");
		algGAParams.Attributes.add(new RMAttributeInfo("ConfigID", Ontology.NOMINAL));
        algGAParams.Attributes.add(new RMAttributeInfo("CrossLengthPct", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("EliteSubjectPct", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("IterationCount", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("MutateLengthPct", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("MutateSubjectPct", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("NumSolutions", Ontology.REAL));
        algGAParams.Attributes.add(new RMAttributeInfo("RandomSubjectPct", Ontology.REAL));
        
		Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
		algGAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
		algGAXPaths.put("RandomSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));
		algGAXPaths.put("CrossLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
		algGAXPaths.put("EliteSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
		algGAXPaths.put("IterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
		algGAXPaths.put("MutateLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
		algGAXPaths.put("MutateSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
		algGAXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numSolutions"));
				
        _rmAlgParams.put("GeneticAlgorithm", algGAParams);
        _rmAlgParamsXPaths.put("GeneticAlgorithm", algGAXPaths);
        _rmDataTables.put( algGAParams, new ArrayList<DataRow>());
        
        // TabuSearch     
        RMDataTableInfo algTSParams = new RMDataTableInfo("/databases/experiments/parameters", "TabuSearch");
        algTSParams.Attributes.add(new RMAttributeInfo("ConfigID", Ontology.NOMINAL));
        algTSParams.Attributes.add(new RMAttributeInfo("NumIteration", Ontology.REAL));
        algTSParams.Attributes.add(new RMAttributeInfo("TabuListLength", Ontology.REAL));
        
        Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
        algTSXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        algTSXPaths.put("NumIteration", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
        algTSXPaths.put("TabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
                
        _rmAlgParams.put("TabuSearch", algTSParams);
        _rmAlgParamsXPaths.put("TabuSearch", algTSXPaths);
        _rmDataTables.put( algTSParams, new ArrayList<DataRow>());
        
        
		
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
			
			new TaskRunner().runTasks(tasks, Runtime.getRuntime().availableProcessors());

		}
		catch (Exception ex)
		{
			_logger.severe(ex.getMessage());
		}
		
		writeDataTablesToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO,
				"Processing experiment logs DONE - " + t1 + "s");
	}
	
	private synchronized void pickupDataFromDocument(Document doc, RMDataTableInfo tableInfo) throws Exception
	{
		double[] valArray = new double[tableInfo.Attributes.size()];
		int i=0;
		String version = getReportVersion(doc);			
		
		for(RMAttributeInfo attInfo : tableInfo.Attributes)
		{ 
			XmlHelper.XPath xPath = _xPaths.get(version).get(attInfo.AttributeName);
			if(xPath == null)
			{
			    _logger.warning("No XmlHelper.XPath defined for attribute: " + attInfo.AttributeName);
			    continue;
			}
			String val = XmlHelper.getValueFromDocument(doc.getDocumentElement(), xPath);//(String) XmlHelper.XPath.evaluate(attInfo.XmlHelper.XPath, doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
			//String val = (String) attInfo.XmlHelper.XPath.evaluate( doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
			if(val.equals(""))
				throw new Exception(xPath.XPathStr + " not found");
			if(attInfo.Type == Ontology.NOMINAL)
				valArray[i++] = attInfo.Attribute.getMapping().mapString(val);
			else
				valArray[i++] = Double.parseDouble(val);
		}
		
		_rmDataTables.get(tableInfo).add(new DoubleArrayDataRow(valArray));
	}
	
	private synchronized void pickupAlgorithmParamsFromDocument(Document doc) throws Exception
	{
	    String configID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
	    if(_rmAlgParamsConfigIDs.containsKey(configID))
	        return;
	    else
	        _rmAlgParamsConfigIDs.put(configID, configID);
	    
	    String algorithmID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
	    if(!_rmAlgParams.containsKey(algorithmID))
	    {
	        _logger.warning("No parameter info for algorithm: " + algorithmID);
	        return;
	    }
	    RMDataTableInfo algParams = _rmAlgParams.get(algorithmID);
	    	    
	    double[] valArray = new double[algParams.Attributes.size()];
        int i=0;
        
        
        for(RMAttributeInfo attInfo : algParams.Attributes)
        { 
            XmlHelper.XPath xPath = _rmAlgParamsXPaths.get(algorithmID).get(attInfo.AttributeName);
            if(xPath == null)
            {
                _logger.warning("No XmlHelper.XPath defined for attribute: " + attInfo.AttributeName);
                continue;
            }
            String val = XmlHelper.getValueFromDocument(doc.getDocumentElement(), xPath);//(String) XmlHelper.XPath.evaluate(attInfo.XmlHelper.XPath, doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
            if(val.equals(""))
                throw new Exception(xPath.XPathStr + " not found");
            if(attInfo.Type == Ontology.NOMINAL)
                valArray[i++] = attInfo.Attribute.getMapping().mapString(val);
            else
                valArray[i++] = Double.parseDouble(val);
        }
        
        _rmDataTables.get(algParams).add(new DoubleArrayDataRow(valArray));
	}
	
	private String getReportVersion(Document doc)
	{	
		String version = doc.getDocumentElement().getAttribute("version");
		if(version.equals("0.2"))
			return "0.4";
		if(!version.equals(""))
			return version;
		if(doc.getDocumentElement().getAttributes().getLength()==0)
			return "0.1";
		if(doc.getDocumentElement().getAttributes().getLength()==1)
			return "0.3";
		return "0.2";
	}

	

	private void writeDataTablesToRepository() throws OperatorException, OperatorCreationException, RepositoryException
	{
		System.setProperty("rapidminer.home", ".");
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RapidMiner.init();
		
		initRepository();
		
		for(RMDataTableInfo tableInfo : _rmDataTables.keySet())
		{
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			for(RMAttributeInfo attInfo : tableInfo.Attributes)
				attributes.add(attInfo.Attribute);
			
			MemoryExampleTable memoryTable = new MemoryExampleTable(attributes);
			
			/* Save model */
			Operator modelWriter = OperatorService
					.createOperator(RepositoryStorer.class);
			modelWriter.setParameter(
					RepositoryStorer.PARAMETER_REPOSITORY_ENTRY,
					"//"+RAPIDMINER_LOCAL_REPOSITORY_NAME+tableInfo.RepositoryPath+"/"+tableInfo.TableName);
	
			Process process = new Process();
	
			process.getRootOperator().getSubprocess(0).addOperator(modelWriter);
			process.getRootOperator()
					.getSubprocess(0)
					.getInnerSources()
					.getPortByIndex(0)
					.connectTo(
							modelWriter.getInputPorts().getPortByName("input"));
			
			memoryTable.readExamples(new ListDataRowReader(_rmDataTables.get(tableInfo).iterator()));
	
			process.run(new IOContainer(memoryTable.createExampleSet()));
		}
		
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
	
	private class RMDataTableInfo
	{
	    public String RepositoryPath;
		public String TableName;
		public List<RMAttributeInfo> Attributes;
		
		public RMDataTableInfo(String repositoryPath, String tableName) {
		    RepositoryPath = repositoryPath;
		    TableName = tableName;
			Attributes = new ArrayList<RMAttributeInfo>();
		}
		
	}
	private class RMAttributeInfo
	{
		public String AttributeName;
		public int Type;
		public Attribute Attribute;
		
		public RMAttributeInfo(String attributeName, int type) 
		{
			AttributeName = attributeName;
			Type = type;
			Attribute = AttributeFactory.createAttribute(attributeName, type);			
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
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				//XmlHelper.XPath XmlHelper.XPath = XmlHelper.XPathFactory.newInstance().newXmlHelper.XPath();
				
				ZipFile zf = new ZipFile(_zipFile);
				
				for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
				{
					ZipEntry ze = e.nextElement();
					String name = ze.getName();
					if (name.endsWith(".xml"))
					{
						InputStream in = zf.getInputStream(ze);
						// read from 'in'
						try
						{
							Document doc = builder.parse(in);
							pickupDataFromDocument(doc, _experimentDataInfo);
							pickupAlgorithmParamsFromDocument(doc);
						}
						catch (NullPointerException ex)
						{
						    _logger.log(Level.SEVERE, ex.getMessage(), ex);
						}
						catch (Exception ex)
						{
						    _logger.warning(name+" - "+ex.getMessage()+" - " +ex.toString());
						}
						// DataNode dn = XmlHelper.readXml(in);
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
