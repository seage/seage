/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Jan Zmatlik
 *     - Modified
 */
package org.seage.experimenter.singlealgorithm;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.experimenter.ExperimentRunner;
import org.seage.experimenter.IExperimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.experimenter.config.RandomConfigurator;

/**
 * 
 * @author rick
 */
public class SingleAlgorithmExperimenter implements IExperimenter
{

    private static Logger _logger = Logger.getLogger(SingleAlgorithmExperimenter.class.getName());

    Configurator _configurator;
    ExperimentRunner _experimentRunner;

    public SingleAlgorithmExperimenter()
    {
        _configurator = new RandomConfigurator();

        _experimentRunner = new ExperimentRunner();
    }

    public void runFromConfigFile(String configPath) throws Exception
    {
        _experimentRunner.run(configPath, Long.MAX_VALUE);
    }

    public void runExperiment(String problemID, int numRuns, long timeoutS) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<String> algIDs = new ArrayList<String>();
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));

        runExperiment(problemID, numRuns, timeoutS, algIDs.toArray(new String[] {}));
    }

    public void runExperiment(String problemID, int numRuns, long timeoutS, String[] algorithmIDs) throws Exception
    {
        long experimentID = System.currentTimeMillis();
        _logger.log(Level.INFO, "Running the experiment "+experimentID+" ...");
        _logger.info("Mem: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));

        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        // ic.prepareConfigs(pi);

        for (String algID : algorithmIDs)
        {
            for (DataNode instanceInfo : pi.getDataNode("Instances").getDataNodes())
            {
                List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
                configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, instanceInfo, numRuns)));

                
                String instanceName = instanceInfo.getValueStr("name");
                long numOfConfigs = configs.size();
                long runsPerCpu = numOfConfigs * 5 / Runtime.getRuntime().availableProcessors();
                long estimatedTime = runsPerCpu * timeoutS;
                _logger.info("Algorithm: " + algID);
                _logger.info("Instance: " + instanceName);
                _logger.info("Number of configs: " + numOfConfigs);
                _logger.info("Number of runs: " + numOfConfigs * 5);
                _logger.info("Runs per cpu core: " + runsPerCpu);
                _logger.info("Estimated time in seconds: " + estimatedTime);
                _logger.info("Estimated time: " + getDurationBreakdown(estimatedTime * 1000) + " (DD:HH:mm:ss)");
                _logger.info("Memory used for configs: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));

                long expID = _experimentRunner.run(configs.toArray(new ProblemConfig[] {}), experimentID, algID, instanceName, timeoutS);
                _logger.log(Level.INFO, "Experiment " + expID + " finished");
            }
        }
    }

    private static String getDurationBreakdown(long millis)
    {
        if (millis < 0) { throw new IllegalArgumentException("Duration must be greater than zero!"); }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds); // (sb.toString());
    }

}
