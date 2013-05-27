package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.experimenter.IExperimenter;
import org.seage.metaheuristic.genetics.GeneticSearch;
import org.seage.metaheuristic.genetics.RealGeneticOperator;

public class SingleAlgorithmEvolutionExperimenter implements IExperimenter
{
	private IAlgorithmAdapter _geneticAlgorithm;

	public SingleAlgorithmEvolutionExperimenter()
	{
		_geneticAlgorithm = new GeneticAlgorithmAdapter<Double>(new ExperimentGeneticOperator(null), new ExperimentEvaluator(), false, "");
	}
	
	@Override
	public void runFromConfigFile(String configPath) throws Exception
	{
		
	}

	@Override
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID) throws Exception
	{
		
	}

	@Override
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs) throws Exception
	{
		
	}

	@Override
	public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception
	{
		
		DataNode p = new DataNode("Parameters");
		
		p.putValue("crossLengthPct", 30);
		p.putValue("mutateLengthPct", 20);
		p.putValue("eliteSubjectPct", 5);
		p.putValue("iterationCount", 10);
		p.putValue("mutateSubjectPct", 10);
		p.putValue("numSolutions", 10);
		p.putValue("randomSubjectPct", 1);
		
		AlgorithmParams params = new AlgorithmParams("Parameters");
		params.putValue("problemID", "Experiment");
		params.putValue("instance", "run01");
		params.putDataNodeRef(p);
		
		List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
//        configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, instanceInfo, numOfConfigs))); 
//		
//		_geneticAlgorithm.solutionsFromPhenotype(configs);
//		_geneticAlgorithm.startSearching(params);
//		dest = _geneticAlgorithm.solutionsToPhenotype();
//		------------
		int dim = 10;
		RealGeneticOperator.Limit[] limits = new RealGeneticOperator.Limit[dim];
		RealGeneticOperator operator = new RealGeneticOperator(limits);

		for (int i = 0; i < dim; i++)
			limits[i] = operator.new Limit(-10, 10);

		GeneticSearch<Double> gs = new GeneticSearch<Double>(operator, new ExperimentEvaluator());
		gs.setCrossLengthPct(0.3);
		gs.setEliteSubjectsPct(0.2);
		gs.setIterationToGo(200000);
		gs.setMutateChromosomeLengthPct(0.1);
		gs.setMutatePopulationPct(0.0);			
		gs.setPopulationCount(100);
		gs.setRandomSubjectsPct(0.01);
	}

	

}
