/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.hh.experimenter._obsolete;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seage.data.DataNode;
import org.slf4j.LoggerFactory;

import com.rapidminer.FileProcessLocation;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.IOObjectCollection;
import com.rapidminer.operator.Operator;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;

/**
 *
 * @author zmatlja1
 */
public class ProcessPerformer
{

    private final String REPORT_NODE_NAME = "Report";
    private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "SEAGE-RM-REPO-old";
    private final String RAPIDMINER_LOCAL_REPOSITORY_PATH = "SEAGE-RM-REPO-old";

    private List<RMProcess> _processes;

    private List<NamedIOObjectNode> _exampleSetNodes;

    public ProcessPerformer() throws Exception
    {
        _processes = new ArrayList<RMProcess>();
        _exampleSetNodes = new ArrayList<NamedIOObjectNode>();

        RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.EMBEDDED_WITHOUT_UI);
        RepositoryManager.getInstance(null).addRepository(
                new LocalRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME, new File(RAPIDMINER_LOCAL_REPOSITORY_PATH)));
        RapidMiner.init();
    }

    public void addProcess(RMProcess process)
    {
        _processes.add(process);
    }

    public List<RMProcess> getProcesses()
    {
        return _processes;
    }

    /**
     * @param RMProcess rmProcess - instance of RMProcess
     * 
     * Method performs a defined RapidMiner proces. Output is setted from process sinks.
     */
    public void performProcessThroughSinks(RMProcess rmProcess) throws Exception
    {
        // RapidMiner process inicialization
        Process process = new Process(getClass().getResourceAsStream(rmProcess.getResourceName()));
        process.setProcessLocation(new FileProcessLocation(new File(".")));

        process.run();

        NamedIOObjectNode namedNode = new NamedIOObjectNode();
        namedNode.setRmProcess(rmProcess);

        for (String port : rmProcess.getOperatorOutputPorts())
        {
            namedNode.addDataFromOutputPort(port,
                    process.getRootOperator().getSubprocess(0).getInnerSinks().getPortByName(port).getAnyDataOrNull());
        }

        _exampleSetNodes.add(namedNode);
    }

    /**
     * @param RMProcess rmProcess - instance of RMProcess
     * 
     * Method performs a defined RapidMiner proces. Output is setted from defined operator and his ports
     */
    public void performProcess(RMProcess rmProcess) throws Exception
    {
        // RapidMiner process inicialization
        Process process = new Process(getClass().getResourceAsStream(rmProcess.getResourceName()));
        process.setProcessLocation(new FileProcessLocation(new File(".")));

        LoggerFactory.getLogger(ProcessPerformer.class.getName()).info(
                process.getRootOperator().createProcessTree(0));

        process.run();

        Collection<Operator> operators = process.getAllOperators();

        for (Operator operator : operators)
        {
            if (operator.getName().equals(rmProcess.getOperatorName()))
            {
                NamedIOObjectNode namedNode = new NamedIOObjectNode();
                namedNode.setRmProcess(rmProcess);

                for (String port : rmProcess.getOperatorOutputPorts())
                    namedNode.addDataFromOutputPort(port,
                            operator.getOutputPorts().getPortByName(port).getAnyDataOrNull());

                _exampleSetNodes.add(namedNode);
            }
        }
    }

    /**
     * Iterates over all defined RMProcesses and preform them.
     */
    public void performProcesses() throws Exception
    {
        for (RMProcess process : _processes)
        {
            this.performProcess(process);
        }
    }

    @SuppressWarnings("unchecked")
    public List<DataNode> getProcessesDataNodes() throws Exception
    {
        for (NamedIOObjectNode exampleSetNode : _exampleSetNodes)
        {
            Set<Map.Entry<String, IOObject>> set = exampleSetNode.getMap().entrySet();
            Iterator<Map.Entry<String, IOObject>> iterator = set.iterator();
            DataNode dataNode = null;

            while (iterator.hasNext())
            {
                Map.Entry<String, IOObject> entry = iterator.next();

                IOObject obj = entry.getValue();
                if (obj instanceof IOObjectCollection)
                    dataNode = ExampleSetConverter
                            .convertToDataNodeFromCollection((IOObjectCollection<ExampleSet>) obj);
                else if (obj instanceof ExampleSet)
                    dataNode = ExampleSetConverter.convertToDataNode((ExampleSet) obj);

                exampleSetNode.addDataNode(dataNode);
            }

        }

        return mergeDataNodes();
    }

    private List<DataNode> mergeDataNodes() throws Exception
    {
        // Sort Nodes by RMProces Report name
        Collections.sort(_exampleSetNodes, new Comparator<NamedIOObjectNode>()
        {
            @Override
            public int compare(NamedIOObjectNode obj1, NamedIOObjectNode obj2)
            {
                return obj1.getRmProcess().getReportName().compareTo(obj2.getRmProcess().getReportName());
            }
        });

        DataNode dataNode = new DataNode(REPORT_NODE_NAME);
        String lastReportName = null;
        List<DataNode> dataNodes = new ArrayList<DataNode>();
        int counter = 0;

        for (NamedIOObjectNode objectNode : _exampleSetNodes)
        {
            if (lastReportName != null && !lastReportName.equals(objectNode.getRmProcess().getReportName()))
            {
                dataNodes.add(dataNode);
                dataNode = new DataNode(REPORT_NODE_NAME);
            }
            counter++;

            for (DataNode dn : objectNode.getDataNodes())
                dataNode.putDataNode(dn);

            if (counter == _exampleSetNodes.size())
                dataNodes.add(dataNode);

            lastReportName = objectNode.getRmProcess().getReportName();
        }

        return dataNodes;
    }

}
