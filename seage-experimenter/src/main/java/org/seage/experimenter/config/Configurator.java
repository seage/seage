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

import java.util.logging.Logger;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimenter;

/**
 * 
 * @author rick
 */
public abstract class Configurator
{
	protected static Logger _logger = Logger.getLogger(Configurator.class.getName());
	
    // TODO: A - Remove this method, replace it by the second one.
    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception;

    public abstract ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, DataNode instanceInfo, int numConfigs) throws Exception;

    protected ProblemConfig createProblemConfig(ProblemInfo problemInfo, String algID, DataNode instanceInfo) throws Exception
    {
    	// Config
        ProblemConfig config = new ProblemConfig("Config");
        // |_ Problem
        DataNode problem = new DataNode("Problem");
        // | |_ Instance
        DataNode instance = new DataNode("Instance");
        // |_ Algorithm
        DataNode algorithm = new DataNode("Algorithm");
        // |_ Parameters
        DataNode params = new DataNode("Parameters");

        problem.putValue("id", problemInfo.getValue("id"));
        algorithm.putValue("id", algID);

        algorithm.putDataNodeRef(params);
        problem.putDataNodeRef(instance);
        config.putDataNodeRef(algorithm);
        config.putDataNodeRef(problem);

        // ProblemConfig instanceCfg = (ProblemConfig)config.clone();
        config.getDataNode("Problem").getDataNode("Instance").putValue("name", instanceInfo.getValue("name"));
        config.getDataNode("Problem").getDataNode("Instance").putValue("type", instanceInfo.getValue("type"));
        config.getDataNode("Problem").getDataNode("Instance").putValue("path", instanceInfo.getValue("path"));
        
        if(problemInfo.getDataNode("Algorithms").getDataNodeById(algID)==null)
        	throw new Exception("Unknown algorithm id: " + algID);
        
        return config;
    }
}
