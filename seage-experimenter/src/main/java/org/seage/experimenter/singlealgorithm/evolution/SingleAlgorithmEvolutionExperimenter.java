package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.Experimenter;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator.Limit;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;

public class SingleAlgorithmEvolutionExperimenter extends Experimenter implements IAlgorithmListener<GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject>>
{
	public SingleAlgorithmEvolutionExperimenter()
	{
		super("SingleAlgorithmEvolution");
	}

	@Override
	protected void runExperimentTasks(long experimentID, ProblemInfo problemInfo, InstanceInfo instanceInfo, String[] algorithmIDs, int numConfigs, long timeoutS, ZipOutputStream zos) throws Exception
	{
		String problemID = problemInfo.getProblemID();
		String instanceID = instanceInfo.getInstanceID();
		
		for (String algorithmID : algorithmIDs)
		{
			ContinuousGeneticOperator.Limit[] limits = prepareAlgorithmParametersLimits(algorithmID, problemInfo);
			ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject> realOperator = new ContinuousGeneticOperator<SingleAlgorithmExperimentTaskSubject>(limits);

			SingleAlgorithmExperimentTaskEvaluator evaluator = new SingleAlgorithmExperimentTaskEvaluator(experimentID, problemID, instanceID, algorithmID, timeoutS, zos);
			GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject> ga = new GeneticAlgorithm<SingleAlgorithmExperimentTaskSubject>(realOperator, evaluator);
			ga.addGeneticSearchListener(this);
			ga.setCrossLengthPct(30);
			ga.setEliteSubjectsPct(20);
			ga.setIterationToGo((int) timeoutS);
			ga.setMutateChromosomeLengthPct(10);
			ga.setMutatePopulationPct(1);
			ga.setPopulationCount(numConfigs);
			ga.setRandomSubjectsPct(1);

			List<SingleAlgorithmExperimentTaskSubject> subjects = initializeSubjects(algorithmID, problemInfo, numConfigs);

			ga.startSearching(subjects);
			_logger.info("   " + ga.getBestSubject().toString());

		}
	}

	private List<SingleAlgorithmExperimentTaskSubject> initializeSubjects(String algorithmID, ProblemInfo pi, int count) throws Exception
	{
		List<SingleAlgorithmExperimentTaskSubject> result = new ArrayList<SingleAlgorithmExperimentTaskSubject>();
		List<DataNode> params = pi.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");

		for (int i = 0; i < count; i++)
		{
			Double[] values = new Double[params.size()];
			for (int j = 0; j < params.size(); j++)
			{
				double min = params.get(j).getValueDouble("min");
				double max = params.get(j).getValueDouble("max");
				values[j] = min + (max - min) * Math.random();
			}
			result.add(new SingleAlgorithmExperimentTaskSubject(values));
		}

		return result;
	}

	protected Limit[] prepareAlgorithmParametersLimits(String algorithmID, ProblemInfo pi) throws Exception
	{
		List<DataNode> params = pi.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter");
		Limit[] result = new Limit[params.size()];

		int i = 0;
		for (DataNode paramNode : params)
		{
			double min = paramNode.getValueDouble("min");
			double max = paramNode.getValueDouble("max");

			result[i++] = new ContinuousGeneticOperator.Limit(min, max);
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
		_logger.info("      Iteration " + e.getGeneticSearch().getCurrentIteration());
	}

	@Override
	public void noChangeInValueIterationMade(GeneticAlgorithmEvent<SingleAlgorithmExperimentTaskSubject> e)
	{
		// TODO Auto-generated method stub

	}
}
