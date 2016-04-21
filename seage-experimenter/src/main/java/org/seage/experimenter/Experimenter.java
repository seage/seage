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
 * 	   Richard Malek
 * 	   - Interface definition
 */
package org.seage.experimenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Experimenter
{
    protected static Logger _logger = LoggerFactory.getLogger(Experimenter.class.getName());
    protected String _experimentName;

    public Experimenter(String experimentName)
    {
        _experimentName = experimentName;

        new File("output/experiment-logs").mkdirs();
    }

    public void runFromConfigFile(String configPath) throws Exception
    {
        throw new Exception("Not implemented");
    }

    public final void runExperiment(String problemID, String[] instanceIDs, String[] algorithmIDs) throws Exception
    {
        ProblemInfo problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        // *** Check arguments ***
        if (instanceIDs[0].equals("-"))
        {
            List<String> instIDs = new ArrayList<String>();
            for (DataNode ins : problemInfo.getDataNode("Instances").getDataNodes("Instance"))
                instIDs.add(ins.getValueStr("id"));
            instanceIDs = instIDs.toArray(new String[] {});
        }

        if (algorithmIDs[0].equals("-"))
        {
            List<String> algIDs = new ArrayList<String>();
            for (DataNode alg : problemInfo.getDataNode("Algorithms").getDataNodes("Algorithm"))
                algIDs.add(alg.getValueStr("id"));
            algorithmIDs = algIDs.toArray(new String[] {});
        }
        // ***********************

        long experimentID = System.currentTimeMillis();
        _logger.info("-------------------------------------");
        _logger.info("Experimenter: " + _experimentName);
        _logger.info("ExperimentID: " + experimentID);
        _logger.info("-------------------------------------");

        long totalNumOfConfigs = getNumberOfConfigs(instanceIDs.length, algorithmIDs.length);
        long totalRunsPerCpu = totalNumOfConfigs / Runtime.getRuntime().availableProcessors();
        long totalEstimatedTime = getEstimatedTime(instanceIDs.length, algorithmIDs.length)
                / Runtime.getRuntime().availableProcessors();

        _logger.info(String.format("%-25s: %s", "Total number of configs", totalNumOfConfigs));
        _logger.info("Total number of configs per cpu core: " + totalRunsPerCpu);
        _logger.info("Total estimated time: " + getDurationBreakdown(totalEstimatedTime) + " (DD:HH:mm:ss)");
        _logger.info("-------------------------------------");

        for (int i = 0; i < instanceIDs.length; i++)
        {
            try
            {
                ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceIDs[i]);

                _logger.info("-------------------------------------");
                _logger.info(String.format("%-15s %s", "Problem:", problemID));
                _logger.info(String.format("%-15s %-16s    (%d/%d)", "Instance:", instanceIDs[i], i + 1,
                        instanceIDs.length));

                String reportPath = String.format("output/experiment-logs/%s-%s-%s.zip", experimentID, problemID,
                        instanceIDs[i]);

                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)));

                performExperiment(experimentID, problemInfo, instanceInfo, algorithmIDs, zos);

                zos.close();
            }
            catch (Exception ex)
            {
                _logger.warn( ex.getMessage(), ex);
            }

        }
        _logger.info("-------------------------------------");
        _logger.info("Experiment " + experimentID + " finished ...");
        _logger.info(String.format("Experiment duration: %s (DD:HH:mm:ss)",
                getDurationBreakdown(System.currentTimeMillis() - experimentID)));
    }

    protected abstract void performExperiment(long experimentID, ProblemInfo problemInfo,
            ProblemInstanceInfo instanceInfo, String[] algorithmIDs, ZipOutputStream zos) throws Exception;

    protected abstract long getEstimatedTime(int instancesCount, int algorithmsCount);

    protected abstract long getNumberOfConfigs(int instancesCount, int algorithmsCount);

    protected static String getDurationBreakdown(long millis)
    {
        if (millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

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
