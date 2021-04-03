package org.seage.launcher.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProblemInstanceParamsParser {
  // HyFlex instancesIDs
  static String[] hyflexSatInstanceIDs = {
    "pg-525-2276-hyflex-3",
    "pg-696-3122-hyflex-5", 
    "pg-525-2336-hyflex-4",
    "jarv-684-2300-hyflex-10", 
    "hg4-300-1200-hyflex-11"
  };

  static String[] hyflexTspInstanceIDs = {
    "pr299-hyflex-0", 
    "usa13509-hyflex-8", 
    "rat575-hyflex-2",
    "u2152-hyflex-7", 
    "d1291-hyflex-6"
  };


  /**
   * Parses command line instance arguments.
   * @param instanceParams .
   * @return Map of problems and instances.
   * @throws Exception .
   */
  public static HashMap<String, List<String>> parseProblemInstanceParams(
      String[] instanceParams) throws Exception {
    
    HashMap<String, List<String>> instances = new HashMap<>();

    for (String param : instanceParams) {
      parseParam(param, instances);
    }

    return instances;
  }

  private static void parseParam(
      String param, HashMap<String, List<String>> parsed) throws Exception {
    String[] p = param.split(":");
    String problemID = p[0];
    String instanceID = p[1];

    if (instanceID.equals("hyflex")) {
      switch (problemID) {
        case "ALL":
          parsed.put("SAT", Arrays.asList(hyflexSatInstanceIDs));
          parsed.put("TSP", Arrays.asList(hyflexTspInstanceIDs));
          break;

        case "TSP":
          parsed.put("TSP", Arrays.asList(hyflexTspInstanceIDs));
          break;
          
        case "SAT":
          parsed.put("SAT", Arrays.asList(hyflexSatInstanceIDs));
          break;
        
        default:
          new Exception("Bad problemID");
      }
      return;
    }   
  
    if (parsed.containsKey(problemID) == false) {
      parsed.put(problemID, new ArrayList<>());
    }

    parsed.get(problemID).add(instanceID); 
  }
}
