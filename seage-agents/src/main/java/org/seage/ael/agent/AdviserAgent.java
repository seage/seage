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

import aglobe.ontology.Message;
import aglobe.ontology.MessageConstants;
import aglobex.protocol.queryref.QueryRefParticipantTask;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.reasoning.algparams.ReasonerFactory;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 *
 * @author Richard Malek
 */
public class AdviserAgent extends AelAgent
{
    private Reasoner _reasoner;

    private final int stINIT = 1;               // "Initialization"
    private final int stREASONING = 2;          // "Querying ProblemAgent"
    private String[] stNames = {"Uninitialized", "Initialization", "Querying ProblemAgent"};

    @Override
    public void init(DataNode params)
    {
        // TODO: C - Add DataNode validation against the XSD schema
        try                
        {
            setState(stINIT);
            setStateInfo(stINIT, StateInfo.InProgress);
            DataNode adviserNode = params.getDataNode("adviser");
            ReasonerFactory factory = (ReasonerFactory)Class.forName(adviserNode.getValueStr("factoryClass")).newInstance();
            _reasoner = factory.createReasoner(adviserNode);
            
            ArrayList<String> service = new ArrayList<String>();
            service.add(adviserNode.getValueStr("id"));
            _directoryShell.register(this, service);
            setStateInfo(stINIT, StateInfo.Success);
        }
        catch(Exception ex)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            ex.printStackTrace(pw);
            pw.flush();
            sw.flush();

            logSevere(sw.toString());
        }
    }

    @Override
    public void done()
    {        
    }
 
    @Override
    protected void processIncomingMessage(Message m) 
    {
        if(getStateEx() == stREASONING)
            if(m.getPerformative().equals(MessageConstants.QUERY_REF))
                new AdviceQueryRefParticipantTask(this, m);
    }

    @Override
    protected void processCurrentState()
    {
        switch(getStateEx())
        {
            case stINIT:
                if(currentStateInfo(StateInfo.Success))
                    setState(stREASONING);
                return;
            case stREASONING:
                return;
        }
    }

    @Override
    protected String getStateName(int stateNum) 
    {
        return stNames[stateNum];
    }

    protected DataNode getAdvise(DataNode report)
    {
        return report;
    }
    
    private class AdviceQueryRefParticipantTask extends QueryRefParticipantTask
    {
        public AdviceQueryRefParticipantTask(AdviserAgent owner, Message queryMessage)
        {
            super(owner, queryMessage, true);
        }

        @Override
        protected void processQuery(Message queryMessage)
        {
            if(!(queryMessage.getContent() instanceof AlgorithmReport))
                failure("Unknown message");
                 
            AlgorithmReport report = (AlgorithmReport)queryMessage.getContent();
            
            try
            {
                _reasoner.putPolicyReport(report);
                informResult(_reasoner.getPolicy());
            }
            catch(Exception ex)
            {
                log.error(ex);
            }
        }
    
    }
}
