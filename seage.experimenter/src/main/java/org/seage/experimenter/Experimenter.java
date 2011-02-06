/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.experimenter.config.DefaultConfigurator;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.experimenter.config.IntervalConfigurator;

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
        Configurator dc = new IntervalConfigurator("");
    }

    public void runExperiments(String problemID, String algorithmID) throws Exception {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        Configurator dc = new IntervalConfigurator(algorithmID);

        ExperimentRunner.run(dc.prepareConfigs(pi));
    }

}
