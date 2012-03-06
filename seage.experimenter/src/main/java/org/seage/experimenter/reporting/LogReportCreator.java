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

import com.rapidminer.FileProcessLocation;
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
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import java.util.Collection;
//import org.seage.experimenter.ExampleSetConverter;

/**
 *
 * @author rick
 */
public class LogReportCreator implements ILogReport {
    
    private static String _logPath = "output";
    private static String _reportPath = "report";
    private static String _csvHeader = "ExperimentID;ProblemID;AlgorithmID;InstanceID;ConfigID;SolutionValue;";
    private static final String XSL_TEMPLATE = "report2csv.xsl";
    private static final String RAPIDMINER_PROCESS_FILE = "processDefifnition.xml";
    private static final String OUTPUT_CSV_FILE = "report.csv";
    
    public LogReportCreator()
    {
        RapidMiner.setExecutionMode( RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI );
        RapidMiner.init();
    }    
   
    public void report() throws Exception
    {
        createReport();
        reportRapidMiner();
    }
    
    private void reportRapidMiner() throws Exception
    {        
        Process process = new Process( getClass().getResourceAsStream(RAPIDMINER_PROCESS_FILE ));
        process.setProcessLocation(new FileProcessLocation(new File(".")));
        System.out.println(process.getRootOperator().createProcessTree(0));
                        
        process.run();

//        Collection<Operator> ops = process.getAllOperators();
//        ExampleSet ex = null;
//
//        for(Operator op : ops)
//        {
//            System.out.println(op.getName());
//            if(op.getName().equals("Sort (2)"))
//                ex = op.getOutputPorts().getPortByName("example set output").getData();
//        }
//        
//        //ExampleSetConverter.convertToDataNode(null);
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
        
        File output = new File(reportDir.getPath() + "/" + OUTPUT_CSV_FILE);
        output.createNewFile();

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
        
        // TODO: B Number of names of parameters is fixed, for future will be good to create a floating number of names of parameters 
        
        int numberOfParameters = 10;
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < numberOfParameters + 1; ++i)
        {
            sb.append("p").append(i).append(";");
        }        

        outputStream.getOutputStream().write( ( _csvHeader + sb.append("\n").toString() ).getBytes() );

        for(String dirName : logDir.list(filter))
        {
            System.err.println(dirName);
            
            File logDirDir = new File(logDir.getPath() + "/" + dirName);
            Arrays.sort(logDirDir.list());
            
            for(String xmlFileName : logDirDir.list())
            {
                String xmlPath = logDir.getPath() + "/" + dirName + "/" + xmlFileName;

                try
                {
                    transform(xmlPath, XSL_TEMPLATE, outputStream);
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
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(getClass().getResourceAsStream(xsltName)));      
        
        transformer.transform (new StreamSource(new FileInputStream(xmlPath)), outputStream);
    }        
}
