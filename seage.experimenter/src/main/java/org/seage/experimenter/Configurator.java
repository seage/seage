/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.aal.ProblemConfig;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
abstract class Configurator {
    public abstract ProblemConfig[] prepareConfigs(DataNode problemInfo)  throws Exception;
}
