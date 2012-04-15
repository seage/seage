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
    
    private List<RMProcess> _processes;
    
    private List<NamedExampleSetNode> _exampleSetNodes;
        
    public ProcessPerformer()
    {
        _processes = new ArrayList<RMProcess>();
        _exampleSetNodes = new ArrayList<NamedExampleSetNode>();
        
        RapidMiner.setExecutionMode( RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI );
        RapidMiner.init();
    } 
    
    public void addProcess(RMProcess process)
    {
        _processes.add( process );
    }
    
    public List<RMProcess> getProcesses()
    {
        return _processes;
    }
    
    private class NamedExampleSetNode
    {
        private ExampleSet exampleSet;
        private DataNode dataNode;
        private String name;

        public NamedExampleSetNode(ExampleSet exampleSet, String name, DataNode dataNode) {
            this.exampleSet = exampleSet;
            this.dataNode = dataNode;
            this.name = name;
        }

        public ExampleSet getExampleSet() {
            return exampleSet;
        }

        public void setExampleSet(ExampleSet exampleSet) {
            this.exampleSet = exampleSet;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DataNode getDataNode() {
            return dataNode;
        }

        public void setDataNode(DataNode dataNode) {
            this.dataNode = dataNode;
        }
        
    }
    
    public ExampleSet performProcess(String resourceName) throws Exception
    {
        Process process = new Process( getClass().getResourceAsStream( resourceName ) );
        process.setProcessLocation( new FileProcessLocation( new File(".") ) );        
        
        // TODO: B - Info only for debug, after that they'll be removed
        
        Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, process.getRootOperator().createProcessTree(0));
        
        Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, "RapidMiner Process RUNS");
        Logger.getLogger(ProcessPerformer.class.getName()).fine("RapidMiner Process RUNS");
        process.run();

        Collection<Operator> operators = process.getAllOperators();
        
        ExampleSet exampleSet = null;
        
        // TODO: B - Always is taken next to last operator
        int counter = 1;
        for(Operator operator : operators)
        {
            if( ( operators.size() - 1 ) == counter)
            {
                exampleSet = operator.getOutputPorts().getPortByName( EXAMPLESET_OUTPUT_PORT ).getData( ExampleSet.class );
                _exampleSetNodes.add( new NamedExampleSetNode( exampleSet , resourceName , null ) );
            }
            
            ++counter;
        }
        return exampleSet;
    }
    
    public void performProcesses() throws Exception
    {
        for(RMProcess process : _processes)
        {
            this.performProcess( process.getResourceName() );
        }
    }
    
    public List<DataNode> getProcessesDataNodes() throws Exception
    {        
        for(NamedExampleSetNode exampleSetNode : _exampleSetNodes)
        {            
            DataNode dataNode = ExampleSetConverter.convertToDataNode( exampleSetNode.getExampleSet() );
            exampleSetNode.setDataNode(dataNode);
        }
        
        return mergeDataNodes();
    }
    
    private List<DataNode> mergeDataNodes() throws Exception
    {
        List<DataNode> dataNodes = new ArrayList<DataNode>();
        
        DataNode dataNode = new DataNode("Report");
        String lastReportName = null;
        
        int counter = 0;
        for(NamedExampleSetNode exampleSetNode : _exampleSetNodes)
        {
            String reportName = exampleSetNode.getName().substring( 0 , exampleSetNode.getName().lastIndexOf('-') );

            if( lastReportName != null && !lastReportName.equals( reportName ) )
            {
                dataNodes.add(dataNode);
                dataNode = new DataNode("Report");
            }
            counter++;
            
            dataNode.putDataNode( exampleSetNode.getDataNode() );
            
            if(counter == _exampleSetNodes.size() )
                dataNodes.add(dataNode);
            
            lastReportName = reportName;
        }

        return dataNodes;
    }    
    
}
