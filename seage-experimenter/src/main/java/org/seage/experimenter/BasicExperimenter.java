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
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.experimenter;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.experimenter.config.RandomConfigurator;

/**
 *
 * @author rick
 */
public class BasicExperimenter implements IExperimenter {

    Configurator _configurator;
    
    public BasicExperimenter()
    {
        _configurator = new RandomConfigurator();
    }
    
    public void runFromConfigFile(String configPath) throws Exception {
        ExperimentRunner.run(configPath, Long.MAX_VALUE);
        
    }

    public void runExperiments(String problemID, int numRuns, long timeoutS) throws Exception {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        List<String> algIDs = new ArrayList<String>();
        for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));
        
        runExperiments(problemID, numRuns, timeoutS, algIDs.toArray(new String[]{}));
    }

    public void runExperiments(String problemID, int numRuns, long timeoutS, String[] algorithmIDs) throws Exception {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        //ic.prepareConfigs(pi);
        List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
        for(String algID : algorithmIDs)
            configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, numRuns)));
                
        ExperimentRunner.run(configs.toArray(new ProblemConfig[]{}), timeoutS);
    }

}
