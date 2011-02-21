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

import org.seage.experimenter.config.DefaultConfigurator;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.experimenter.config.IntervalConfigurator;
import org.seage.experimenter.config.RandomConfigurator;

/**
 *
 * @author rick
 */
public class Experimenter {

    public void runFromConfigFile(String configPath) throws Exception {
        ExperimentRunner.run(configPath);
    }

    public void runExperiments(String problemID) throws Exception {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        Configurator dc = new IntervalConfigurator("", 5);
    }

    public void runExperiments(String problemID, String algorithmID, int numRuns) throws Exception {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        Configurator ic = new RandomConfigurator(algorithmID, numRuns);
        //ic.prepareConfigs(pi);

        ExperimentRunner.run(ic.prepareConfigs(pi));
    }

}
