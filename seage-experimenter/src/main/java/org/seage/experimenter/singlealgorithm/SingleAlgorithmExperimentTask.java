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
 */

package org.seage.experimenter.singlealgorithm;


import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.experimenter.ExperimentTask;

/**
 * Runs experiment and provides following experiment log:
 * 
 * ExperimentTask 			# version 0.1 
 * |_ ... 
 * 
 * ExperimentReport 		# version 0.2
 *  |_ version (0.4)
 *  |_ experimentID
 *  |_ startTimeMS
 *  |_ timeoutS
 *  |_ durationS
 *  |_ machineName
 *  |_ nrOfCores
 *  |_ totalRAM
 *  |_ availRAM 
 *  |_ Config
 *  |	|_ configID
 *  |	|_ runID
 *  |	|_ Problem
 *  |	|	|_ problemID
 *  |	|	|_ Instance
 *  |	|		|_ name
 *  |	|_ Algorithm
 *  |		|_ algorithmID
 *  |		|_ Parameters
 *  |_ AlgorithmReport
 *  	|_ Parameters
 *  	|_ Statistics
 *  	|_ Minutes
 * 
 * @author Richard Malek
 */
public class SingleAlgorithmExperimentTask extends ExperimentTask
{
    public SingleAlgorithmExperimentTask(String experimentType, String experimentID, String problemID, String instanceID,
            String algorithmID, AlgorithmParams algorithmParams, int runID, long timeoutS,
            ZipOutputStream reportOutputStream) throws Exception
    {
        super(experimentType, experimentID, problemID, instanceID, algorithmID, algorithmParams, runID, timeoutS);
    }

    @Override
    public void run()
    {
        try
        {
            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(_problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(_algorithmID);

            // problem instance
            ProblemInstance instance = provider
                    .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(_instanceID));
            instance.toString();
            // algorithm
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance);

            Phenotype<?>[] solutions = provider.generateInitialSolutions(instance,
                    _algorithmParams.getValueInt("numSolutions"), _experimentID.hashCode());

            long startTime = System.currentTimeMillis();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(_algorithmParams, true);
            waitForTimeout(algorithm);
            algorithm.stopSearching();
            long endTime = System.currentTimeMillis();

            solutions = algorithm.solutionsToPhenotype();

            _experimentTaskReport.putDataNode(algorithm.getReport());
            _experimentTaskReport.putValue("durationS", (endTime - startTime) / 1000);

            //XmlHelper.writeXml(_experimentTaskReport, _reportOutputStream, getReportName());

        }
        catch (Exception ex)
        {
            _logger.error( ex.getMessage(), ex);
            _logger.error( _algorithmParams.toString());

        }
    }

    private void waitForTimeout(IAlgorithmAdapter alg) throws Exception
    {
        long time = System.currentTimeMillis();
        while (alg.isRunning() && ((System.currentTimeMillis() - time) < _timeoutS * 1000))
            Thread.sleep(100);
    }

}
