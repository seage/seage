/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

/**
 *
 * @author rick
 */
public class Experimenter {

    public void runFromConfigFile(String configPath) throws Exception{
        new ExperimentRunner().run(configPath);
    }

    public void runExperiments(String problem) throws Exception{
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void runExperiments(String string, String algorithm) throws Exception{
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
