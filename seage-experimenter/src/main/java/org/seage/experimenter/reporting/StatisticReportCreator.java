/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.experimenter.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.stream.StreamResult;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author zmatlja1
 */
public class StatisticReportCreator implements ILogReport
{
    private ProcessPerformer _processPerformer;
    
    private static final String INPUT_DATA_PATH     = "statistics";    
    private static final String REPORT_PATH         = "htmlreport";    
    private static final String OUTPUT_FILE         = "statistics.html";    
    private static final String XSLTEMPLATE         = "rm-report1-html.xsl";
    
    private String[] _resourceRMProcesses;
    
    public StatisticReportCreator()
    {
        _processPerformer = new ProcessPerformer();
        _resourceRMProcesses = new String[] {
            "rm-report1-p1.rmp",
            "rm-report1-p2.rmp",
            "rm-report2-p1.rmp",
            "rm-report2-p2.rmp",
            "rm-report3-p1.rmp"        
        };
        
        for (int i = 0; i < _resourceRMProcesses.length; i++)
        {
            _processPerformer.addProcess( new RMProcess( _resourceRMProcesses[i] ) );
        }     
    }    

    @Override
    public void report() throws Exception
    {
        _processPerformer.performProcesses();

        this.createHTMLReport();
        
        /**
         * 
         * Vytvorene datanody lze transformovat do HTMl, PDF, ... pomoci xslTransformeru
         * 
         * nebo je lze vyuzit ke zpetne vazbe
         */
    }
    
    private void createHTMLReport() throws Exception
    {
        File inputDataDir = new File( INPUT_DATA_PATH );
        inputDataDir.mkdir();
        
        int k = 0;
        for(DataNode dataNode : _processPerformer.getProcessesDataNodes())
        {
//            XmlHelper.writeXml(dataNode, inputDataDir.getPath() + "/" + (++k) + ".xml");
        }
        
        File reportDir = new File( REPORT_PATH );
        reportDir.mkdir();
        
        File outputDir = new File( REPORT_PATH + "/" + OUTPUT_FILE );
        outputDir.createNewFile();

        StreamResult outputStream = new StreamResult( new FileOutputStream( outputDir ) );
  
        for(String fileName : inputDataDir.list())
        {
            Transformer.getInstance().transformByXSLT(inputDataDir.getPath() + "/" + fileName, XSLTEMPLATE, outputStream);
        }
        
    } 
    
}
