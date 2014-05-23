package org.seage.experimenter.reporting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ProcessExperimentZipFileTask implements Runnable
{
	private static Logger _logger = Logger.getLogger(ProcessExperimentZipFileTask.class.getName());
	private File _zipFile;
	private List<IDocumentProcessor> _documentProcessors;
	
	public ProcessExperimentZipFileTask(List<IDocumentProcessor> documentProcessors, File zipFile)
	{
		_zipFile = zipFile;
		_documentProcessors = documentProcessors;
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
			boolean isProcessed = false;
			for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
			{
				ZipEntry ze = e.nextElement();
				if (!ze.isDirectory() && ze.getName().endsWith(".xml"))
				{
					InputStream in = zf.getInputStream(ze);
					isProcessed = true;
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
						for(IDocumentProcessor processor : _documentProcessors)
						    if(processor.isInvolved(doc))
						    	processor.processDocument(doc);
						
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
			if(!isProcessed)
				throw new Exception("No xml file in the zip file.");
			
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