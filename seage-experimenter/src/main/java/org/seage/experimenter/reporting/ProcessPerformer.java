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

import com.rapid_i.repository.wsimport.StoreProcess;
import com.rapidminer.FileProcessLocation;
import com.rapidminer.RapidMiner;
import com.rapidminer.Process;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.IOObjectCollection;
import com.rapidminer.operator.Operator;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.ExampleSetConverter;

/**
 *
 * @author zmatlja1
 */
public class ProcessPerformer {
    
    private String REPORT_NODE_NAME = "Report";
    
    private List<RMProcess> _processes;
    
    private List<NamedIOObjectNode> _exampleSetNodes;
        
    public ProcessPerformer() throws Exception
    {
        _processes = new ArrayList<RMProcess>();
        _exampleSetNodes = new ArrayList<NamedIOObjectNode>();

        RapidMiner.setExecutionMode( RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI );
        
        
        //RepositoryManager.getInstance(new RepositoryAccessor() {}).addRepository(new LocalRepository("SEAGE", new File("../SREPO")));        
        RepositoryManager.getInstance(null).addRepository(new LocalRepository("MyRepo1", new File("SREPO")));

        
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

    public void performProcess(RMProcess rmProcess) throws Exception
    {
        Process process = new Process( getClass().getResourceAsStream( rmProcess.getResourceName() ) );
        process.setProcessLocation( new FileProcessLocation( new File(".") ) );        
        
        // TODO: B - Info only for debug, after that they'll be removed        
        Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, process.getRootOperator().createProcessTree(0));

        process.run();

        Collection<Operator> operators = process.getAllOperators();

        for(Operator operator : operators)
        {
            if(operator.getName().equals( rmProcess.getOperatorName() ) )
            {
                NamedIOObjectNode namedNode = new NamedIOObjectNode();
                namedNode.setRmProcess( rmProcess );
                
                for(String port : rmProcess.getOperatorOutputPorts())
                    namedNode.addDataFromOutputPort( port , operator.getOutputPorts().getPortByName( port ).getAnyDataOrNull() );
   
                _exampleSetNodes.add( namedNode );
            }
        }
    }
    
    public void performProcesses() throws Exception
    {
        for(RMProcess process : _processes)
        {
            this.performProcess( process );
        }
    }
    
    public List<DataNode> getProcessesDataNodes() throws Exception
    {        
        for(NamedIOObjectNode exampleSetNode : _exampleSetNodes)
        {
            Set set = exampleSetNode.getMap().entrySet();            
            Iterator<IOObject> iterator = set.iterator();
            DataNode dataNode = null;
            
            while( iterator.hasNext() )
            {
                Map.Entry<String, IOObject> entry = (Map.Entry)iterator.next();
                
                IOObject obj = entry.getValue();
                if(obj instanceof IOObjectCollection)
                    dataNode = ExampleSetConverter.convertToDataNodeFromCollection( (IOObjectCollection)obj );
                else if(obj instanceof ExampleSet)
                    dataNode = ExampleSetConverter.convertToDataNode( (ExampleSet)obj );
                
                exampleSetNode.addDataNode( dataNode );
            }            

        }
        
        return mergeDataNodes();
    }
    private List<DataNode> mergeDataNodes() throws Exception
    {
        // Sort Nodes by RMProces Report name
        Collections.sort( _exampleSetNodes , new Comparator<NamedIOObjectNode>()
        {
            @Override
            public int compare(NamedIOObjectNode obj1, NamedIOObjectNode obj2)
            {
                return obj1.getRmProcess().getReportName().compareTo(obj2.getRmProcess().getReportName());
            }
        });
        
        DataNode dataNode = new DataNode( REPORT_NODE_NAME );
        String lastReportName = null;
        List<DataNode> dataNodes = new ArrayList<DataNode>();
        int counter = 0;
        
        for(NamedIOObjectNode objectNode : _exampleSetNodes)
        {
            if( lastReportName != null && !lastReportName.equals( objectNode.getRmProcess().getReportName() ) )
            {
                dataNodes.add(dataNode);
                dataNode = new DataNode( REPORT_NODE_NAME );
            }
            counter++;
            
            for(DataNode dn : objectNode.getDataNodes())
                dataNode.putDataNode( dn );
            
            if(counter == _exampleSetNodes.size() )
                dataNodes.add(dataNode);
            
            lastReportName = objectNode.getRmProcess().getReportName();
        }
        
        return dataNodes;
    }

}
