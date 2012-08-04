package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ExperimentLogImporter {
	private String _logPath;
	private String _repoName;

	public ExperimentLogImporter(String logPath, String repoName) {
		_logPath = logPath;
		_repoName = repoName;
	}

	public void processLogs() {
		Logger.getLogger(getClass().getName()).log(Level.INFO,
				"Processing experiment logs ...");

		long t0 = System.currentTimeMillis();

		File logDir = new File(_logPath);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			for (File f : logDir.listFiles()) {
				if (!f.getName().endsWith(".zip"))
					continue;
				Logger.getLogger(getClass().getName()).log(Level.INFO, f.getName());

				int numFiles = 0;
				
				ZipFile zf = new ZipFile(f);
				try {
				  for (Enumeration<? extends ZipEntry> e = zf.entries();
				        e.hasMoreElements();) {
					  numFiles++;
				    ZipEntry ze = e.nextElement();
				    String name = ze.getName();
				    if (name.endsWith(".xml")) {
				      InputStream in = zf.getInputStream(ze);
				      // read from 'in'
				      try{
				      Document doc = builder.parse(in);
				      }catch(SAXException ex)
				      {
				    	  Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage());
				      }
				      //DataNode dn = XmlHelper.readXml(in);
				    }
				  }
				} finally {
				  zf.close();
				}
				Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, ""+numFiles);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO,
				"Processing experiment logs DONE - " + t1 + "s");
	}
}
