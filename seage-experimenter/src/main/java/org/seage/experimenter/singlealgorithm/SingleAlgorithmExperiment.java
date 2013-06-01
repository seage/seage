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

import java.util.logging.Level;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.Experiment;

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
 * @author rick
 */
public class SingleAlgorithmExperiment extends Experiment
{
    public SingleAlgorithmExperiment(String experimentType, long experimentID, long timeoutS, ProblemConfig config, String reportName, ZipOutputStream reportOutputStream, int runID ) throws Exception
    {
    	super(experimentType, experimentID, runID, timeoutS, config, reportName, reportOutputStream);  	
    	       
    }
    
    public void run() {
        String problemID = "";
        String algorithmID = "";
        try{
            problemID = _config.getDataNode("Problem").getValueStr("id");
            algorithmID = _config.getDataNode("Algorithm").getValueStr("id");

            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

            // problem instance
            ProblemInstanceInfo instance = provider.initProblemInstance(_config);
            instance.toString();
            // algorithm
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, _config);

            AlgorithmParams algNode = _config.getAlgorithmParams();
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance, _experimentID);

            long startTime = System.currentTimeMillis();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(algNode, true);
            waitForTimeout(algorithm);
            algorithm.stopSearching();
            long endTime = System.currentTimeMillis();

            solutions = algorithm.solutionsToPhenotype();

            _experimentReport.putDataNode(algorithm.getReport());
            _experimentReport.putValue("durationS", (endTime - startTime)/1000);
            
            XmlHelper.writeXml(_experimentReport, _reportOutputStream, _reportName);

        }
        catch(Exception ex){
        	_logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    private void waitForTimeout(IAlgorithmAdapter alg) throws Exception
    {
        long time = System.currentTimeMillis();
        while(alg.isRunning() && ((System.currentTimeMillis()-time)<_timeoutS*1000))
            Thread.sleep(100);
    }

}
