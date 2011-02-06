package org.seage.ael.agent;

/**
 *
 * @author Rick
 */

import aglobe.container.AgentContainer;
import aglobe.container.agent.CMAgent;
import aglobe.container.task.ConversationUnit;
import aglobe.container.transport.Address;
import aglobe.ontology.*;

import aglobex.protocol.queryref.QueryRefParticipantTask;
import aglobex.protocol.request.RequestInitiatorTask;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.seage.aal.data.ProblemConfig;

public class ProblemAgent extends AelAgent
{
    private String _configPath = "config-agents.xml";
    private String _outputPath;

    private final int stPREINIT = 1;                // 
    private final int stINIT = 2;                   // "Initialization"
    private final int stINSTANCING = 3;             //
    private final int stSENDINGINIT = 4;            // "Sending init to all agents"
    private final int stSENDINGBREAK = 5;
    private final int stCOUNTDOWN = 6;              // "Waiting until timeout"
    private final int stSENDINGDONE = 7;            // "Sending done to all agents"

    private final String[] stNames = new String[] {
        "Unknown", "stPREINIT - Pre-initialization",
        "stINIT - Initialization", "stINSTANCING - Instancing the problem",
        "stSENDINGINIT - Sending init to all agents", "stSENDINGBREAK",
        "stCOUNTDOWN - Waiting until timeout", "stSENDINGDONE - Sending done to all agents"};

    private InitSender _initSender;

    private String _initTime;
    private int _instanceIx;
    private long _timeout;
    private long _numberOfRuns;

    private long _startTime;
    private DataNode _reportLog;
    private double[] _bestEverFitness;
    private Object[] _bestEverSubject;

    private int _breakAccepted = 0;
    private int _breakDone = 0;

    protected DataNode _params;
    protected HashMap<Address, DataNode> _agentsParams;
    protected List<DataNode> _instanceInfos;
    protected IPhenotypeEvaluator _evaluator;

    protected IProblemProvider _problemProvider;

    @Override
    public void init(AgentInfo ai, int initState) 
    {
        super.init(ai, initState);

        setState(stPREINIT);
        setStateInfo(stPREINIT, StateInfo.InProgress);

        _agentsParams = new HashMap<Address, DataNode>();
        _instanceInfos = new ArrayList<DataNode>();
        _instanceIx = 0;
        _initSender = new InitSender();        
        
        try
        {
            // read config file
            _configPath = getContainer().getProperty("config");
            DataNode config = XmlHelper.readXml(new File(_configPath));
            List<DataNode> param = config.getDataNodes();

            _params = param.get(0);

            initInputOutput(config.hash());
             XmlHelper.writeXml(config, _outputPath+"/config.xml");

            _problemProvider = (IProblemProvider)Class.forName(_params.getDataNode("problem").getValueStr("problemProvider")).newInstance();

            // create all other agents
            for(int i=1;i<param.size();i++)
            {
                int instances = 1;
                if(param.get(i).containsValue("instances"))
                    instances = param.get(i).getValueInt("instances");
                for(int j=0;j<instances;j++)
                {
                    String agentName = param.get(i).getValueStr("name");
                    if(instances > 1) agentName +="-"+j; 
                    Address a = createAgent(agentName, param.get(i).getValueStr("mainClass"));
                    _agentsParams.put(a, param.get(i));
                }
            }
            setState(stINIT);
        }
        catch(Exception ex)
        {
            setStateInfo(stPREINIT, StateInfo.Failure);
            logSevere(exception2String(ex));
        }
    }

    private void initInputOutput(String hash) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd-HHmmss");
        _initTime = formatter.format(new Date());
        
        _outputPath = _params.getDataNode("problem").getValueStr("outputDir");
        _outputPath += "/"+_initTime;
        _outputPath += "-"+hash;
        new File(_outputPath).mkdirs();       
    }

    @Override
    public void init(DataNode params) throws Exception
    {
        logFine("init");
        _timeout = params.getDataNode("problem").getValueLong("timeoutMS");
        _numberOfRuns = params.getDataNode("problem").getValueLong("numberOfRuns");
        _instanceInfos = params.getDataNode("problem").getDataNodes("instance");
        //_reportLog = new DataNode("Problem");
    }

    @Override
    public void done()
    {}


    @Override
    protected void processCurrentState() throws Exception
    {
        //logInfo(getName() + " - " + stNames[_currentState]);
        switch(getStateEx())
        {
            case stINIT:
                if(currentStateInfo(StateInfo.Ready))
                {
                    init(_params);
                    setState(stINSTANCING);
                }
                return;
                
            case stINSTANCING:
                if(currentStateInfo(StateInfo.Ready))
                {
                    _problemProvider.initProblemInstance((ProblemConfig)_params.getDataNode("problem"));
                
                    DataNode evaluatorParams = _params.getDataNode("problem").getDataNode("evaluator");
                    //IProblemFactory factory = (IProblemFactory)Class.forName(evaluatorParams.getValueStr("factoryClass")).newInstance();
                    _evaluator = (IPhenotypeEvaluator)Class.forName(evaluatorParams.getValueStr("mainClass")).newInstance();

                    //_evaluator = factory.createPhenotypeEvaluator(evaluatorParams);
                    
                    // register service
                    Collection<String> list = new ArrayList<String>();
                    list.add("Problem");
                    _directoryShell.register(this, list);

                    setState(stSENDINGINIT);
                }
                return;
            
            case stSENDINGINIT:
                if(currentStateInfo(StateInfo.Ready))
                {
                     setStateInfo(stSENDINGINIT, StateInfo.InProgress);
                     _numberOfRuns--;                     
                     _bestEverFitness = null;

                     _reportLog = new DataNode("Problem");
                     _reportLog.putValue("startTime", _initTime);
                     _reportLog.putValue("instance", _instanceInfos.get(_instanceIx).getValueStr("name"));
                     _initSender.prepare(_configPath, _agentsParams);
                }
                if(currentStateInfo(StateInfo.InProgress))
                {
                    // if nothing is to be sent then success and continue next state
                    if(_initSender.send(this))
                        setState(stCOUNTDOWN);
                }

                return;
            
            case stCOUNTDOWN:
                if(currentStateInfo(StateInfo.Ready))
                {
                    _startTime = System.currentTimeMillis();
                    setStateInfo(stCOUNTDOWN, StateInfo.InProgress);
                }
                if(currentStateInfo(StateInfo.InProgress))
                {
                    long time = (System.currentTimeMillis() - _startTime)/1000;
                    if(time > _timeout)
                        setState(stSENDINGBREAK);
                }

                return;

            case stSENDINGBREAK:
                if(currentStateInfo(StateInfo.Ready))
                {
                    _breakAccepted = 0;
                    _breakDone = 0;
                    for(Address a : _agentsParams.keySet())
                        new BreakRequestInitiatorTask(this, a, "BREAK", true);
                    Thread.currentThread().sleep(1000);
                    setStateInfo(stSENDINGBREAK, StateInfo.InProgress);
                    return;
                }
                if(currentStateInfo(StateInfo.InProgress))
                    if(_breakDone != 0 && _breakAccepted == _breakDone)
                        setState(stSENDINGDONE);
                if(currentStateInfo(StateInfo.Failure))
                    setState(stSENDINGBREAK);
                return;
                
            case stSENDINGDONE:
                if(currentStateInfo(StateInfo.Ready))
                {
                    if(_numberOfRuns > 0)
                    {
                        setState(stSENDINGINIT);
                    }
                    else
                    {
                        sendDone();
                        _instanceIx++;

                        // deregister service
                        Collection<String> list = new ArrayList<String>();
                        list.add("Problem");
                        _directoryShell.deregister(list);

                        if(_instanceIx < _instanceInfos.size())
                            setState(stINIT);
                        else
                        {
                            setStateInfo(stSENDINGDONE, StateInfo.Success);
                            //quit();
                            logInfo("Everything DONE");
                        }
                    }                    
                }
                return;
        }
    }

    private void quit()
    {
        AgentContainer.CommandService.Shell s = (AgentContainer.CommandService.
                                                 Shell) getContainer().getServiceManager().
              getService(null, AgentContainer.COMMANDSERVICE);
        Command c = new Command();
        c.setName(AgentContainer.CommandService.QUIT);
        s.execute(c);
    }

    @Override
    protected String getStateName(int stateNum) {
        return stNames[stateNum];
    }

    @Override
    protected void processIncomingMessage(Message m)
    {
        if(getStateEx() > stINSTANCING)
        {
            try
            {
                if(m.getPerformative().equals(MessageConstants.QUERY_REF))
                    new ProblemQueryRefParticipantTask(this, m);
                if(m.getPerformative().equals(MessageConstants.INFORM) && m.getContent() instanceof Object[][])
                    handleBestSolution(m.getSender().getName(), (Object[][])m.getContent()); // TODO: A - Store new best solution
            }
            catch(Exception ex)
            {
                logWarning(exception2String(ex));
            }
        }
        else
            logWarning(m.toString());
    }
    
    private Address createAgent(String name, String mainClass) throws  Exception
    {
        Address result = null;
        AgentInfo ai= new AgentInfo();
        ai.setName(name);
        ai.setReadableName(name);
        ai.setType("");
        ai.setMainClass(mainClass);

        Libraries l= new Libraries();

        ai.setLibraries(l);

        getContainer().getAgentManager().createAgent(ai, false);
        Thread.currentThread().sleep(50);
        int i = 0;
        while(true)
        {
            result = getContainer().getAgentManager().getAgentInstance(name).getAddress();
            if(result == null || result.getName().equals(""))
            {
                logWarning("Unable to create the agent. Trying again.");
                if(i++==5)
                    throw new Exception("Unable to create the agent: " + name);
                Thread.currentThread().sleep(500);
            }
            else return result;
        }

    }

    private void sendDone() throws Exception
    {
        for(Entry<Address, DataNode> e : _agentsParams.entrySet())
        {
            Message m = Message.newInstance();
            m.setPerformative("DONE");
            m.setSender(getAddress());
            m.setReceiver(e.getKey());
            sendMessage(m);
        }
        setStateInfo(stSENDINGDONE, StateInfo.Success);
    }  
    
    private void handleBestSolution(String agentName, Object[][] solutions) throws Exception
    {
        boolean newBest = false;
        for(int i = 0; i<solutions.length;i++ )
        {
            double[] solutionQuality = _evaluator.evaluate(solutions[i], null);
            if(_bestEverFitness == null || _evaluator.compare(solutionQuality, _bestEverFitness) > 0)
            {
                _bestEverFitness = solutionQuality.clone();
                _bestEverSubject = solutions[i].clone();
                newBest = true;
            }
        }
        if(newBest)
        {
            logInfo(agentName + " -> " + _bestEverFitness[0]);

            DataNode newNode = new DataNode("Solution");
            newNode.putValue("agentName", agentName);
            newNode.putValue("fitness", _bestEverFitness[0]);
            String data = "";
            for(int i=0;i<_bestEverSubject.length;i++)
                data += _bestEverSubject[i].toString() + " ";
            newNode.putValue("solution", data);
            _reportLog.putDataNode(newNode);
            XmlHelper.writeXml(_reportLog, _strOutDir+"/Problem.xml");
        }
    }

    public class BreakRequestInitiatorTask extends RequestInitiatorTask
    {

        //private ArrayList<Address> _algorithms;
        public BreakRequestInitiatorTask(ConversationUnit owner, Address participant, Object content, boolean start)
        {
            super(owner, participant, content, start);
            logFinest(participant.getName() + " init request");
            //_algorithms = new ArrayList<Address>();
        }

        @Override
        protected void informDone()
        {
            logFinest(participant.getName() +" INITed successfully");
            //if(_algorithms.contains(participant))
            //    _algorithms.remove(participant);
            _breakDone++;
        }

        @Override
        protected void informResult(Object result)
        {
            logFinest(participant.getName() +" INITed successfully");
        }

        @Override
        protected void failure(Object result, String reason)
        {
            logFinest(participant.getName() + " failure");
            setStateInfo(stSENDINGBREAK, StateInfo.Failure);
        }

        @Override
        protected void notUnderstood(Message msg)
        {
            logFinest(participant.getName() + " notUnderstood");
        }

        @Override
        protected void requestAgreed()
        {
            logFinest(participant.getName() + " requestAgreed");
            _breakAccepted++;
        }

        @Override
        protected void requestRefused()
        {
            logFinest(participant.getName() + " requestRefused");
        }

        @Override
        protected void timeout()
        {
//                if(_agentStates.get(participant) == InitState.Sending)
//                {
//                    _agentStates.put(participant, InitState.Failure);
//                    logFinest(participant.getName() + " timeout");
//                }
            //setStateInfo(stSENDINGBREAK, StateInfo.Failure);
        }
    }

    private class ProblemQueryRefParticipantTask extends QueryRefParticipantTask
    {
        public ProblemQueryRefParticipantTask(CMAgent owner, Message queryMessage)
        {
            super(owner, queryMessage, true);
        }

        @Override
        protected void processQuery(Message queryMessage)
        {
            try
            {
                if(queryMessage.getContent() instanceof Integer)
                {
                    agree();
                    Object[][] population = _problemProvider.generateInitialSolutions((Integer)queryMessage.getContent(), null);
                    handleBestSolution(getName(), population);
                    informResult(population);
                    return;
                }
                refuse();
            }
            catch(Exception ex)
            {
                logWarning(exception2String(ex));
            }
        }

    }

    private enum InitState {Ready, Sending, Done, Failure};
    private class InitSender
    {

        private  HashMap<Address, InitState> _agentStates;
        private  HashMap<Address, DataNode> _agentParams;
        private  HashMap<Address, Integer> _agentFailures;


        public void prepare(String configPath, HashMap<Address, DataNode> agentParams) throws Exception
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
            String strTargetDir = _outputPath +"/"+formatter.format(new Date())+"/";
            _strOutDir = strTargetDir;
            new File(strTargetDir).mkdirs();

            initLogger(strTargetDir);
            
            _agentStates = new HashMap<Address, InitState>();
            _agentParams = agentParams;
            _agentFailures = new HashMap<Address, Integer>();

            for(Entry<Address, DataNode> e  : _agentParams.entrySet())
            {
                e.getValue().putValue("outDir", strTargetDir);
                _agentStates.put(e.getKey(), InitState.Ready);
                _agentFailures.put(e.getKey(), 0);
            }
        }

        /**
         * Sends init requests to all agents - resends if previous failured
         * @param agent
         * @return true if all INIT succesfully accepted
         * @throws java.lang.Exception
         */
        public boolean send(ConversationUnit agent) throws Exception
        {
            boolean result = true;
            boolean failure = false;

            for(Entry<Address, InitState> e : _agentStates.entrySet())
            {
                if(e.getValue() == InitState.Failure)
                {
                    int numFail = _agentFailures.get(e.getKey());
                    if(numFail++ > 10) failure = true;
                    _agentFailures.put(e.getKey(), numFail);
                }

                if(e.getValue() == InitState.Ready || e.getValue() == InitState.Failure)
                {                    
                    new InitRequestInitiatorTask(agent, e.getKey(), _agentParams.get( e.getKey()), true);
                    _agentStates.put(e.getKey(), InitState.Sending);
                }
//                if(e.getValue() != InitState.Done)
//                {
//                    result = false;
//                    //logInfo(e.getKey().getName() + " is not ready yet.");
//                }
                
            }

            if(failure) throw new Exception("Unable to initialize all agents");
            
            return result;
        }

        public  class InitRequestInitiatorTask extends RequestInitiatorTask
        {
            public InitRequestInitiatorTask(ConversationUnit owner, Address participant, Object content, boolean start)
            {
                super(owner, participant, content, start);
                logFinest(participant.getName() + " init request");
            }

            @Override
            protected void informDone()
            {
                _agentStates.put(participant, InitState.Done);
                logFinest(participant.getName() +" INITed successfully");
            }

            @Override
            protected void informResult(Object result)
            {
                _agentStates.put(participant, InitState.Done);
                logFinest(participant.getName() +" INITed successfully");
            }

            @Override
            protected void failure(Object result, String reason)
            {
                _agentStates.put(participant, InitState.Failure);
                logWarning(participant.getName() + " failure: " + reason);
            }

            @Override
            protected void notUnderstood(Message msg)
            {
                _agentStates.put(participant, InitState.Failure);
                logWarning(participant.getName() + " notUnderstood");
            }

            @Override
            protected void requestAgreed()
            {
                logFinest(participant.getName() + " requestAgreed");
            }

            @Override
            protected void requestRefused()
            {
                _agentStates.put(participant, InitState.Failure);
                logFinest(participant.getName() + " requestRefused");
            }

            @Override
            protected void timeout()
            {
                if(_agentStates.get(participant) == InitState.Sending)
                {
                    _agentStates.put(participant, InitState.Failure);
                    logWarning(participant.getName() + " timeout");
                }
            }
        }
    }
}


