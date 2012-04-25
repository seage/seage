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
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.experimenter.config;

import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class DefaultConfigurator extends Configurator{

    //private String _problemID;
    //private String _algID;

//    public DefaultConfigurator(String algID) {
//        _algID = algID;
//    }


    @Override
    public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String algID, int numConfigs) throws Exception {
        ProblemConfig result = new ProblemConfig("Config");
        //result.putDataNode(problemInfo.getDataNode("Algorithms").getDataNodeById(_algID));
        DataNode problem = new DataNode("Problem");
        problem.putValue("id", problemInfo.getValue("id"));
        problem.putDataNode(problemInfo.getDataNode("Instances").getDataNode("Instance", 0));
        
        DataNode algorithm = new DataNode("Algorithm");
        algorithm.putValue("id", algID);

        DataNode params = new DataNode("Parameters");
        for(DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter"))
            params.putValue(dn.getValueStr("name"), dn.getValue("init"));

        algorithm.putDataNode(params);

        result.putDataNode(problem);
        result.putDataNode(algorithm);

        return new ProblemConfig[]{result};
    }

}
