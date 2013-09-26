package org.seage.experimenter.reporting.h2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

class ProcessExperimentZipFileTask implements Runnable
{
	private static Logger _logger = Logger.getLogger(ProcessExperimentZipFileTask.class.getName());
	private File _zipFile;
	
	ProcessExperimentZipFileTask(File zipFile)
	{
		_zipFile = zipFile;
	}

	@Override
	public void run()
	{	
		ZipFile zf = null;	
		try
		{
			zf = new ZipFile(_zipFile);	
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
		finally
		{
			if(zf != null)
				try
				{
					zf.close();
				}
				catch (IOException ex)
				{
					_logger.warning("ERROR: "+_zipFile.getName()+": "+ex.getMessage());			
				}
		}
	}
}
