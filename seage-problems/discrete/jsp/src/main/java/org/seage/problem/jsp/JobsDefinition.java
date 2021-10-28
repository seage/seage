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
package org.seage.problem.jsp;

import java.io.InputStream;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JobsDefinition extends ProblemInstance
{
    protected JobInfo[] _jobInfos;
    protected int _numMachines;
    
    public JobsDefinition(ProblemInstanceInfo instanceInfo, InputStream jobsDefinitionStream) throws Exception
    {
        super(instanceInfo);
        
        createJobInfos(jobsDefinitionStream);
    }
    
    public int getJobsCount()
    {
        return _jobInfos.length;
    }
    
    public JobInfo[] getJobInfos()
    {
        return _jobInfos;
    }
    
    public int getMachinesCount()
    {
        return _numMachines;
    }
    
    protected void createJobInfos(InputStream jobsDefinitionStream) throws Exception
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();        
        Document xmlDoc = db.parse(jobsDefinitionStream);        
        NodeList jobNodes = xmlDoc.getDocumentElement().getElementsByTagName("job");
        
        HashSet<Integer> machines = new HashSet<Integer>();
        _jobInfos = new JobInfo[jobNodes.getLength()];
        
        for(int i=0;i<jobNodes.getLength();i++)
        {
            Element jobNode = (Element)jobNodes.item(i);
            Integer jobID = Integer.parseInt(jobNode.getAttributes().getNamedItem("id").getNodeValue());
            NodeList operationNodes = jobNode.getElementsByTagName("operation");
            OperationInfo[] operationInfos = new OperationInfo[operationNodes.getLength()];
            for(int j=0;j<operationNodes.getLength();j++)
            {
                Node operNode = operationNodes.item(j);
                OperationInfo oper = new OperationInfo();
                oper.OperationID = Integer.parseInt(operNode.getAttributes().getNamedItem("number").getNodeValue());
                oper.JobID = jobID;
                oper.MachineID = Integer.parseInt(operNode.getAttributes().getNamedItem("machineID").getNodeValue());
                oper.Length = Integer.parseInt(operNode.getAttributes().getNamedItem("length").getNodeValue());
                operationInfos[j] = oper;
                
                if(!machines.contains(oper.MachineID))
                    machines.add(oper.MachineID);
            }
            _jobInfos[i] = new JobInfo(jobID, operationInfos);
        }
        _numMachines = machines.size();
    }
}
