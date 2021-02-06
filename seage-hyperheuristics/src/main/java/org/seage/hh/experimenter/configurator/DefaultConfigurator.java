/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.hh.experimenter.configurator;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

/**
 * DefaultConfigurator class.
 * @author Richard Malek
 */
public class DefaultConfigurator extends Configurator {
  @Override
  public ProblemConfig[] prepareConfigs(
      ProblemInfo problemInfo, String instanceID, String algID, int numConfigs)
      throws Exception {
    ProblemConfig result = new ProblemConfig("Config");
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));

    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);

    DataNode params = new DataNode("Parameters");
    for (DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter"))
      params.putValue(dn.getValueStr("name"), dn.getValue("init"));

    algorithm.putDataNode(params);

    result.putDataNode(problem);
    result.putDataNode(algorithm);

    return new ProblemConfig[] { result };
  }

}
