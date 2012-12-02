package org.seage.experimenter.singlealgorithm.evolution;

import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.data.AlgorithmParams;
import org.seage.experimenter.IExperimenter;

public class SingleAlgorithmEvolutionExperimenter implements IExperimenter
{
	private IAlgorithmAdapter _geneticAlgorithm;

	public SingleAlgorithmEvolutionExperimenter()
	{
		_geneticAlgorithm = new GeneticAlgorithmAdapter(new ExperimentGeneticOperator(), new ExperimentEvaluator(), false, "");
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
		AlgorithmParams params = new AlgorithmParams("");
		_geneticAlgorithm.startSearching(params);
	}

	

}
