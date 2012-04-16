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
import javax.xml.transform.stream.StreamResult;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class StatisticalReportCreator implements ILogReport
{
    private ProcessPerformer _processPerformer;
 
    private static final String REPORT_PATH         = "htmlreport";    
    
    private String[] _resourceRMProcesses;
    private String[] _xslTemplates;
    
    public StatisticalReportCreator()
    {
        _processPerformer = new ProcessPerformer();
        _resourceRMProcesses = new String[] {
            "rm-report1-p1.rmp",
            "rm-report1-p2.rmp",
            "rm-report2-p1.rmp",
            "rm-report2-p2.rmp",
            "rm-report3-p1.rmp"        
        };
        
        _xslTemplates = new String[] {
            "rm-report1-html.xsl",  
            "rm-report2-html.xsl", 
            "rm-report3-html.xsl"
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
        File reportDir = new File( REPORT_PATH );
        reportDir.mkdir();

        int counter = 0;
        for(DataNode dataNode : _processPerformer.getProcessesDataNodes())
        {
            File outputDir = new File( REPORT_PATH + "/" + _xslTemplates[counter] + ".html" );
            outputDir.createNewFile();

            StreamResult outputStream = new StreamResult( new FileOutputStream( outputDir ) );
            
            System.out.println(dataNode.toXml());
        
            Transformer.getInstance().transformByXSLTFromDOMSource(dataNode.toXml(), _xslTemplates[counter], outputStream);
            
            counter++;
            if(counter>1) break;
        }
        
    } 
    
}
