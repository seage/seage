package org.seage.experimenter.singlealgorithm.random;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimentTask;
import org.seage.thread.TaskRunnerEx;

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
    protected void performExperiment(long experimentID, ProblemInfo problemInfo, ProblemInstanceInfo instanceInfo,
            String[] algorithmIDs, ZipOutputStream zos) throws Exception
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
                    taskQueue.add(new SingleAlgorithmExperimentTask(_experimentName, experimentID, problemID,
                            instanceID, algorithmID, config.getAlgorithmParams(), runID, _timeoutS, zos));
                }
            }
            new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[] {}));
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
