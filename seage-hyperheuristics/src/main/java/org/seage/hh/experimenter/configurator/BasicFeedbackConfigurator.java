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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

/**
 * DefaultConfigurator class.
 * @author Richard Malek
 */
public class BasicFeedbackConfigurator extends Configurator {
  private double spread;

  /**
   * DefaultConfigurator constructor.
   * @param spread The maximal distance from the init value (0, 1>
   */
  public BasicFeedbackConfigurator() {
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

  private ProblemConfig createConfig(ProblemInfo problemInfo, String instanceID, String algID, double spread)
      throws Exception {


    Map<String, Map<String, Map<String, Double>>> bestParams = new HashMap<>() {{
        put("SAT", new HashMap<>() {{
            put("GeneticAlgorithm", new HashMap<>() {{
                put("crossLengthPct", 10.166310691188228);
                put("eliteSubjectPct", 10.166310691188228);
                put("iterationCount", 1000000.0);
                put("mutateLengthPct", 9.833689308811772);
                put("mutateSubjectPct", 9.833689308811772);
                put("numSolutions", 98.35352415723654);
                put("randomSubjectPct", 9.833689308811772);
              }
            });
            put("SimulatedAnnealing", new HashMap<>() {{
                put("iterationCount", 8.549159392982824E8);
                put("maxTemperature", 1000.0);
                put("minTemperature", 0.0);
                put("numSolutions", 1.0);
              }
            });
            put("AntColony", new HashMap<>() {{
                put("avgObjVal", 376.1);
                put("bestObjVal", 376.1);
                put("initObjVal", 384.1);
                put("lastIterNumberNewSol", 10.0);
                put("numberOfIter", 10.0);
                put("numberOfNewSolutions", 4.0);
              }
            });
            put("TabuSearch", new HashMap<>() {{
                put("iterationCount", 8.623870985731076E10);
                put("numSolutions", 1.0);
                put("tabuListLength", 167.47528852684022);
              }
            });
          }
        });
      }
    };

    ProblemConfig config = new ProblemConfig("Config");
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));

    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);

    DataNode params = new DataNode("Parameters");
    for (DataNode dn : problemInfo.getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter")) {
      
      double value = dn.getValueDouble("init");
      if(spread != 0) {
        double min = dn.getValueDouble("min");
        double max = dn.getValueDouble("max");
        double sign = Math.random() > 0.5 ? 1 : -1;
        double delta = (max - min) * spread * sign;
        value = Math.max(min, Math.min(max, value + delta));
      }
      params.putValue(dn.getValueStr("name"), value);
    }      

    algorithm.putDataNode(params);

    config.putDataNode(problem);
    config.putDataNode(algorithm);
    return config;
  }

}
