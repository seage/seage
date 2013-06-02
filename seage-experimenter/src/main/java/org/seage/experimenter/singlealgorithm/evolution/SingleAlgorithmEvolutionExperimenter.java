package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;

public class SingleAlgorithmEvolutionExperimenter extends SingleAlgorithmExperimenter
{
	public SingleAlgorithmEvolutionExperimenter() 
	{
		super("SingleAlgorithmEvolutio", null);		
	}

	@Override
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception
	{
		long experimentID = System.currentTimeMillis();
        _logger.info("Experiment "+experimentID+" started ...");
        _logger.info("-------------------------------------");
        
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        for (String algorithmID : algorithmIDs)
        {
        	ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(algorithmID, pi);
        	ContinuousGeneticOperator<ExperimentSubject> realOperator = new ContinuousGeneticOperator<ExperimentSubject>(limits);
        	
            for (String instanceID : instanceIDs)
            {
            	_logger.info(instanceID);
            	GeneticAlgorithm<ExperimentSubject> gs = new GeneticAlgorithm<ExperimentSubject>(realOperator, new ExperimentEvaluator());
        		gs.setCrossLengthPct(0.3);
        		gs.setEliteSubjectsPct(0.2);
        		gs.setIterationToGo(numOfConfigs);
        		gs.setMutateChromosomeLengthPct(0.1);
        		gs.setMutatePopulationPct(0.0);
        		gs.setPopulationCount(5);
        		gs.setRandomSubjectsPct(0.01);
        		
        		List<ExperimentSubject> subjects = initializeSubjects(algorithmID, pi, 10);        		
        		
        		gs.startSearching(subjects);
        		_logger.info(" -" +gs.getBestSubject().getObjectiveValue()[0]);
            }
        }
//		------------
		
		_logger.info("-------------------------------------");
        _logger.log(Level.INFO, "Experiment " + experimentID + " finished ...");
	}

	private List<ExperimentSubject> initializeSubjects(String algorithmID, ProblemInfo pi, int count) throws Exception 
	{
		List<ExperimentSubject> result = new ArrayList<ExperimentSubject>();
		List<DataNode> params = pi.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");
		
		for(int i=0;i<count;i++)
		{
			Double[] values = new Double[params.size()];
			for (int j=0;j<params.size();j++)
	        {            
	            double min = params.get(j).getValueDouble("min");
	            double max = params.get(j).getValueDouble("max");
	            values[j] = min + (max-min)*Math.random();
	        }
			result.add(new ExperimentSubject(values));
		}
		
		return result;
	}

	protected Limit[] prepareAlgorithmParametersLimits(String algorithmID, ProblemInfo pi) throws Exception 
	{
		List<DataNode> params = pi.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");
		Limit[] result = new Limit[params.size()];
		
		int i=0;
		for (DataNode paramNode : params)
        {            
            double min = paramNode.getValueDouble("min");
            double max = paramNode.getValueDouble("max");
            
            result[i++]=new ContinuousGeneticOperator.Limit(min, max);
        }
		
		return result;
	}

}
