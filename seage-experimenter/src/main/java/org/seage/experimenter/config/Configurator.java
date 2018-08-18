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
package org.seage.experimenter.config;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Richard Malek
 */
public abstract class Configurator
{
    protected static Logger _logger = LoggerFactory.getLogger(Configurator.class.getName());

    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String instanceID, String algorithmID,
            int numConfigs) throws Exception;

    protected ProblemConfig createProblemConfig(ProblemInfo problemInfo, String instanceID, String algorithmID)
            throws Exception
    {
        ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
        // Config
        ProblemConfig config = new ProblemConfig("Config");
        // |_ Problem
        DataNode problem = new DataNode("Problem");
        // | |_ Instance
        DataNode instance = new DataNode("Instance");
        // |_ Algorithm
        DataNode algorithm = new DataNode("Algorithm");
        //   |_ Parameters
        DataNode params = new DataNode("Parameters");

        problem.putValue("id", problemInfo.getValue("id"));
        algorithm.putValue("id", algorithmID);

        algorithm.putDataNodeRef(params);
        problem.putDataNodeRef(instance);
        config.putDataNodeRef(algorithm);
        config.putDataNodeRef(problem);

        // ProblemConfig instanceCfg = (ProblemConfig)config.clone();
        config.getDataNode("Problem").getDataNode("Instance").putValue("name", instanceInfo.getValue("name"));
        config.getDataNode("Problem").getDataNode("Instance").putValue("type", instanceInfo.getValue("type"));
        config.getDataNode("Problem").getDataNode("Instance").putValue("path", instanceInfo.getValue("path"));

        if (problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID) == null)
            throw new Exception("Unknown algorithm id: " + algorithmID);

        return config;
    }
}
