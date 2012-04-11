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

import java.util.ArrayList;
import java.util.List;
import org.seage.data.DataNode;

/**
 *
 * @author zmatlja1
 */
public class StatisticReportCreator implements ILogReport
{
    private ProcessPerformer processPerformer;
    
    private String[] resourceRMProcesses;
    
    public StatisticReportCreator()
    {
        processPerformer = new ProcessPerformer();
        resourceRMProcesses = new String[] {
            "rm-report1-p1.rmp",
            "rm-report1-p2.rmp",
            "rm-report2-p1.rmp",
            "rm-report2-p2.rmp",
            "rm-report3-p1.rmp"        
        };
        
        for (int i = 0; i < resourceRMProcesses.length; i++)
        {
            processPerformer.addProcess( new RMProcess( resourceRMProcesses[i] ) );
        }        
        
    }    

    @Override
    public void report() throws Exception
    {
        processPerformer.performProcesses();
        
        List<RMProcess> processes = processPerformer.getProcesses();
        
        List<DataNode> dataNodes = new ArrayList<DataNode>( processes.size() );

        for(RMProcess process : processes)
        {
            dataNodes.add( CsvTransformer.getInstance().csvToDataNode( "report/" + process.getResourceName().substring(0,process.getResourceName().lastIndexOf(".")) + ".csv" ) );
        }
        
        /**
         * 
         * Vytvorene datanody lze transformovat do HTMl, PDF, ... pomoci xslTransformeru
         * 
         * nebo je lze vyuzit ke zpetne vazbe
         */
        
        
    }
    
}
