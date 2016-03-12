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
package org.seage.ael.agent;

import aglobe.container.agent.CMAgent;
import aglobe.container.task.ConversationUnit;
import aglobe.container.task.Task;
import aglobe.ontology.AgentInfo;
import aglobe.ontology.AglobeParam;
import aglobe.ontology.Message;
import aglobe.service.directory.DirectoryListener;
import aglobe.service.directory.DirectoryRecord;
import aglobe.service.directory.DirectoryService;
import aglobex.protocol.request.RequestParticipantTask;
import java.io.File;
import java.io.PrintStream;
import org.seage.data.DataNode;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.FileHandler;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import java.util.logging.SimpleFormatter;


/**
 *
 * @author Rick
 */
public abstract class AelAgent extends CMAgent implements DirectoryListener
{
    protected enum StateInfo {Ready, InProgress, Success, Failure};

    private int _currentState = 0;
    private StateInfo _currentStateInfo = StateInfo.Ready;
    protected DirectoryService.Shell _directoryShell;
    private PrintStream _logHandler;
    private PrintStream _logHandlerErr;

    public abstract void init(DataNode params) throws Exception;
    public abstract void done() throws Exception;
    void breakAlgorithm(BreakRequestParticipantTask breakTask) throws Exception {} // only algorithm agents implement this method


    protected abstract void processIncomingMessage(Message m);
    protected abstract void processCurrentState() throws Exception;
    protected abstract String getStateName(int stateNum);

    protected String _strOutDir;

    @Override
    public void init(final AgentInfo ai, final int initState)
    {
        //super.init(ai, initState);
        _directoryShell = //(DirectoryService.Shell) getContainer().getServiceManager().
                          //  getService(this, DirectoryService.SERVICENAME);
                  (DirectoryService.Shell) getContainer().getServiceManager().getService(this, DirectoryService.SERVICENAME);
        
        setIdleTask(new Task(this) {

            @Override
            public void handleIncomingMessage(Message m)
            {
                logInfo("Recieved message from: "+ m.getSender().toString()+"/n/t"+ m.toString());
                try
                {
                    if(m.getPerformative().equals("REQUEST"))
                    {
                        if(m.getContent() instanceof DataNode)
                        {
                            if(((DataNode)m.getContent()).getName().equals("agent"))
                                new InitRequestParticipantTask(this.getConversationManager().getConversationUnit(), m, true);
                        }
                        else if(m.getContent().equals("BREAK"))
                            new BreakRequestParticipantTask(this.getConversationManager().getConversationUnit(), m, true);

                    }
                    else if(m.getPerformative().equals("DONE"))
                        done();
                    else
                        processIncomingMessage(m);
                }
                catch(Exception ex)
                {                    
                    logSevere(exception2String(ex));
                }
            }
        });

        new AgentTimerTask().start();

        logInfo("initialized");
    }

    @Override
    public void handleNewRegister(String containerName, DirectoryRecord[] records, String matchingFilter){}
    @Override
    public void handleDeregister(String containerName, DirectoryRecord[] records, String matchingFilter){}
    @Override
    public void handleInvisible(String containerName, DirectoryRecord[] records, String matchingFilter){}
    @Override
    public void handleVisible(String containerName, DirectoryRecord[] records, String matchingFilter){}

    protected void setState(int state)
    {
        _currentState = state;
        _currentStateInfo = StateInfo.Ready;
        logInfo("*** " + getName() + " - "+ getStateName(state) + " - " + _currentStateInfo+" ***");
    }

    protected int getStateEx()
    {
        return _currentState;
    }

    protected void setStateInfo(int state, StateInfo info)
    {
        if(state != _currentState)
        {
            logWarning("State changed: " + getStateName(_currentState) +"("+_currentStateInfo+") -> " + getStateName(state)+"("+info+")");
            return;
        }
        
        _currentStateInfo = info;
        logFine("*** "+getName()+" - " + getStateName(_currentState) + " - " + info+" ***");
    }

    protected boolean currentStateInfo(StateInfo info)
    {
        return _currentStateInfo == info;
    }

    protected void initLogger(String targetDir) throws Exception
    {
        if(_logHandler != null)
            _logHandler.close();
        
        _logHandler = new PrintStream(new File(targetDir+getName()+".log"));
        System.setOut(_logHandler);

        if(_logHandlerErr != null)        
            _logHandlerErr.close();
        
        _logHandlerErr = new PrintStream(new File(targetDir+getName()+".err"));
        System.setErr(_logHandlerErr);

    }

    protected String exception2String(Exception ex)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    private class AgentTimerTask extends Thread
    {
        @Override
        public void run() 
        {
            try
            {
                while(true)
                {
                    processCurrentState();
                    Thread.currentThread().sleep(500);
                }

            }
            catch(Exception ex)
            { 
                logSevere(exception2String(ex));
            }
        }

    }
    
    class InitRequestParticipantTask extends RequestParticipantTask
    { 
        public InitRequestParticipantTask(ConversationUnit owner, Message requestMessage, boolean autostart) {
            super(owner, requestMessage, autostart);
        }

        @Override
        protected void processRequest(Message requestMessage)
        {          
            try
            {
                done();

                DataNode params = (DataNode)requestMessage.getContent();
                requestMessage.setContent(null);
                agree();
                
                _strOutDir = params.getValueStr("outDir");
                initLogger(_strOutDir);

                logInfo(getName() + " - Recieved INIT");

                init(params);
                informDone();
                
            }
            catch(Exception ex)
            {
                logWarning(exception2String(ex));
                failure(ex);
            }
        }

    }

    class BreakRequestParticipantTask extends RequestParticipantTask
    {
        public BreakRequestParticipantTask(ConversationUnit owner, Message requestMessage, boolean autostart) {
            super(owner, requestMessage, autostart);
        }

        @Override
        protected void processRequest(Message requestMessage)
        {
            try
            {
                breakAlgorithm(this);
            }
            catch(Exception ex)
            {
                failure(ex);
            }
        }

        public void sendAgree()
        {
            agree();
        }

        public void sendDone()
        {
            informDone();
        }

        public void sendFailure()
        {
            failure("failure probably during solution storing");
        }
    }
}
