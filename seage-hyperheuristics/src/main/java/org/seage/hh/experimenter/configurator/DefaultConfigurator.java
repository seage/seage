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
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.hh.experimenter.configurator;

import java.util.ArrayList;
import java.util.List;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

/**
 * DefaultConfigurator class.
 * @author Richard Malek
 */
public class DefaultConfigurator extends Configurator {
  private double spread;

  /**
   * DefaultConfigurator constructor.
   * @param spread The maximal distance from the init value (0, 1>
   */
  public DefaultConfigurator(double spread) {
    this.spread = spread;
  }

  @Override
  public ProblemConfig[] prepareConfigs(
      ProblemInfo problemInfo, String instanceID, String algID, int numConfigs)
      throws Exception {

    List<ProblemConfig> results = new ArrayList<>();

    results.add(createConfig(problemInfo, instanceID, algID, 0));
    for (int i = 1; i < numConfigs; i++) {
      double s = Math.random() * this.spread;
      results.add(createConfig(problemInfo, instanceID, algID, s));
    }

    return results.toArray(new ProblemConfig[0]);
  }

  private ProblemConfig createConfig(
      ProblemInfo problemInfo, String instanceID, String algID, double spread)
      throws Exception {
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));

    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);

    DataNode params = new DataNode("Parameters");
    for (DataNode dn : problemInfo
        .getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter")
    ) {
      double value = dn.getValueDouble("init");
      if (spread != 0) {
        double min = dn.getValueDouble("min");
        double max = dn.getValueDouble("max");
        double sign = Math.random() > 0.5 ? 1 : -1;
        double delta = (max - min) * spread * sign;
        value = Math.max(min, Math.min(max, value + delta));
      }
      params.putValue(dn.getValueStr("name"), value);
    }      

    algorithm.putDataNode(params);

    ProblemConfig config = new ProblemConfig("Config");
    config.putDataNode(problem);
    config.putDataNode(algorithm);
    return config;
  }

}
