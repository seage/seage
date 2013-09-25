package org.seage.experimenter.reporting.h2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seage.experimenter.reporting.h2.tablecreator.ExperimentDataTableCreator;
import org.seage.experimenter.reporting.h2.tablecreator.ExperimentIDsTableCreator;
import org.seage.experimenter.reporting.h2.tablecreator.TableCreator;
import org.seage.thread.TaskRunnerEx;

public class ExperimentDataH2Importer extends TableCreator
{
	private static Logger _logger = Logger.getLogger(ExperimentDataH2Importer.class.getName());
	private String _logPath;
	//private String _dbPath;
	private boolean _clean;

	
	public ExperimentDataH2Importer(String logPath, String dbPath, boolean clean) throws Exception
	{
		super(dbPath);
		_logPath = logPath;
		_clean = clean;
		new ExperimentDataTableCreator(dbPath, clean);
		
	}
	
	public void processLogs() throws Exception
	{		
		ExperimentIDsTableCreator expIDsTableCreator = new ExperimentIDsTableCreator(_logPath, _dbPath, _clean);
		HashSet<String> ids = expIDsTableCreator.getExperimentIDs();
		createAndRunProcessExperimentZipFileTasks(ids);
		expIDsTableCreator.insertNewExperiments(ids);
       
	}

	private void createAndRunProcessExperimentZipFileTasks(HashSet<String> ids) throws Exception
	{				
		_logger.info("Processing experiment logs ...");

		long t0 = System.currentTimeMillis();

		File logDir = new File(_logPath);
		List<Runnable> tasks = new ArrayList<Runnable>();
		int fileCount = 0;
		try
		{			
			for (File f : logDir.listFiles(new ZipFileFilter(ids)))
			{
				fileCount++;
				tasks.add(new ProcessExperimentZipFileTask(f));
				//_logger.info(f.getName());
			}	
			new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(tasks.toArray(new Runnable[]{}));
		}
		catch (Exception ex)
		{
			_logger.log(Level.SEVERE, ex.getMessage());
		}
		
		//writeDataTablesToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		_logger.info("Files processed: " + fileCount);
		_logger.info("Time:  " + t1 + "s");
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
