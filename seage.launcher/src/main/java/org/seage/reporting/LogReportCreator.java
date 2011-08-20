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
import javax.xml.transform.dom.DOMSource;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;
import org.w3c.dom.Document;

/**
 *
 * @author rick
 */
public class LogReportCreator {
    
    private static String _logPath = "output";
    private static String _reportPath = "report";
   
    public void report() throws Exception
    {
        report0();
        report1();
    }
    
    
    
    private void report0() throws Exception
    {
        System.out.println("Creating reports 0...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath+"0");
        
        if(reportDir.exists())
            FileHelper.deleteDirectory(reportDir);
        
        reportDir.mkdirs();
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };
        DataNode xmlDoc = new DataNode("xml");
        
        for(String dirName : logDir.list(filter))
        {
            //System.out.println(dirName);
            
            //File reportDirDir = new File(reportDir.getPath()+"/"+dirName);
            //reportDirDir.mkdirs();
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            
            
            for(String xmlFileName : logDirDir.list())
            {
                //System.out.println("\t"+xmlFileName);
                File xmlFile = new File(logDir.getPath()+"/"+dirName+"/"+xmlFileName);
                try{
                    DataNode dn = XmlHelper.readXml(xmlFile); 
                    xmlDoc.putDataNodeRef(dn);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }            
            
            transform0(xmlDoc.toXml(), reportDir);
        }
        
        
        
    }
    
    private void transform0(Document xmlDoc, File reportDir)
    {
        try
            {
                //XmlHelper.writeXml(xmlDoc, reportDir.getPath()+"/report.xml");
                
                System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");
                
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report0.xsl")));

                transformer.transform
                  (new DOMSource(xmlDoc),
                   //new javax.xml.transform.stream.StreamSource
                   //     (reportDir.getPath()+"/report.xml"),
                   new javax.xml.transform.stream.StreamResult
                        ( new FileOutputStream(reportDir.getPath()+"/report.html")));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            } 
    }
    
    private void report1()
    {
        System.out.println("Creating reports 1...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath+"1");
        
        if(reportDir.exists())
            FileHelper.deleteDirectory(reportDir);
        
        reportDir.mkdirs();
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };
        
        DataNode xmlDoc = new DataNode("xml");
        
        for(String dirName : logDir.list(filter))
        {
            //System.out.println(dirName);
            
            //File reportDirDir = new File(reportDir.getPath()+"/"+dirName);
            //reportDirDir.mkdirs();
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            
            
            for(String xmlFileName : logDirDir.list())
            {
                //System.out.println("\t"+xmlFileName);
                File xmlFile = new File(logDir.getPath()+"/"+dirName+"/"+xmlFileName);
                try{
                    DataNode report = XmlHelper.readXml(xmlFile); 
                    
                    String experimentID = getExperimentID(report);                    
                    DataNode experiment = touchDataNode(xmlDoc, "Experiment", experimentID);                    
                    
                    String problemID = report.getDataNode("Config").getDataNode("Problem").getValueStr("id");
                    DataNode problem = touchDataNode(experiment, "Problem", problemID);
                    
                    String instanceID = report.getDataNode("Config").getDataNode("Problem").getDataNode("Instance").getValueStr("name");
                    DataNode instance = touchDataNode(problem, "Instance", instanceID);
                    
                    //String algorithmID = report.getDataNode("Config").getDataNode("Algorithm").getValueStr("id");
                    //DataNode algorithm = touchDataNode(instance, "Algorithm", algorithmID);
                    
                    String configID = report.getDataNode("Config").getValueStr("configID");
                    DataNode config = touchDataNode(instance, "Config", configID);
                    
                    //config.putValue("algorithmID", algorithm.getValue("id"));
                    DataNode algorithm = report.getDataNode("Config").getDataNode("Algorithm");
                    if(!config.containsNode("Algorithm"))
                        config.putDataNodeRef(algorithm);
                    
                    String runID = getRuntID(report);
                    //touchDataNode(config, "Algorithm", runID);
                    DataNode run = touchDataNode(config, "Run", runID);
                    
                    DataNode algReport = report.getDataNode("AlgorithmReport");
                    String algID = algReport.getValueStr("algorithmID");
                    DataNode algReport2 = touchDataNode(run, "AlgorithmReport", algID);
                    algReport2.putDataNodeRef(algReport.getDataNode("Statistics"));

                    
                    
                    //experiment.putDataNode(problem);
                    //xmlDoc.putDataNode(experiment);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }
            //XmlHelper.writeXml(xmlDoc, reportDirDir.getPath()+"/report.xml");
            
            
        }
        
        //XmlHelper.writeXml(xmlDoc, reportDir.getPath()+"/report.xml");
        
        try
            {          
                System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");

                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream("report1.xsl")));

                transformer.transform
                  (new DOMSource(xmlDoc.toXml()),
                   //new javax.xml.transform.stream.StreamSource
                   //     (reportDir.getPath()+"/report.xml"),
                   new javax.xml.transform.stream.StreamResult
                        ( new FileOutputStream(reportDir.getPath()+"/report.html")));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            } 
    }
    
    private String getExperimentID(DataNode report) throws Exception
    {
        if(report.containsValue("experimentID"))
            return report.getValueStr("experimentID");
        else
            return report.getDataNode("Config").getValueStr("runID");
    }
    
    private String getRuntID(DataNode report) throws Exception
    {
        if(report.containsValue("runID"))
            return report.getValueStr("runID");
        else
            return report.getDataNode("AlgorithmReport").getValueStr("created");
    }
    
    private DataNode touchDataNode(DataNode parent, String elemName, String elemID) throws Exception
    {
        DataNode result;
        if(!parent.containsNode(elemName) || parent.getDataNodeById(elemID)==null)
        {
            result = new DataNode(elemName);
            result.putValue("id", elemID);

            parent.putDataNodeRef(result);            
        }
        else
            result = parent.getDataNodeById(elemID);
        
        return result;
    }
}
