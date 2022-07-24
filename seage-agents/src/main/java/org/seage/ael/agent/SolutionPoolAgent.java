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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.ael.agent;

import org.seage.data.DataNode;

import aglobe.container.transport.Address;
import aglobe.ontology.Message;
import aglobe.service.directory.DirectoryRecord;
import aglobex.protocol.cnp.CNPParticipantTask;

import aglobex.protocol.queryref.QueryRefInitiatorTask;
import org.seage.data.*;
import org.seage.data.ds.*;
import org.seage.data.xml.XmlHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * 
 * @author Richard Malek
 */
public class SolutionPoolAgent extends AelAgent
{
    protected DataStore _dataStore;
//    protected PhenotypeEvaluator _evaluator;
    
    private Address _problemAgentAddress;
//    protected String _problemName;

    private int _capacity;
    
//    private double[] _bestEverFitness;
//    private Object[] _bestEverSubject;


    private final int stINIT = 1;                   // "Initialization
    private final int stQUERYINITPOPULATION = 2;    // "Querying initial population"
    private final int stLOADSTORE = 3;              // "Working"

    private final String[] stNames
            = new String[] {"", "Initialization", "Querying initial population", "Working"};

//    private ArrayList<String> _allowedAgents;
    // report on best solutions
//    private DataNode _reportLog;

    // <editor-fold desc="Overriden methods">
    @Override
    public void init(DataNode params) throws Exception
    {
        // TODO: C - Add DataNode validation against the XSD schema
        logInfo("INIT");
        setState(stINIT);
        setStateInfo(stINIT, StateInfo.InProgress);

        System.out.println(this.getName() + " initialized with address "
            + this.getAddress().toString());

//        DataNode evaluatorParams = params.getDataNode("evaluator");
//        PhenotypeEvaluatorFactory factory = (PhenotypeEvaluatorFactory)Class.forName(evaluatorParams.getValueStr("factoryClass")).newInstance();
//        _evaluator = factory.createPhenotypeEvaluator(evaluatorParams);

        _capacity = params.getDataNode("solutionPool").getValueInt("capacity");
        
        _dataStore = new DataStore();
        _dataStore.put("solutions", new DataTable());
        
        _directoryShell.subscribe(this, "Problem");

//        _bestEverFitness = null;
//        _bestEverSubject = null;

//        _allowedAgents = new ArrayList<String>();
//        for(DataNode dn : params.getDataNodes("answerTo"))
//            _allowedAgents.add(dn.getValueStr("agent"));
//        _reportLog = new DataNode("SolutionPool");

        //throw new Exception("KO");
        setStateInfo(stINIT, StateInfo.Success);

    }
    
    @Override
    public void done()
    {
        logInfo("DONE");
        
//        if(_reportLog != null)
//            XmlHelper.writeXml(_reportLog, _strOutDir+getName()+".xml");
    }

    @Override
    protected void processIncomingMessage(Message m) 
    {
        System.out.println(getName() + " - " + stNames[getStateEx()]+"\n\t"+m);
        if(getStateEx() == stLOADSTORE)
        {
            try
            {
                if(m.getPerformative().equals("CFP"))
                    new SolutionCNPParticipantTask(this, 10000, m);
                if(m.getPerformative().equals("INFORM") && m.getContent() instanceof Object[][])
                    insertSolutions((Object[][])m.getContent(), m.getSender().getName());
            }
            catch(Exception ex)
            {
                logWarning(ex.toString());
            }
        }
    }

    @Override
    protected void processCurrentState()
    {
        switch(getStateEx())
        {
            case stINIT:
                if(currentStateInfo ( StateInfo.Success))
                    if(_problemAgentAddress != null)
                        setState(stQUERYINITPOPULATION);
                return;

//            case stQUERYPROBLEMINFO:
//                if(currentStateInfo ( StateInfo.Ready) || currentStateInfo ( StateInfo.Failure))
//                {
//                    setStateInfo (StateInfo.InProgress);
//                    new ProblemInfoQueryRefInitiatorTask(this, _problemAgentAddress, new String[]{"PhenotypeEvaluator"});
//                }
//                if(currentStateInfo ( StateInfo.Success))
//                    setState(stQUERYINITPOPULATION);
//                return;

            case stQUERYINITPOPULATION:
                if(currentStateInfo ( StateInfo.Ready) || currentStateInfo ( StateInfo.Failure))
                {
                    setStateInfo(stQUERYINITPOPULATION, StateInfo.InProgress);
                    new InitialPopulationQueryRefInitiatorTask(this, _problemAgentAddress, _capacity);
                }
                if(currentStateInfo ( StateInfo.Success))
                    setState(stLOADSTORE);
                return;

            case stLOADSTORE:
                if(currentStateInfo ( StateInfo.Ready) || currentStateInfo ( StateInfo.Failure))
                {
                    setStateInfo(stLOADSTORE, StateInfo.InProgress);
                    try
                    {
                        Collection<String> list = new ArrayList<String>();
                        list.add("SolutionPool");
                    
                        _directoryShell.register(this, list);
                        logInfo("SolutionPool service registered");
                    }
                    catch(Exception ex)
                    {
                        setStateInfo(stLOADSTORE, StateInfo.Failure);
                    }
                }
                return;

        }
    }

    @Override
    protected String getStateName(int stateNum) {
        return stNames[stateNum];
    }
    
    @Override
    public final void handleNewRegister(String containerName, DirectoryRecord[] records, String matchingFilter)
    {
        for(DirectoryRecord dr :  records)
        {
            for(String service : dr.getServices())            
                if(service.equals("Problem") && _problemAgentAddress == null)
                {
                    _problemAgentAddress = dr.address;                   
                    logFine("ProblemAgent registered");
                }
        }
    }
    
    @Override
    public final void handleDeregister(String containerName, DirectoryRecord[] records, String matchingFilter)
    {
        for(DirectoryRecord dr : records)        
            if(dr.address ==  _problemAgentAddress)            
                _problemAgentAddress = null;
    }    
    // </editor-fold>

    // <editor-fold desc="Private methods">
    // TODO: B - Add read-only option for SolutionPool
    private synchronized Object[][] removeSolutions(int numSolution)
    {        
        DataTable dt =  _dataStore.get("solutions");
        
        numSolution = Math.min(numSolution, dt.size());
        
        Object[][] result = new Object[numSolution][];
        ArrayList<DataRow> removed = new ArrayList<DataRow>();
        
        int[] ixs = getRandomPoints(result.length, dt.size());
        
        for(int i = 0; i<result.length;i++ )
        {
            DataRow dr = dt.get(ixs[i]);
            removed.add(dr);
            result[i] = new Object[dr.size()];
            
            for(int j=0;j<result[i].length;j++)
                result[i][j] = dr.get(j).getCellProperty();             
        }
        synchronized(dt)
        {
            dt.removeAll(removed);
        }
        return result;
    }
    
    private void insertSolutions(Object[][] solutions, String agentName) throws Exception
    {        
        DataTable dt =  _dataStore.get("solutions");
        boolean newBest = false;
        for(int i = 0; i<solutions.length;i++ )
        {
            DataRow row = new DataRow(solutions[i].length);
            
            synchronized(dt) 
            {
                dt.add(row);
            }

            
//            double[] solutionQuality = _evaluator.evaluate(solutions[i]);
//            if(_evaluator.compare(solutionQuality, _bestEverFitness) == 1)
//            {
//                _bestEverFitness = solutionQuality.clone();
//                _bestEverSubject = solutions[i].clone();
//
//                //System.out.print("+++" + agentName + "  " + solutionQuality[0] + "   ");
//                //for(int j=0;j<_bestEverSubject.length;j++) System.out.print((Integer)_bestEverSubject[j]+1 + " ");
//                //System.out.println();
//                newBest = true;
//            }
            

            for(int j=0;j<solutions[i].length;j++)
            {
                row.get(j).setCellProperty(solutions[i][j]);
            }
        }
//        if(newBest)
//        {
//            logInfo(agentName + " -> " + _bestEverFitness[0]);
//
//            DataNode newNode = new DataNode("Solution");
//            newNode.putValue("agentName", agentName);
//            newNode.putValue("fitness", _bestEverFitness[0]);
//            String data = "";
//            for(int i=0;i<_bestEverSubject.length;i++)
//                data += _bestEverSubject[i].toString() + " ";
//            newNode.putValue("solution", data);
//            _reportLog.putDataNode(newNode);
//        }
    }
    
    private int[] getRandomPoints(int num, int max)
    {
        int[] result = new int[num];
        int[] points = new int[max];
        
        Random rnd = new Random();
        
        for(int i=0;i<max;i++) points[i]=i;
        for(int i=0;i<max*100;i++) 
        {
            int ix1 = rnd.nextInt(max);
            int ix2 = rnd.nextInt(max);
            int tmp = points[ix1];
            points[ix1] = points[ix2];
            points[ix2] = tmp;            
        }
        
        for(int i=0;i<num;i++) result[i] = points[i];
        
        return result;
    }
    // </editor-fold>
    
    // <editor-fold  desc="Sealed Task classes">

    private enum MsgType {PUT, GET}
    
    private class SolutionCNPParticipantTask extends CNPParticipantTask
    {
        private MsgType _msgType;
        private int _amount;     
        
        
        public SolutionCNPParticipantTask(SolutionPoolAgent owner, int msec, Message m)
        {
            super(owner, msec, m, true);
            
            try
            {
                DataNode msgElem = XmlHelper.readXml(cfpMessage.getContent().toString());
                _msgType = msgElem.getName().equals("put") ? MsgType.PUT : MsgType.GET;
                _amount = msgElem.getValueInt("amount");
            }
            catch(Exception ex)
            {
                logWarning(ex.toString());
            }
        }

        @Override
        protected void prepareProposal()
        {
            try
            {
//                if(_allowedAgents.contains(initiator.getName()) || _allowedAgents.contains("all"))
                {
                    int numProposal = Math.min(_amount, _dataStore.get("solutions").size());
                    sendProposal(numProposal);
                    logFinest("SolutionCNPParticipantTask sent offer for " + numProposal + " solutions");
                }
//                else
//                    sendRefuse("You are not in my accept list");
            }
            catch(Exception ex)
            {
                logWarning(ex.toString());
            }
        }

        @Override
        protected void proposalAccepted(Message acceptMessage)
        {   try
            {                
                if(_msgType==MsgType.GET)
                {                      
                    sendDone(removeSolutions(_amount));
                    logFinest("SolutionCNPParticipantTask sent " + _amount + " solutions");
                }
                if(_msgType==MsgType.PUT && acceptMessage.getContent() != null)
                {
                    Object[][] solutions = (Object[][])acceptMessage.getContent();
                    insertSolutions(solutions, acceptMessage.getSender().getName());      
                    logFinest("SolutionCNPParticipantTask stored " + solutions.length + " solutions");
                    sendDone("OK");
                }
                
                cancelTask();
            }
            catch(Exception ex)
            {
                logWarning(ex.toString());
            }
        }

        @Override
        protected void proposalRefused(Message refuseMessage)
        {
            logFinest("proposalRefused");
        }

        
    }
    
//    private class ProblemInfoQueryRefInitiatorTask extends QueryRefInitiatorTask
//    {
//        private SolutionPoolAgent _owner;
//
//        public ProblemInfoQueryRefInitiatorTask(SolutionPoolAgent owner, Address participant, String[] query)
//        {
//            super(owner, participant, query, true);
//            _owner = owner;
//            logFinest("ProblemInfoQueryRefInitiatorTask - query: " + query.length + " item(s)");
//        }
//
//        @Override
//        protected void informResult(Object result)
//        {
//            try
//            {
//                HashMap<String, Object> data = (HashMap<String, Object>)result;
//                _owner.touchProblemData(data);
//                setStateInfo(StateInfo.Success);
//                logFinest("Recieved " + data.size() + " item(s)");
//            }
//            catch(Exception ex)
//            {
//                setStateInfo(StateInfo.Failure);
//                logWarning(ex.toString());
//            }
//        }
//
//        @Override
//        protected void queryRefused()
//        {
//            setStateInfo(StateInfo.Failure);
//            logFinest("queryRefused");
//        }
//
//        @Override
//        protected void timeout()
//        {
//            if(currentStateInfo (StateInfo.InProgress))
//            {
//                setStateInfo(StateInfo.Failure);
//                logFinest("timeout");
//                cancelTask();
//            }
//        }
//    }

    private class InitialPopulationQueryRefInitiatorTask extends QueryRefInitiatorTask
    {
        private SolutionPoolAgent _owner;

        public InitialPopulationQueryRefInitiatorTask(SolutionPoolAgent owner, Address participant, int amount)
        {
            super(owner, participant, amount, true);
            _owner = owner;
            logFinest("InitialPopulationQueryRefInitiatorTask: " + amount);
        }

        @Override
        protected void informResult(Object result)
        {
            try
            {
                 if(!currentStateInfo(StateInfo.Failure))
                 {
                     logFinest("Recieved: " + ((Object[][])result).length + " solutions");
                 
                    _owner.insertSolutions((Object[][])result, participant.getName());
                    setStateInfo(stQUERYINITPOPULATION, StateInfo.Success);
                 }
            }
            catch(Exception ex)
            {
                setStateInfo(stQUERYINITPOPULATION, StateInfo.Failure);
                logWarning(ex.toString());
            }
        }

        @Override
        protected void queryRefused()
        {
            setStateInfo(stQUERYINITPOPULATION, StateInfo.Failure);
            logFinest("queryRefused");
        }


        @Override
        protected void timeout() 
        {
            if(currentStateInfo ( StateInfo.InProgress))
            {
                setStateInfo(stQUERYINITPOPULATION, StateInfo.Failure);
                logFinest("timeout");
                cancelTask();
            }
        }
    }
    // </editor-fold>
}
