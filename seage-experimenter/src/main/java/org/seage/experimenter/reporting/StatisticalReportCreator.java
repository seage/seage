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
import java.util.List;
import javax.xml.transform.stream.StreamResult;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class StatisticalReportCreator implements ILogReport
{
    private ProcessPerformer _processPerformer;
 
    private static final String REPORT_PATH = "htmlreport";    
    
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
            "rm-report3-p1.rmp",
            "rm-report4-p1.rmp"
        };
        
        _xslTemplates = new String[] {
            "html-rm-report1.xsl",  
            "html-rm-report2.xsl", 
            "html-rm-report3.xsl",
            "html-rm-report4.xsl"
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
    }
    
    private void createHTMLReport() throws Exception
    {
        File reportDir = new File( REPORT_PATH );
        reportDir.mkdir();

        int counter = 0;
        List<DataNode> dataNodes = _processPerformer.getProcessesDataNodes();
        for(DataNode dataNode : dataNodes)
        {
            File outputDir = new File( REPORT_PATH + "/" + _xslTemplates[counter] + ".html" );
            outputDir.createNewFile();

            StreamResult outputStream = new StreamResult( new FileOutputStream( outputDir ) );

            Transformer.getInstance().transformByXSLTFromDOMSource(dataNode.toXml(), _xslTemplates[counter], outputStream);
            
            counter++;
        }
        
    } 
    
}
