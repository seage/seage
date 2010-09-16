package org.seage.ael.agent;

import aglobe.container.transport.Address;
import aglobe.ontology.Message;
import aglobe.ontology.MessageConstants;
import aglobe.service.directory.DirectoryRecord;
import aglobex.protocol.cnp.CNPInitiatorTask;
import aglobex.protocol.queryref.QueryRefInitiatorTask;
import org.seage.aal.AlgorithmReport;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.reasoning.algparams.Policy;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * AlgorithmAgent class
 *
 * <report>
 *      <algorithmParams/>
 *      <runStatistics/>
 * </report>
 *
 * @author rick
 */
public class AlgorithmAgent extends AelAgent
{    
    protected IAlgorithmAdapter _algorithm;
    protected Object[][] _solutions;
    protected DataNode _algorithmParams;
    protected Policy _policy;
    
    private ArrayList<Address> _solutionPoolAddresses; 
    private ArrayList<Address> _adviserAddresses;
    
    private Address _problemAgentAddress;
    protected String _problemName;
    protected String _adviserName;
    
    // report on last algorithm run
    private AlgorithmReport _report;
    // report on all algorithm runs
    private DataNode _reportLog;

    private BreakRequestParticipantTask _breakTask;

    private final int stINITAGENT = 1;              // "Initialization"
    private final int stQUERYPROBLEMAGENT = 2;      // "Querying ProblemAgent"
    private final int stINITALGORITHM = 3;          // "Initialization"
    private final int stADVISERNEGOTIATION = 4;     // "Adviser Negotiation"
    private final int stASKINGADVISER = 5;          // "Asking Advisers for Policy"
    private final int stLOADSOLUTION = 6;           // "Loading Solutions"
    private final int stSEARCHING = 7;              // "Searching by Algorithm"
    private final int stBREAK = 8;              // "Searching by Algorithm"
    private final int stSTORESOLUTION = 9;          // "Storing Solutions"
    private final int stDONE = 10;                   // "Everything done"

    private final String[] stNames
            = new String[] {"Unknown", "Initialization", "Querying ProblemAgent", "Algorithm Initialization", "Adviser Negotiation",
                            "Asking Advisers for Policy", "Loading Solutions", "Searching by Algorithm", "Algorithm Break",
                            "Storing Solutions", "Everything done"};

//    protected abstract void queryProblemData();
//    protected abstract void touchProblemData(HashMap<String, Object> data);
//    protected abstract void initAlgorithm();
    
    @Override
    public void init(DataNode params) throws Exception
    {
        //if(getStateEx() != stDONE && getStateEx() != stBREAK)
        //    return;
        // TODO: C - Add DataNode validation against the XSD schema
        logInfo("INIT");
        setState(stINITAGENT);
        setStateInfo(stINITAGENT, StateInfo.InProgress);

         // extract parameters
        DataNode algParams = params.getDataNode("algorithm").getDataNodes().get(0);
        _algorithmParams = new DataNode("parameters");
        for(String p : algParams.getValueNames())
            _algorithmParams.putValue(p, algParams.getValue(p));

        DataNode factoryParams = params.getDataNode("algorithm");
        factoryParams.putValue("agentName", getName());
        IAlgorithmFactory factory = (IAlgorithmFactory)Class.forName(factoryParams.getValueStr("factoryClass")).newInstance();
        _algorithm = factory.createAlgorithm();

       

        // ask for Problem and SolutionPool services
        _solutionPoolAddresses = new ArrayList<Address>();
        _directoryShell.subscribe(this, "Problem|SolutionPool");

        // ask for Adviser service
        _adviserName = params.getDataNode("algorithm").getValueStr("adviserID");
        _adviserAddresses = new ArrayList<Address>();
        _directoryShell.subscribe(this, _adviserName);
        
        _reportLog = new DataNode("algorithmRuns");
        
        _report = new AlgorithmReport("report");
        _report.putDataNode(_algorithmParams);

        setStateInfo(stINITAGENT, StateInfo.Success);

        logInfo("AlgorithmAgent " + this.getName() + " initialized with address "
            + this.getAddress().toString());

    }

    @Override
    public void done() throws Exception
    {
        setState(stDONE);

        if(_reportLog != null)
            XmlHelper.writeXml(_reportLog, _strOutDir+getName()+".xml");
        if(_algorithm != null)
            _algorithm.stopSearching();       
    }

    @Override
    void breakAlgorithm(BreakRequestParticipantTask breakTask) throws Exception
    {
        setState(stBREAK);
        _breakTask = breakTask;
        _breakTask.sendAgree();
        
        if(_algorithm != null)
            _algorithm.stopSearching();

    }

    @Override
    protected void processIncomingMessage(Message m)
    {
        logFine(m.toString());
    }

    @Override
    protected void processCurrentState() throws Exception
    {        
        switch(getStateEx())
        {
            case stINITAGENT:
                if(currentStateInfo ( StateInfo.Success))
                    setState(stADVISERNEGOTIATION);
                return;
            case stADVISERNEGOTIATION:
                if(currentStateInfo ( StateInfo.Ready))
                    setStateInfo(stADVISERNEGOTIATION, StateInfo.InProgress);
                if(_adviserAddresses != null && _adviserAddresses.size() != 0)
                    setState(stASKINGADVISER);
                return;
            case stASKINGADVISER:
                if(currentStateInfo ( StateInfo.Ready))
                {
                    logInfo(getName() + " - " + stNames[getStateEx()]);
                    setStateInfo(stASKINGADVISER, StateInfo.InProgress);
                    new AdviserQueryRefInitiatorTask(this, _adviserAddresses.get(0), _report);
                }
                if(currentStateInfo ( StateInfo.Success))
                    setState(stLOADSOLUTION);
                if(currentStateInfo ( StateInfo.Failure))
                    setState(stASKINGADVISER);
                return;
            case stLOADSOLUTION:
                if(currentStateInfo ( StateInfo.Ready))
                    if(_solutionPoolAddresses != null && _solutionPoolAddresses.size() > 0)
                    {
                        logInfo(getName() + " - " + stNames[getStateEx()]);
                        setStateInfo(stLOADSOLUTION, StateInfo.InProgress);
                        new LoadSolutionCNPInitiatorTask(this, _solutionPoolAddresses, _algorithmParams.getValueInt("numSolutions"));
                    }

                if(currentStateInfo ( StateInfo.Success))
                    setState(stSEARCHING);
                if(currentStateInfo ( StateInfo.Failure))
                    setState(stLOADSOLUTION);
                return;
            case stSEARCHING:
                if(currentStateInfo ( StateInfo.Ready))
                {
                    logInfo(getName() + " - " + stNames[getStateEx()]);
                    setStateInfo(stSEARCHING, StateInfo.InProgress);

                    _algorithmParams = _policy.suggest(_report);
                    try
                    {
                        do
                        {
                            logInfo("algorithm run");
                            _algorithm.solutionsFromPhenotype(_solutions);
                            _algorithm.startSearching(_algorithmParams);
                            // TODO: A - Handle INIT
                            _report = _algorithm.getReport();
                            _report.setId(_policy.getID());
                            _reportLog.putDataNode(_report);
                            _solutions = _algorithm.solutionsToPhenotype();
                            if(_solutions == null)
                                throw new Exception("_solutions is null");

                            sendBestSolutionToProblemAgent(_solutions[0]);
                            //assert (_solutions != null);
                            _algorithmParams = _policy.suggest(_report);
                            
                        }
                        while(_algorithmParams.getValueBool("continue") && getStateEx() == stSEARCHING);
                    }
                    catch(Exception ex)
                    {
                        logWarning(stNames[getStateEx()]+"\n"+ exception2String(ex));
                        
                    }
                    finally
                    {
                        if(getStateEx() == stSEARCHING)
                            setState(stSTORESOLUTION);
                    }                    
                }
                return;
            case stBREAK:
//                if(currentStateInfo ( StateInfo.Ready))
//                    if(_solutionPoolAddresses != null && _solutions != null)
//                    {
//                        logInfo(getName() + " - " + stNames[getStateEx()]);
//                        setStateInfo(stBREAK, StateInfo.InProgress);
//                        new StoreSolutionCNPInitiatorTask(this, _solutionPoolAddresses, _solutions, stBREAK);
//                    }
//                    else
//                        setStateInfo(stBREAK, StateInfo.Success);
                if(currentStateInfo (StateInfo.Ready))
                {
                    setState(stDONE);
                    _solutions = null;
                    _breakTask.sendDone();
                    
                    return;
                }
//                if(currentStateInfo (StateInfo.Failure))
//                {
//                    setState(stBREAK);
//                    //_breakTask.sendFailure();
//
//                    return;
//                }
//                return;
            case stSTORESOLUTION:
                if(currentStateInfo ( StateInfo.Ready))
                    if(_solutionPoolAddresses != null && _solutions != null)
                    {
                        logInfo(getName() + " - " + stNames[getStateEx()]);
                        setStateInfo(stSTORESOLUTION, StateInfo.InProgress);
                        new StoreSolutionCNPInitiatorTask(this, _solutionPoolAddresses, _solutions, stSTORESOLUTION);
                    }
                if(currentStateInfo ( StateInfo.Success))
                {
                    _solutions = null;
                    setState(stASKINGADVISER);
                }
                if(currentStateInfo ( StateInfo.Failure))
                    setState(stSTORESOLUTION);
                return;
            case stDONE:
                if(currentStateInfo ( StateInfo.Ready))
                {                    
                    setStateInfo(stDONE, StateInfo.InProgress);
                }
                else
                    Thread.currentThread().sleep(5000);
                return;
        }
    }

    @Override
    protected String getStateName(int stateNum) {
        return stNames[stateNum];
    }

    // DirectoryListener methods
        
    @Override
    public void handleNewRegister(String containerName, DirectoryRecord[] records, String matchingFilter)
    {
        //System.out.println(getName() + "> Ladies and gentleman, we've got him");
        for(DirectoryRecord dr : records)
        {
            for(String service : dr.getServices())
            {
                if(service.equals("Problem") && _problemAgentAddress == null)
                {
                    _problemAgentAddress = dr.address;
                    logFine("Problem agent new registration - " + _problemAgentAddress.toString());
                }
                if(service.equals("SolutionPool"))
                {
                    if(!_solutionPoolAddresses.contains(dr.address))
                    {
                        _solutionPoolAddresses.add(dr.address);
                        logFine("SolutionPool agent new registration - " + _solutionPoolAddresses.toString());
                    }
                }
                if(service.equals(_adviserName))
                {
                    if(!_adviserAddresses.contains(dr.address))
                    {
                        _adviserAddresses.add(dr.address);
                        logFine("Adviser agent new registration - " + _adviserAddresses.toString());
                    }
                }
                        //System.out.println("\t"+records[i].address);
            }            
        }
    }
    
    @Override
    public void handleDeregister(String containerName, DirectoryRecord[] records, String matchingFilter)
    {
        for(DirectoryRecord dr : records)
        {
            if(dr.address ==  _problemAgentAddress)
                _problemAgentAddress = null;            

            if(_solutionPoolAddresses.contains(dr.address))
                _solutionPoolAddresses.remove(dr.address);
            
            if(_adviserAddresses.contains(dr.address))
                _adviserAddresses.remove(dr.address);
        }
    }    

    private void sendBestSolutionToProblemAgent(Object[] solution) throws Exception
    {
        logInfo("sendBestSolutionToProblemAgent");
        Message m = Message.newInstance(MessageConstants.INFORM, getAddress(), _problemAgentAddress);
        m.setContent(new Object[][] {solution});
        sendMessage(m);
    }

    private class AdviserQueryRefInitiatorTask extends QueryRefInitiatorTask
    {

        public AdviserQueryRefInitiatorTask(AlgorithmAgent owner, Address participant, DataNode report)
        {
            super(owner, participant, report, true);
        }

        @Override
        protected void informResult(Object result)
        {
            try
            {
                _policy = (Policy)result;
                logFinest(XmlHelper.getStringFromDocument(_algorithmParams.toXml()));
                setStateInfo(stASKINGADVISER, StateInfo.Success);
            }
            catch(Exception ex)
            {
                setStateInfo(stASKINGADVISER, StateInfo.Failure);
                logWarning(ex.toString());
            }
        }
        
        @Override
        protected void queryRefused()
        {
            logWarning("queryRefused");
            setStateInfo(stASKINGADVISER, StateInfo.Failure);
        }

        @Override
        protected void timeout() {
            if(getStateEx() == stASKINGADVISER)
            {
                logWarning("timeout");
                setStateInfo(stASKINGADVISER, StateInfo.Failure);
            }
        }
    
    }

    private class LoadSolutionCNPInitiatorTask extends CNPInitiatorTask
    {
        private int _amount;

        public LoadSolutionCNPInitiatorTask(AlgorithmAgent agent, Collection<Address> participants, int amount)
        {
            super(agent, participants, "<get amount='"+amount+"'/>", 1000, "", true);
            _amount = amount;
            logFine("LoadSolutionCNPInitiatorTask sent request for " + amount + " solutions");
        }

        @Override
        protected List<Address> evaluateReplies()
        {
            List<Address> replies = new ArrayList<Address>();
            try
            {
                for(Address a : participants)
                {
                    if(receivedOffers.get(a).getPerformative().equals("REFUSE"))
                        continue;
                    if((Integer)receivedOffers.get(a).getContent() >= _amount)
                        replies.add(a);
                    logFine("LoadSolutionCNPInitiatorTask recieved offer for " + (Integer)receivedOffers.get(a).getContent() + " solutions");
                }
                if(replies.size() == 0)
                {
                    setStateInfo(stLOADSOLUTION, StateInfo.Failure);
                    logWarning("Reply with offer 0 recieved, task canceled.");
                    //cancelTask();
                }
            }
            catch(Exception ex)
            {
                logSevere(ex.toString());
            }
            return replies;
        }
        
        @Override
        protected boolean evaluateAcceptReply(Message m)
        {
            _solutions = (Object[][])m.getContent();
            setStateInfo(stLOADSOLUTION, StateInfo.Success);

            logFine("LoadSolutionCNPInitiatorTask recieved " + _solutions.length + " solutions");
            return false;
        }

        @Override
        protected void evaluateAcceptTimeout()
        {
            setStateInfo(stLOADSOLUTION, StateInfo.Failure);
            logWarning("evaluateAcceptTimeout ");
        }

        @Override
        protected void timeout() {
            logWarning("timeout ");
            setStateInfo(stLOADSOLUTION, StateInfo.Failure);
        }
    }

    private class StoreSolutionCNPInitiatorTask extends CNPInitiatorTask
    {
        AlgorithmAgent _agent;
        Object[][] _solutions;
        int _currentTask;
        public StoreSolutionCNPInitiatorTask(AlgorithmAgent agent, Collection<Address> participants, Object[][] solutions, int currentTask)
        {
            super(agent, participants, "<put amount='"+solutions.length+"'/>", 2000, "", true);
            _agent = agent;
            _solutions = solutions;
            _currentTask = currentTask;
            logFine("StoreSolutionCNPInitiatorTask sent request for " + solutions.length + " solutions");
        }

        @Override
        protected List<Address> evaluateReplies()
        {
            List<Address> replies = new ArrayList<Address>();
            for(Address a : participants)
            {
                replies.add(a);
            }
            logFine("Reply to " + replies.size() + " participants");
            return replies;
        }

        // TODO: A - check CFP for storing
        @Override
        protected void sendAnswersToParticipants(Map<Address, Object> arg0)
        {
            Map<Address, Object> acceptedParticipants = new HashMap<Address, Object>();
            for(Address a : arg0.keySet())
            {
                //arg0.remove(a);
                //logFinest(_solutions.toString());
                acceptedParticipants.put(a, _solutions);
            }
            
            super.sendAnswersToParticipants(acceptedParticipants);
        }

        @Override
        protected boolean evaluateAcceptReply(Message m)
        {

            setStateInfo(_currentTask, StateInfo.Success);
            return false;
        }

        @Override
        protected void evaluateAcceptTimeout()
        {
            setStateInfo(_currentTask, StateInfo.Failure);
            logWarning("evaluateAcceptTimeout ");
        }
    }
}
