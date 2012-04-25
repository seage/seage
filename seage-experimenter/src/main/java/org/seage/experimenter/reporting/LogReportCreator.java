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

import org.seage.experimenter.reporting.rapidminer.ProcessPerformer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import org.seage.data.file.FileHelper;
import javax.xml.transform.stream.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rick
 */
public class LogReportCreator implements ILogReport {
    
    private static final String LOG_PATH            = "output";
    private static final String REPORT_PATH         = "report";
    private static final String XSL_TEMPLATE        = "report2csv.xsl";
    private static final String OUTPUT_CSV_FILE     = "report.csv";    
    private static final String CSV_HEADER          = "ExperimentID;ProblemID;AlgorithmID;InstanceID;ConfigID;SolutionValue;Time;";

    public LogReportCreator() {}
    
    @Override
    public void report() throws Exception
    {
        //createReport();
    }
    
    private void createReport() throws Exception
    {
        Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, "Creating reports ...");
        Logger.getLogger(ProcessPerformer.class.getName()).fine("Creating reports ...");
        
        File logDir = new File(LOG_PATH);
        
        File reportDir = new File(REPORT_PATH);
        
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

        outputStream.getOutputStream().write( ( CSV_HEADER + sb.append("\n").toString() ).getBytes() );

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
                    Transformer.getInstance().transformByXSLT(xmlPath, XSL_TEMPLATE, outputStream);
                }
                catch(Exception e)
                {
                    Logger.getLogger(ProcessPerformer.class.getName()).fine(xmlPath);
                }
            }            
        }        
    }   
}
