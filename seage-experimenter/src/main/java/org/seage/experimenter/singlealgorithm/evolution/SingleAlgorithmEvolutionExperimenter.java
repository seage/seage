package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;

public class SingleAlgorithmEvolutionExperimenter extends Experimenter
{	
	public SingleAlgorithmEvolutionExperimenter() 
	{
		super("SingleAlgorithmEvolution");
	}
	
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception
	{
		long experimentID = System.currentTimeMillis();
        _logger.info("Experiment "+experimentID+" started ...");
        _logger.info("-------------------------------------");
        
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        for (String algorithmID : algorithmIDs)
        {
        	ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(algorithmID, pi);
        	ContinuousGeneticOperator<ExperimentTaskSubject> realOperator = new ContinuousGeneticOperator<ExperimentTaskSubject>(limits);
        	
            for (String instanceID : instanceIDs)
            {
            	_logger.info(instanceID);
            	GeneticAlgorithm<ExperimentTaskSubject> gs = new GeneticAlgorithm<ExperimentTaskSubject>(realOperator, new ExperimentTaskEvaluator());
        		gs.setCrossLengthPct(30);
        		gs.setEliteSubjectsPct(20);
        		gs.setIterationToGo((int) timeoutS);
        		gs.setMutateChromosomeLengthPct(10);
        		gs.setMutatePopulationPct(1);
        		gs.setPopulationCount(numOfConfigs);
        		gs.setRandomSubjectsPct(1);
        		
        		List<ExperimentTaskSubject> subjects = initializeSubjects(algorithmID, pi, numOfConfigs);        		
        		
        		gs.startSearching(subjects);
        		_logger.info("   " +gs.getBestSubject().toString());
            }
        }
//		------------
		
		_logger.info("-------------------------------------");
        _logger.log(Level.INFO, "Experiment " + experimentID + " finished ...");
	}

	private List<ExperimentTaskSubject> initializeSubjects(String algorithmID, ProblemInfo pi, int count) throws Exception 
	{
		List<ExperimentTaskSubject> result = new ArrayList<ExperimentTaskSubject>();
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
			result.add(new ExperimentTaskSubject(values));
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
