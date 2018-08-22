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
 *         Richard Malek
 * 	   - Created the class 
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.hh.experimenter.config;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;

public class RandomConfiguratorEx extends Configurator
{
    // Statistical data on previous run
    private DataNode _statistics;

    public RandomConfiguratorEx(DataNode statistics)
    {
        super();
        _statistics = statistics;
    }

    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String instanceID, String algorithmID,
            int numConfigs) throws Exception
    {
        List<ProblemConfig> results = new ArrayList<ProblemConfig>();

        // Config
        ProblemConfig config = new ProblemConfig("Config");
        //  |_ Problem
        DataNode problem = new DataNode("Problem");
        //  |   |_ Instance
        DataNode instance = new DataNode("Instance");
        //  |_ Algorithm
        DataNode algorithm = new DataNode("Algorithm");
        //      |_ Parameters
        DataNode params = new DataNode("Parameters");

        problem.putValue("id", problemInfo.getValue("id"));
        algorithm.putValue("id", algorithmID);

        algorithm.putDataNode(params);
        problem.putDataNode(instance);
        config.putDataNode(algorithm);
        config.putDataNode(problem);

        int j = 0;
        for (DataNode inst : problemInfo.getDataNode("Instances").getDataNodes())
        {
            ProblemConfig instanceCfg = (ProblemConfig) config.clone();
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("name", inst.getValue("name"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("type", inst.getValue("type"));
            instanceCfg.getDataNode("Problem").getDataNode("Instance").putValue("path", inst.getValue("path"));

            for (int i = 0; i < numConfigs; i++)
            {
                ProblemConfig paramInfo = (ProblemConfig) instanceCfg.clone();
                for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
                        .getDataNodes("Parameter"))
                {
                    String parameterName = paramNode.getValueStr("name");

                    double min = _statistics.getDataNodes().get(j).getValueDouble("min");
                    double max = _statistics.getDataNodes().get(j).getValueDouble("max");
                    double parameterValue = min + (max - min) * Math.random();

                    paramInfo.getDataNode("Algorithm").getDataNode("Parameters").putValue(parameterName,
                            parameterValue);
                    j++;
                }
                j = 0;

                paramInfo.putValue("configID",
                        FileHelper.md5fromString(XmlHelper.getStringFromDocument(paramInfo.toXml())));
                results.add(paramInfo);
            }
        }
        return results.toArray(new ProblemConfig[0]);
    }
}
