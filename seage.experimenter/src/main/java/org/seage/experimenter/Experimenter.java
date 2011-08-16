/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.experimenter;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.seage.experimenter.config.DefaultConfigurator;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.experimenter.config.IntervalConfigurator;
import org.seage.experimenter.config.RandomConfigurator;

/**
 *
 * @author rick
 */
public class Experimenter {

    Configurator _configurator;
    
    public Experimenter()
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
