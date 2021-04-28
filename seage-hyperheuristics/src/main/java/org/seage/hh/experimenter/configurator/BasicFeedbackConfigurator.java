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
import java.util.List;
import java.util.Map;
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

  private ProblemConfig createConfig(
      ProblemInfo problemInfo, String instanceID, String algID, double spread)
      throws Exception {


    Map<String, Map<String, Map<String, Double>>> bestDefaultParams = new HashMap<>() {{
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
                put("alpha", 1.0);
                put("beta", 2.4636979267198944);
                put("defaultPheromone", 1.0E-5);
                put("iterationCount", 1000000.0);
                put("localEvaporation", 0.9786027772416056);
                put("numSolutions", 158.99322806081162);
                put("qantumOfPheromone", 69.52953013409171);
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
        put("TSP", new HashMap<>() {{
            put("GeneticAlgorithm", new HashMap<>() {{
                put("crossLengthPct", 10.12645264293384);
                put("eliteSubjectPct", 9.87354735706616);
                put("iterationCount", 998735.6000233046);
                put("mutateLengthPct", 10.12645264293384);
                put("mutateSubjectPct", 9.87354735706616);
                put("numSolutions", 98.748118834955);
                put("randomSubjectPct", 9.87354735706616);
              }
            });
            put("SimulatedAnnealing", new HashMap<>() {{
                put("iterationCount", 9.001462912250427E8);
                put("maxTemperature", 9.995360802110308E7);
                put("minTemperature", 0.0);
                put("numSolutions", 1.0);
              }
            });
            put("AntColony", new HashMap<>() {{
                put("alpha", 1.508466606577072);
                put("beta", 2.491533393422928);
                put("defaultPheromone", 0.05650572465677848);
                put("iterationCount", 1000000.0);
                put("localEvaporation", 0.9771182190174438);
                put("numSolutions", 44.06867327652208);
                put("qantumOfPheromone", 1.0);
              }
            });
            put("TabuSearch", new HashMap<>() {{
                put("iterationCount", 9.968307133974689E10);
                put("numSolutions", 1.0);
                put("tabuListLength", 33.16611731596033);
              }
            });
          }
        });
      }
    };


    
    Map<String, Map<String, Map<String, Double>>> bestRandomParams = new HashMap<>() {{
        put("SAT", new HashMap<>() {{
            put("GeneticAlgorithm", new HashMap<>() {{
                put("crossLengthPct", 16.92164812580709);
                put("eliteSubjectPct", 16.92164812580709);
                put("iterationCount", 930790.440390055);
                put("mutateLengthPct", 3.0783518741929106);
                put("mutateSubjectPct", 16.92164812580709);
                put("numSolutions", 168.52431644549017);
                put("randomSubjectPct", 3.0783518741929106);
              }
            });
            put("SimulatedAnnealing", new HashMap<>() {{
                put("iterationCount", 9.525273894507701E8);
                put("maxTemperature", 1000.0);
                put("minTemperature", 0.0);
                put("numSolutions", 1.0);
              }
            });
            put("AntColony", new HashMap<>() {{
                put("alpha", 1.0);
                put("beta", 2.859904382201463);
                put("defaultPheromone", 0.01557602409359541);
                put("iterationCount", 1000000.0);
                put("localEvaporation", 0.9425282337174113);
                put("numSolutions", 115.41051795783903);
                put("qantumOfPheromone", 1.0);
              }
            });
            put("TabuSearch", new HashMap<>() {{
                put("iterationCount", 905467.3922561172);
                put("numSolutions", 1.0);
                put("tabuListLength", 124.43816957430852);
              }
            });
          }
        });
        put("TSP", new HashMap<>() {{
            put("GeneticAlgorithm", new HashMap<>() {{
                put("crossLengthPct", 19.036494354929765);
                put("eliteSubjectPct", 19.03649435492976);
                put("iterationCount", 1000000.0);
                put("mutateLengthPct", 0.9635056450702351);
                put("mutateSubjectPct", 0.9635056450702351);
                put("numSolutions", 189.46129411380468);
                put("randomSubjectPct", 0.9635056450702351);
              }
            });
            put("SimulatedAnnealing", new HashMap<>() {{
                put("iterationCount", 9.99999999E8);
                put("maxTemperature", 9.74985598511794E7);
                put("minTemperature", 0.0);
                put("numSolutions", 1.0);
              }
            });
            put("AntColony", new HashMap<>() {{
                put("alpha", 1.0);
                put("beta", 2.3861673412171998);
                put("defaultPheromone", 0.06821294671735695);
                put("iterationCount", 931797.0532826431);
                put("localEvaporation", 0.9172622581982506);
                put("numSolutions", 167.52159246610802);
                put("qantumOfPheromone", 78.13542512489084);
              }
            });
            put("TabuSearch", new HashMap<>() {{
                put("iterationCount", 1000000.0);
                put("numSolutions", 1.0);
                put("tabuListLength", 30.0);
              }
            });
          }
        });
      }
    };
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));
    
    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);
    
    DataNode params = new DataNode("Parameters");
    ProblemConfig config = new ProblemConfig("Config");
    for (DataNode dn : problemInfo
        .getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter")) {
      
    //   double value = dn.getValueDouble("init");
    //   if(spread != 0) {
    //     double min = dn.getValueDouble("min");
    //     double max = dn.getValueDouble("max");
    //     double sign = Math.random() > 0.5 ? 1 : -1;
    //     double delta = (max - min) * spread * sign;
    //     value = Math.max(min, Math.min(max, value + delta));
    //}
    //System.out.println("\t\t\t\n\n\n" + problemInfo.getValue("id") + " " + algID + " "+ instanceID);
      params.putValue(dn.getValueStr("name"), 
          bestRandomParams.get(problemInfo.getValue("id"))
            .get(algID).get(dn.getValueStr("name")));
    }      

    algorithm.putDataNode(params);

    config.putDataNode(problem);
    config.putDataNode(algorithm);
    return config;
  }

}
