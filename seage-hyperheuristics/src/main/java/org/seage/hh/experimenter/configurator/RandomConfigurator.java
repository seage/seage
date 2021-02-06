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
import org.seage.data.HashHelper;
import org.seage.data.xml.XmlHelper;

/**
 * 
 * @author Richard Malek
 */
public class RandomConfigurator extends Configurator {
  @Override
  public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String instanceID, String algorithmID, int numConfigs)
      throws Exception {

    List<ProblemConfig> results = new ArrayList<>();

    for (int i = 0; i < numConfigs; i++) {
      ProblemConfig config = createProblemConfig(problemInfo, instanceID, algorithmID);

      for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
          .getDataNodes("Parameter")) {
        String name = paramNode.getValueStr("name");
        double min = paramNode.getValueDouble("min");
        double max = paramNode.getValueDouble("max");
        double val = min + (max - min) * Math.random();
        config.getDataNode("Algorithm").getDataNode("Parameters").putValue(name, val);
      }

      config.putValue("configID", HashHelper.hashFromString(XmlHelper.getStringFromDocument(config.toXml())));
      results.add(config);
    }

    //

    return results.toArray(new ProblemConfig[0]);
  }
}
