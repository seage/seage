package org.seage.launcher.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemInstanceParamsParser {

  private static final Map<String,  List<String>> hyflexInstances = Map.of(
      "SAT", Arrays.asList(
          "pg-525-2276-hyflex-3",
          "pg-696-3122-hyflex-5", 
          "pg-525-2336-hyflex-4",
          "jarv-684-2300-hyflex-10", 
          "hg4-300-1200-hyflex-11"),
      "TSP", Arrays.asList(
          "pr299-hyflex-0", 
          "usa13509-hyflex-8", 
          "rat575-hyflex-2",
          "u2152-hyflex-7", 
          "d1291-hyflex-6"),
      "FSP", Arrays.asList(
          "tai100_20_02",
          "tai500_20_02",
          "tai100_20_04",
          "tai200_20_01",
          "tai500_20_03"),
      "QAP", Arrays.asList(
          "sko100a",
          "tai100a",
          "tai256c",
          "tho150",
          "wil100") 
  );

  /**
   * Parses command line instance arguments.
   * @param instanceParams .
   * @return Map of problems and instances.
   * @throws Exception .
   */
  public static Map<String, List<String>> parseProblemInstanceParams(
      List<String> instanceParams) throws Exception {
    
    HashMap<String, List<String>> instances = new HashMap<>();

    for (String param : instanceParams) {
      parseParam(param, instances);
    }

    return instances;
  }

  private static void parseParam(
      String param, Map<String, List<String>> parsed) throws Exception {
    String[] p = param.split(":");
    String problemID = p[0];
    String instanceID = p[1];

    if (instanceID.equals("hyflex")) {
      switch (problemID) {
        case "ALL":
          parsed = hyflexInstances;
          break;

        case "SAT":
          parsed.put("SAT", hyflexInstances.get("SAT"));
          break;

        case "TSP":
          parsed.put("TSP", hyflexInstances.get("TSP"));
          break;
          
        case "FSP":
          parsed.put("FSP", hyflexInstances.get("FSP"));
          break;
        
        case "QAP":
          parsed.put("QAP", hyflexInstances.get("QAP"));
          break;
        
        default:
          throw new IllegalArgumentException("Bad problemID");
      }
      return;
    }   
  
    if (!parsed.containsKey(problemID)) {
      parsed.put(problemID, new ArrayList<>());
    }

    parsed.get(problemID).add(instanceID); 
  }
}
