package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.experimenter.ExperimentTask;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.thread.TaskRunner3;

public class SingleAlgorithmExperimentTaskEvaluator extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject>
{
    private String _experimentID;
    private String _problemID;
    private String _instanceID;
    private String _algorithmID;
    private long _timeoutS;
    private ZipOutputStream _reportOutputStream;
    //private int iterationID = 1;
    private int evaluationID = 1;

    private HashMap<String, Double> _configCache;

    public SingleAlgorithmExperimentTaskEvaluator(String experimentID, String problemID, String instanceID,
            String algorithmID, long timeoutS)
    {
        super();
        _experimentID = experimentID;
        _problemID = problemID;
        _instanceID = instanceID;
        _algorithmID = algorithmID;
        _timeoutS = timeoutS;
        _reportOutputStream = reportOutputStream;
        _configCache = new HashMap<String, Double>();
    }

    @Override
    public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception
    {
        List<ExperimentTask> taskQueue = new ArrayList<ExperimentTask>();
        HashMap<ExperimentTask, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<ExperimentTask, SingleAlgorithmExperimentTaskSubject>();

        for (SingleAlgorithmExperimentTaskSubject s : subjects)
        {
            AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
            for (int i = 0; i < s.getChromosome().getLength(); i++)
            {
                algorithmParams.putValue(s.getParamNames()[i], s.getChromosome().getGene(i));
            }

            String configID = algorithmParams.hash();
            if (_configCache.containsKey(configID))
            {
                s.setObjectiveValue(new double[] { _configCache.get(configID) });
            }
            else
            {
            	ExperimentTask task = new ExperimentTask(new ExperimentSetup("SingleAlgorithmEvolution",
                        _experimentID, _problemID, _instanceID, _algorithmID, algorithmParams,
                        _timeoutS));

                taskMap.put(task, s);
                taskQueue.add(task);
            }
        }

        TaskRunner3.run(taskQueue.toArray(new Runnable[] {}), Runtime.getRuntime().availableProcessors());

        for (ExperimentTask task : taskQueue)
        {
            SingleAlgorithmExperimentTaskSubject s = taskMap.get(task);
            double value = Double.MAX_VALUE;
            try
            {
                value = task.getExperimentTaskReport().getDataNode("AlgorithmReport").getDataNode("Statistics")
                        .getValueDouble("bestObjVal");
            }
            catch (Exception ex)
            {
                _logger.warn("Unable to set value");
            }
            s.setObjectiveValue(new double[] { value });

            _configCache.put(task.getConfigID(), value);
        }

    }

    @Override
    protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception
    {
        throw new Exception("Should be unimplemented");
    }

}
