/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter.config;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;

/**
 *
 * @author rick
 */
public abstract class Configurator {
    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo)  throws Exception;
}
