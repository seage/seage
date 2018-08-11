package org.seage.experimenter.singlealgorithm.random;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.experimenter.ExperimentTask;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.thread.TaskRunner3;

public class SingleAlgorithmRandomExperimenter extends Experimenter
{
    protected Configurator _configurator;
    private int _numConfigs;
    private int _timeoutS;

    private final int NUM_RUNS = 3;

    public SingleAlgorithmRandomExperimenter(int numConfigs, int timeoutS)
    {
        super("SingleAlgorithmRandom");

        _numConfigs = numConfigs;
        _timeoutS = timeoutS;

        _configurator = new RandomConfigurator();
    }

    protected SingleAlgorithmRandomExperimenter(String experimenterName, int numConfigs, int timeoutS)
    {
        this(numConfigs, timeoutS);
        _experimentName = experimenterName;
    }

    @Override
    protected void performExperiment(String experimentID, ProblemInfo problemInfo, ProblemInstanceInfo instanceInfo,
            String[] algorithmIDs) throws Exception
    {
        for (int i = 0; i < algorithmIDs.length; i++)
        {
            String problemID = problemInfo.getProblemID();
            String instanceID = instanceInfo.getInstanceID();
            String algorithmID = algorithmIDs[i];

            _logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", algorithmID, i + 1, algorithmIDs.length));
            _logger.info(String.format("%-44s", "   Running... "));

            List<Runnable> taskQueue = new ArrayList<Runnable>();
            ProblemConfig[] configs = _configurator.prepareConfigs(problemInfo, instanceInfo.getInstanceID(),
                    algorithmID, _numConfigs);
            for (ProblemConfig config : configs)
            {
                for (int runID = 1; runID <= NUM_RUNS; runID++)
                {
                    //String reportName = problemInfo.getProblemID() + "-" + algorithmID + "-" + instanceInfo.getInstanceID() + "-" + configID + "-" + runID + ".xml";
                    taskQueue.add(new ExperimentTask(_experimentName, experimentID, problemID,
                            instanceID, algorithmID, config.getAlgorithmParams(), runID, _timeoutS));
                }
            }
            TaskRunner3.run(taskQueue.toArray(new Runnable[] {}), Runtime.getRuntime().availableProcessors());
        }
    }

    @Override
    protected long getEstimatedTime(int instancesCount, int algorithmsCount)
    {
        return getNumberOfConfigs(instancesCount, algorithmsCount) * _timeoutS * 1000;
    }

    @Override
    protected long getNumberOfConfigs(int instancesCount, int algorithmsCount)
    {
        return _numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
    }
}
