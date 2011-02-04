/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public abstract class Configurator {
    public abstract DataNode[] prepareConfigs(DataNode problemInfo)  throws Exception;
}
