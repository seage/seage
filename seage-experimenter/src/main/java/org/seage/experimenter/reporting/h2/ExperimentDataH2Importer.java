package org.seage.experimenter.reporting.h2;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.seage.thread.TaskRunnerEx;
import org.w3c.dom.Document;

public class ExperimentDataH2Importer
{
	private static Logger _logger = Logger.getLogger(ExperimentDataH2Importer.class.getName());
	private String _logPath;
	private String _dbPath;
	private boolean _clean;

	
	public ExperimentDataH2Importer(String logPath, String dbPath, boolean clean) throws Exception
	{
		_logPath = logPath;
		_dbPath = dbPath;	
		_clean = clean;
		
	}

	public void processLogs() throws Exception
	{
		Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:"+_dbPath, "sa", "sa");
        
        try
        {
        	if(_clean) 
        		clean(conn);
			HashSet<String> ids = getExperimentIDs(conn);
			processLogs(ids);
			insertNewExperiments(ids, conn);
        }
        finally
        {
        	if(conn !=null)
        		conn.close();
        }
	}
	
	private void clean(Connection conn) throws Exception
	{
		String queryDropExperiments =
				"DROP TABLE IF EXISTS Experiments";			
        
        Statement dropStmt = conn.createStatement();
        dropStmt.executeUpdate(queryDropExperiments);
	}
	
	private void insertNewExperiments(HashSet<String> ids, Connection conn) throws Exception
	{
		String queryInsert =				
				"INSERT INTO Experiments VALUES (?)"; 
		
		PreparedStatement stmt = conn.prepareStatement(queryInsert);
		
		for(String id : ids)
		{
			stmt.setLong(1, Long.parseLong(id));
			stmt.executeUpdate();
		}
	}

	private void processLogs(HashSet<String> ids) throws Exception
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
				tasks.add(new ProcessZipTask(f));
				//_logger.info(f.getName());
			}	
			
			//new TaskRunner().runTasks(tasks, Runtime.getRuntime().availableProcessors());
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
	
	private HashSet<String> getExperimentIDs(Connection conn) throws Exception
	{
		HashSet<String> experimentIDs = new HashSet<String>();
		File f = new File(_logPath);
		File[] fl = f.listFiles(new FilenameFilter()
		{			
			@Override
			public boolean accept(File dir, String fileName)
			{
				return fileName.endsWith(".zip");
			}
		});
		for(File z : fl)
		{
			String id = z.getName().split("-")[0];
			if(!experimentIDs.contains(id))
				experimentIDs.add(id);
		}
		
		String queryCreateExperiments =
				//"DROP TABLE IF EXISTS Experiments;"+
				"CREATE TABLE IF NOT EXISTS Experiments (id BIGINT PRIMARY KEY)"; 
		String queryCreateNewExperiments =
				//"DROP TABLE IF EXISTS test2;"+
				"CREATE TEMP TABLE NewExperiments (id BIGINT PRIMARY KEY)";
		String queryInsert =				
				"INSERT INTO NewExperiments VALUES (?)"; 
		String queryMinus =				
				"SELECT id FROM NewExperiments MINUS SELECT id FROM Experiments"; 		
				
        PreparedStatement stmt = null;

    	stmt = conn.prepareStatement(queryCreateExperiments);
    	stmt.execute();
    	stmt = conn.prepareStatement(queryCreateNewExperiments);
    	stmt.execute();
    	
    	stmt = conn.prepareStatement(queryInsert);	
    	for(String id : experimentIDs)
    	{        		
    		stmt.setLong(1, Long.parseLong(id));
    		stmt.execute();
    	}
    	
    	stmt = conn.prepareStatement(queryMinus);        	
    	ResultSet rs = stmt.executeQuery();
    	
    	experimentIDs.clear();      	
    	
    	while(rs.next())
    	{
    		experimentIDs.add(rs.getString(1));    		
    	}


		return experimentIDs;
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
				ZipFile zf = new ZipFile(_zipFile);
				_logger.info(Thread.currentThread().getName()+ " - importing file: " + _zipFile.getName());
				
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();	
				
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
							/*for(RMDataTableCreator creator : _rmDataTableCreators)
							    if(creator.isInvolved(doc))
							        creator.processDocument(doc);
							*/
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
				_logger.warning("ERROR: "+_zipFile.getName()+": "+ex.getMessage());
				_zipFile.renameTo(new File(_zipFile.getAbsolutePath()+".err"));
			}
		}
	}
}
