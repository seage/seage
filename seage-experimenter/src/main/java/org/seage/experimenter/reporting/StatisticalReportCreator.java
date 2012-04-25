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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.stream.StreamResult;
import org.seage.data.DataNode;
import org.seage.experimenter.reporting.rapidminer.ProcessPerformer;
import org.seage.experimenter.reporting.rapidminer.RMProcess;

/**
 *
 * @author zmatlja1
 */
public class StatisticalReportCreator implements ILogReport
{
    private ProcessPerformer _processPerformer;
 
    private static final String REPORT_PATH = "report";
    
    private RMProcess[] rmProcesses;
    private String[] _xslTemplates;
    
    public StatisticalReportCreator()
    {
        try {
            _processPerformer = new ProcessPerformer();
        } catch (Exception ex) {
            Logger.getLogger(StatisticalReportCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rmProcesses = new RMProcess[] {
            new RMProcess( "rm-report1-p1.rmp" , "Aggregate"            , "Report 1" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report1-p2.rmp" , "Sort"                 , "Report 1" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report1-p3.rmp" , "Loop Examples"        , "Report 1" , Arrays.asList( "output 1" ) ),//, "output 2"
            
            new RMProcess( "rm-report2-p1.rmp" , "Sort by Pareto Rank"  , "Report 2" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report2-p2.rmp" , "Sort by Pareto Rank"  , "Report 2" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report2-p3.rmp" , "Loop Examples"        , "Report 2" , Arrays.asList( "output 1" ) ),
            
            new RMProcess( "rm-report3-p1.rmp" , "Sort (2)"             , "Report 3" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report2-p2.rmp" , "Sort by Pareto Rank"  , "Report 3" , Arrays.asList( "example set output" ) ),
            new RMProcess( "rm-report2-p3.rmp" , "Loop Examples"        , "Report 3" , Arrays.asList( "output 1" ) )
        };
        
        _xslTemplates = new String[] {
            "html-rm-report1.xsl",
            "html-rm-report2.xsl",
            "html-rm-report3.xsl"
        };

        for(RMProcess process : rmProcesses)
            _processPerformer.addProcess( process );
        
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
            File outputDir = new File( REPORT_PATH + "/" + _xslTemplates[counter].substring(0,_xslTemplates[counter].lastIndexOf('.')) + ".html" );
            outputDir.createNewFile();

            StreamResult outputStream = new StreamResult( new FileOutputStream( outputDir ) );

            Transformer.getInstance().transformByXSLTFromDOMSource(dataNode.toXml(), _xslTemplates[counter], outputStream);
            
            counter++;
        }
        
    } 
    
}
