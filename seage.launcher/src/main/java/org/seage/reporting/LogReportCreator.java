/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
public class LogReportCreator {
    
    private static String _logPath = "output";
    private static String _reportPath = "report";
    
    public void report()
    {
        System.out.println("Creating reports...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath);
        if(!reportDir.exists())
            reportDir.mkdirs();
        else
            FileHelper.deleteDirectory(reportDir);
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };
        
        for(String dirName : logDir.list(filter))
        {
            //System.out.println(dirName);
            
            File reportDirDir = new File(reportDir.getPath()+"/"+dirName);
            reportDirDir.mkdirs();
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            
            DataNode xmlDoc = new DataNode("xml");
            for(String xmlFileName : logDirDir.list())
            {
                //System.out.println("\t"+xmlFileName);
                File xmlFile = new File(logDir.getPath()+"/"+dirName+"/"+xmlFileName);
                try{
                    DataNode dn = XmlHelper.readXml(xmlFile); 
                    xmlDoc.putDataNode(dn);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }
            XmlHelper.writeXml(xmlDoc, reportDirDir.getPath()+"/report.xml");
            
            try
            {                
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report.xsl")));

                transformer.transform
                  (new javax.xml.transform.stream.StreamSource
                        (reportDirDir.getPath()+"/report.xml"),
                   new javax.xml.transform.stream.StreamResult
                        ( new FileOutputStream(reportDirDir.getPath()+"/report.html")));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            } 
        }
    }
}
