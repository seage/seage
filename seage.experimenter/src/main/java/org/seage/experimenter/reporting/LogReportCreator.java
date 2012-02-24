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
 *     Jan Zmatlik
 *     - Modified 
 */
package org.seage.experimenter.reporting;

import com.rapidminer.RapidMiner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.seage.data.file.FileHelper;
import javax.xml.transform.stream.*;
import com.rapidminer.Process;
import java.io.Writer;

/**
 *
 * @author rick
 */
public class LogReportCreator implements ILogReport {
    
    private static String _logPath = "output";
    private static String _reportPath = "report";
    
    public LogReportCreator()
    {
        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        RapidMiner.init();
    }    
   
    public void report() throws Exception
    {
        createReport();
        reportRapidMiner();
    }
    
    private void reportRapidMiner() throws Exception
    {
        Process process = new Process(new File("processDefifnition.xml"));
        
        System.out.println(process.getRootOperator().createProcessTree(0));
                        
        process.run();

//        Collection<Operator> ops = process.getAllOperators();
//        ExampleSet ex = null;
//
//        for(Operator op : ops)
//        {
//            System.out.println(op.getName());
//            if(op.getName().equals("Aggregate (2)"))
//                ex = op.getOutputPorts().getPortByName("example set output").getData();
//        }
//
//
//        System.out.println(ex);
    }

    private void createReport() throws Exception
    {
        System.out.println("Creating reports ...");
        
        File logDir = new File(_logPath);
        
        File reportDir = new File(_reportPath);
        
        if(reportDir.exists())
            FileHelper.deleteDirectory(reportDir);

        reportDir.mkdirs();
        
        File output = new File(reportDir.getPath()+"/report.csv");
        output.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(output);
        
        StreamResult outputStream = new StreamResult
                        ( new FileOutputStream(output));
        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                if(arg0.isDirectory()) 
                    return true;
                else 
                    return false;
            }
        };

        String header = "ExperimentID;ProblemID;AlgorithmID;InstanceID;ConfigID;SolutionValue;Parameters;\n";
        outputStream.getOutputStream().write( header.getBytes() );

        for(String dirName : logDir.list(filter))
        {
            System.err.println(dirName);
            
            File logDirDir = new File(logDir.getPath()+"/"+dirName);
            Arrays.sort(logDirDir.list());
            
            for(String xmlFileName : logDirDir.list())
            {
                String xmlPath = logDir.getPath()+"/"+dirName+"/"+xmlFileName;

                try
                {
                    transform(xmlPath, "report2csv_1.xsl", outputStream);
                }
                catch(Exception e)
                {
                    System.out.println(xmlPath);
                }                
            }            
            
        }
        
    }
    
    private void transform(String xmlPath, String xsltName, StreamResult outputStream) throws Exception
    {
        System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl");
                
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));      
        
        transformer.transform (new StreamSource(new FileInputStream(xmlPath)), outputStream);
    }        
}
