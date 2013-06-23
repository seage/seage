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
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;

public class SingleAlgorithmEvolutionExperimenter extends Experimenter implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject>>
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
        	ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject> realOperator = new ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject>(limits);
        	
            for (String instanceID : instanceIDs)
            {
            	_logger.info(instanceID);
            	GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject> ga = new GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject>(realOperator, new SingleAlgorithmExperimentTaskEvaluator());
            	ga.addGeneticSearchListener(this);
        		ga.setCrossLengthPct(30);
        		ga.setEliteSubjectsPct(20);
        		ga.setIterationToGo((int) timeoutS);
        		ga.setMutateChromosomeLengthPct(10);
        		ga.setMutatePopulationPct(1);
        		ga.setPopulationCount(numOfConfigs);
        		ga.setRandomSubjectsPct(1);
        		
        		List<SingleAlgorithmExperimentTaskSubject> subjects = initializeSubjects(algorithmID, pi, numOfConfigs);        		
        		
        		ga.startSearching(subjects);
        		_logger.info("   " +ga.getBestSubject().toString());
            }
        }
//		------------
		
		_logger.info("-------------------------------------");
        _logger.log(Level.INFO, "Experiment " + experimentID + " finished ...");
	}

	private List<SingleAlgorithmExperimentTaskSubject> initializeSubjects(String algorithmID, ProblemInfo pi, int count) throws Exception 
	{
		List<SingleAlgorithmExperimentTaskSubject> result = new ArrayList<SingleAlgorithmExperimentTaskSubject>();
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
			result.add(new SingleAlgorithmExperimentTaskSubject(values));
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

	@Override
	public void algorithmStarted(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		_logger.info("   Started");
		
	}

	@Override
	public void algorithmStopped(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		_logger.info("   Stopped");
		
	}

	@Override
	public void newBestSolutionFound(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iterationPerformed(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		_logger.info("      Iteration "+e.getGeneticSearch().getCurrentIteration());		
	}

	@Override
	public void noChangeInValueIterationMade(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		// TODO Auto-generated method stub
		
	}
}
