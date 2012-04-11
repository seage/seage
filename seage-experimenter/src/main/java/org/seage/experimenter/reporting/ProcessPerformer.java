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

import com.rapidminer.FileProcessLocation;
import com.rapidminer.RapidMiner;
import com.rapidminer.Process;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author zmatlja1
 */
public class ProcessPerformer {
    
    private HashMap<String, RMProcess> processes;
    
    public ProcessPerformer()
    {
        processes = new HashMap<String, RMProcess>();
        RapidMiner.setExecutionMode( RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI );
        RapidMiner.init();
    } 
    
    public void addProcess(RMProcess process)
    {
        if(processes.containsKey(process.getResourceName()))
        {
            //vyhodit vyjimku
        }
        
        processes.put(process.getResourceName(), process);
    }
    
    public RMProcess getProcess(String name)
    {
        return processes.get(name);
    }
    
    public List<RMProcess> getProcesses()
    {
        return new ArrayList<RMProcess>(processes.values());
    }
    
    public void performProcess(String resourceName) throws Exception
    {
        Process process = new Process( getClass().getResourceAsStream( resourceName ) );
        process.setProcessLocation( new FileProcessLocation( new File(".") ) );        
        
        // TODO: B - Info only for debug, after that they'll be removed
        System.out.println(process.getRootOperator().createProcessTree(0));
        System.out.println("RUN"); 
        
        process.run();
    }
    
    public void performProcesses() throws Exception
    {
        for(RMProcess process : processes.values())
        {
            this.performProcess( process.getResourceName() );
        }
    }
    
    
}
