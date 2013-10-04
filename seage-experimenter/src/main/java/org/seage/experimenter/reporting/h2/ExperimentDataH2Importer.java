package org.seage.experimenter.reporting.h2;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.seage.experimenter.reporting.ProcessExperimentZipFileTask;
import org.seage.experimenter.reporting.h2.tablecreator.AlgorithmParamsTableCreator;
import org.seage.experimenter.reporting.h2.tablecreator.ExperimentTasksTableCreator;
import org.seage.experimenter.reporting.h2.tablecreator.ExperimentsTableCreator;
import org.seage.experimenter.reporting.h2.tablecreator.H2DataTableCreator;
import org.seage.thread.TaskRunnerEx;

public class ExperimentDataH2Importer
{
	private static Logger _logger = Logger.getLogger(ExperimentDataH2Importer.class.getName());
	private String _logPath;
	private String _dbPath;
	private ExperimentsTableCreator _expperimentsTableCreator;
	private List<H2DataTableCreator> _h2DataTableCreators;
		
	public ExperimentDataH2Importer(String logPath, String dbPath, boolean clean) throws Exception
	{
		_logPath = logPath;
		_dbPath = dbPath;

		_expperimentsTableCreator = new ExperimentsTableCreator(_logPath, _dbPath, clean);
		
		_h2DataTableCreators = new ArrayList<H2DataTableCreator>();
		_h2DataTableCreators.add(_expperimentsTableCreator);
		_h2DataTableCreators.add(new ExperimentTasksTableCreator(dbPath));
		_h2DataTableCreators.add(new AlgorithmParamsTableCreator.GeneticAlgorithm(dbPath));
		_h2DataTableCreators.add(new AlgorithmParamsTableCreator.TabuSearch(dbPath));
		_h2DataTableCreators.add(new AlgorithmParamsTableCreator.SimulatedAnnealing(dbPath));
		_h2DataTableCreators.add(new AlgorithmParamsTableCreator.AntColony(dbPath));
	}
			
	public void processLogs() throws Exception
	{		
		_logger.info("Processing experiment logs ...");
		long t0 = System.currentTimeMillis();
		
		try
		{
			HashSet<String> ids = _expperimentsTableCreator.getExperimentIDs();
			createAndRunProcessExperimentZipFileTasks(ids);
			//expIDsTableCreator.insertNewExperiments(ids);
		}
		finally
		{
			for(H2DataTableCreator tc : _h2DataTableCreators)
				tc.close();
		}
		long t1 = (System.currentTimeMillis() - t0) / 1000;
		_logger.info("Time:  " + t1 + "s");
       
	}

	@SuppressWarnings("unchecked")
	private void createAndRunProcessExperimentZipFileTasks(HashSet<String> ids) throws Exception
	{
		File logDir = new File(_logPath);
		List<Runnable> tasks = new ArrayList<Runnable>();
		int fileCount = 0;
		try
		{			
			for (File f : logDir.listFiles(new ZipFileFilter(ids)))
			{
				fileCount++;
				tasks.add(new ProcessExperimentZipFileTask((List<IDocumentProcessor>)(List<?>)_h2DataTableCreators, f));
				//_logger.info(f.getName());
			}	
			new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(tasks.toArray(new Runnable[]{}));
		}
		catch (Exception ex)
		{
			_logger.log(Level.SEVERE, ex.getMessage());
		}
		
		_logger.info("Files processed: " + fileCount);
	}
	
	private class ZipFileFilter implements FilenameFilter
	{
		private HashSet<String> _ids;
		
		public ZipFileFilter(HashSet<String> ids)
		{
			_ids =ids;
		}
		@Override
		public boolean accept(File arg0, String filename)
		{
			if (!filename.endsWith(".zip"))			
				return false;
			String id = filename.split("-")[0];
			if(id == null)
				return false;
			if(_ids.contains(id))
				return true;
			return false;
		}
		
	}
	
	
}
