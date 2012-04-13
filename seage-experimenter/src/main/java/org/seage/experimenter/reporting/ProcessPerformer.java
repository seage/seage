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
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.data.DataNode;
import org.seage.experimenter.ExampleSetConverter;

/**
 *
 * @author zmatlja1
 */
public class ProcessPerformer {
    
    private String EXAMPLESET_OUTPUT_PORT = "example set output";
    
    private HashMap<String, RMProcess> _processes;
    
    private List<ExampleSet> _exampleSets;
    
    public ProcessPerformer()
    {
        _processes = new HashMap<String, RMProcess>();
        _exampleSets = new ArrayList<ExampleSet>();
        RapidMiner.setExecutionMode( RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI );
        RapidMiner.init();
    } 
    
    public void addProcess(RMProcess process)
    {
        if(_processes.containsKey(process.getResourceName()))
        {
            //vyhodit vyjimku
        }
        
        _processes.put(process.getResourceName(), process);
    }
    
    public RMProcess getProcess(String name)
    {
        return _processes.get(name);
    }
    
    public List<RMProcess> getProcesses()
    {
        return new ArrayList<RMProcess>(_processes.values());
    }
    
    public ExampleSet performProcess(String resourceName) throws Exception
    {
        Process process = new Process( getClass().getResourceAsStream( resourceName ) );
        process.setProcessLocation( new FileProcessLocation( new File(".") ) );        
        
        // TODO: B - Info only for debug, after that they'll be removed
        System.out.println(process.getRootOperator().createProcessTree(0));
        
        Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, "RUN");
        Logger.getLogger(ProcessPerformer.class.getName()).fine("RapidMiner Process RUNS");
        process.run();
        
        Collection<Operator> operators = process.getAllOperators();
        
        ExampleSet exampleSet = null;

        
        // TODO: B - Always is taken next to last operator
        int counter = 1;
        for(Operator operator : operators)
        {
            System.out.println(operator.getName());
            System.out.println(operators.size() +" - " + counter);
            if( (operators.size() - 1) == counter)
            {
                System.out.println(operator.getName()+"-----");
                exampleSet = operator.getOutputPorts().getPortByName( EXAMPLESET_OUTPUT_PORT ).getData( ExampleSet.class );
                _exampleSets.add( exampleSet );
                break;
            }
//            
//            if(operator.getName().equals("Sort (2)"))
//                ex = operator.getOutputPorts().getPortByName("example set output").getData();
            
            ++counter;
        }
        return exampleSet;
    }
    
    public void performProcesses() throws Exception
    {
        for(RMProcess process : _processes.values())
        {
            this.performProcess( process.getResourceName() );
        }
    }
    
    public List<DataNode> getProcessesDataNodes() throws Exception
    {
        List<DataNode> dataNodes = new ArrayList<DataNode>();
        
        for(ExampleSet exampleSet : _exampleSets)
            dataNodes.add( ExampleSetConverter.convertToDataNode( exampleSet ) );
        
        return dataNodes;
    }
    
    
}
