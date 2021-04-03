package org.seage.launcher.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProblemInstanceParamsParser {

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
    // String instanceID = p[1];

    if (problemID.equals("ALL:hyflex")) {
      parsed.put("TSP", Arrays.asList(new String[] { "h1", "h2" }));
      parsed.put("SAT", Arrays.asList(new String[] { "h3", "h4" }));
      return;
    }
    throw new Exception("Unknown instance arguments");
  }
}
