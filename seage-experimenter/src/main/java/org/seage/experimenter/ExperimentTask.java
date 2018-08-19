package org.seage.experimenter;

import java.net.UnknownHostException;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs experiment task and provides following experiment log:
 * 
 * ExperimentTask 			    # version 0.1 
 * |_ ... 
 * 
 * ExperimentTaskReport 		# version 0.2
 *  |_ version (0.4)
 *  |_ experimentID
 *  |_ startTimeMS
 *  |_ timeoutS
 *  |_ durationS
 *  |_ machineName
 *  |_ nrOfCores
 *  |_ totalRAM
 *  |_ availRAM 
 *  |_ Config
 *  |	|_ configID
 *  |	|_ runID
 *  |	|_ Problem
 *  |	|	|_ problemID
 *  |	|	|_ Instance
 *  |	|		|_ name
 *  |	|_ Algorithm
 *  |		|_ algorithmID
 *  |		|_ Parameters
 *  |_ AlgorithmReport
 *  	|_ Parameters
 *  	|_ Statistics
 *  	|_ Minutes
 * 
 * @author Richard Malek
 */
public class ExperimentTask implements Runnable
{
    protected static Logger _logger = LoggerFactory.getLogger(ExperimentTask.class.getName());
    //protected ProblemConfig _config;
    protected String _experimentType;
    protected String _experimentID;
    protected String _problemID;
    protected String _instanceID;
    protected String _algorithmID;
    protected String _configID;
    protected AlgorithmParams _algorithmParams;
    protected long _runID;
    protected long _timeoutS;

    protected DataNode _experimentTaskReport;

    public ExperimentTask(String experimentType, String experimentID, String problemID, String instanceID,
            String algorithmID, AlgorithmParams algorithmParams, int runID, long timeoutS) throws Exception
    {
        _experimentType = experimentType;
        _experimentID = experimentID;
        _problemID = problemID;
        _instanceID = instanceID;
        _algorithmID = algorithmID;
        _algorithmParams = algorithmParams;
        _configID = algorithmParams.hash();
        _runID = runID;
        _timeoutS = timeoutS;

        //_reportName = reportName;
        //_reportOutputStream = reportOutputStream;

        _experimentTaskReport = new DataNode("ExperimentTaskReport");
        _experimentTaskReport.putValue("version", "0.7");
        _experimentTaskReport.putValue("experimentType", experimentType);
        _experimentTaskReport.putValue("experimentID", experimentID);
        _experimentTaskReport.putValue("timeoutS", timeoutS);

        try
        {
            _experimentTaskReport.putValue("machineName", java.net.InetAddress.getLocalHost().getHostName());
        }
        catch (UnknownHostException e)
        {
            _logger.warn( e.getMessage());
        }
        _experimentTaskReport.putValue("nrOfCores", Runtime.getRuntime().availableProcessors());
        _experimentTaskReport.putValue("totalRAM", Runtime.getRuntime().totalMemory());
        _experimentTaskReport.putValue("availRAM", Runtime.getRuntime().maxMemory());

        DataNode configNode = new DataNode("Config");
        configNode.putValue("configID", _configID);
        configNode.putValue("runID", _runID);

        DataNode problemNode = new DataNode("Problem");
        problemNode.putValue("problemID", _problemID);

        DataNode instanceNode = new DataNode("Instance");
        instanceNode.putValue("name", _instanceID);

        DataNode algorithmNode = new DataNode("Algorithm");
        algorithmNode.putValue("algorithmID", _algorithmID);
        algorithmNode.putDataNode(_algorithmParams);

        problemNode.putDataNode(instanceNode);
        configNode.putDataNode(problemNode);
        configNode.putDataNode(algorithmNode);

        _experimentTaskReport.putDataNode(configNode);
        
        DataNode solutionsNode = new DataNode("Solutions");
        solutionsNode.putDataNode(new DataNode("Input"));
        solutionsNode.putDataNode(new DataNode("Output"));
        _experimentTaskReport.putDataNode(solutionsNode);

    }

    public String getReportName() throws Exception
    {
        return _configID + "-" + _runID + ".xml";
    }

    public String getConfigID()
    {
        return _configID;
    }

    public DataNode getExperimentTaskReport()
    {
        return _experimentTaskReport;
    }
    
	@Override
    public void run()
    {
        try
        {
            // provider and factory
            IProblemProvider<Phenotype<?>> provider = ProblemProvider.getProblemProviders().get(_problemID);
            IAlgorithmFactory<Phenotype<?>, ?> factory = provider.getAlgorithmFactory(_algorithmID);

            // problem instance
            ProblemInstance instance = provider
                    .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(_instanceID));

            IPhenotypeEvaluator<Phenotype<?>> evaluator = provider.initPhenotypeEvaluator(instance);
            
            // algorithm
            IAlgorithmAdapter<Phenotype<?>, ?> algorithm = factory.createAlgorithm(instance, evaluator);

            Phenotype<?>[] solutions = provider.generateInitialSolutions(instance,
            		_algorithmParams.getValueInt("numSolutions"), _experimentID.hashCode());
            writeSolutions(evaluator, _experimentTaskReport.getDataNode("Solutions").getDataNode("Input"), solutions);
            
            long startTime = System.currentTimeMillis();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(_algorithmParams, true);
            waitForTimeout(algorithm);
            algorithm.stopSearching();
            long endTime = System.currentTimeMillis();

            solutions = algorithm.solutionsToPhenotype();
            writeSolutions(evaluator, _experimentTaskReport.getDataNode("Solutions").getDataNode("Output"), solutions);

            _experimentTaskReport.putDataNode(algorithm.getReport());
            _experimentTaskReport.putValue("durationS", (endTime - startTime) / 1000);

            //XmlHelper.writeXml(_experimentTaskReport, _reportOutputStream, getReportName());

        }
        catch (Exception ex)
        {
            _logger.error( ex.getMessage(), ex);
            _logger.error( _algorithmParams.toString());

        }
    }

    private void waitForTimeout(IAlgorithmAdapter<?, ?> alg) throws Exception
    {
        long time = System.currentTimeMillis();
        while (alg.isRunning() && ((System.currentTimeMillis() - time) < _timeoutS * 1000))
            Thread.sleep(100);
    }
    
    private void writeSolutions(IPhenotypeEvaluator<Phenotype<?>> evaluator, DataNode dataNode, Phenotype<?>[] solutions) {    	
    	for(Phenotype<?> p : solutions) {
    		try {
	    		DataNode solutionNode = new DataNode("Solution");
	    		solutionNode.putValue("objValue", evaluator.evaluate(p)[0]);
	    		solutionNode.putValue("solution", p.toText());
    		
				dataNode.putDataNode(solutionNode);
			} catch (Exception ex) {
				_logger.error("Cannot write solution", ex);
			}
    	}
    }

}
