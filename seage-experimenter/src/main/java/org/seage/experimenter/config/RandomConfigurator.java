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

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;

/**
 * 
 * @author rick
 */
public class RandomConfigurator extends Configurator
{

    // private String _algID;
    // private int _numConfigs;
    //private DataNode _paramInfo;

    // public RandomConfigurator() {
    // _algID = algID;
    // _numConfigs = numConfigs;
    // }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, DataNode instanceInfo, int numConfigs) throws Exception
    {

        List<ProblemConfig> results = new ArrayList<ProblemConfig>();
        // List<List<Double>> values = new ArrayList<List<Double>>();

        //System.out.println(instanceInfo.getValue("path"));
        
        for (int i = 0; i < numConfigs; i++)
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
            
            for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter"))
            {
                String name = paramNode.getValueStr("name");
                double min = paramNode.getValueDouble("min");
                double max = paramNode.getValueDouble("max");
                double val = min + (max - min) * Math.random();
                config.getDataNode("Algorithm").getDataNode("Parameters").putValue(name, val);
            }

            config.putValue("configID", FileHelper.md5fromString(XmlHelper.getStringFromDocument(config.toXml())));
            results.add(config);
        }

        //

        return results.toArray(new ProblemConfig[0]);
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}
