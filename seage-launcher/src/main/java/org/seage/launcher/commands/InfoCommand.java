package org.seage.launcher.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.beust.jcommander.Parameters;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Parameters(commandDescription = "Table of implemented problems and algorithms")
public class InfoCommand extends Command {
  private static final Logger _logger = LoggerFactory.getLogger(InfoCommand.class.getName());

  @Override
  public void performCommand() throws Exception {
    _logger.info("Implemented problems and algorithms");

    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    List<String> problemList = new ArrayList<>(providers.keySet());
    Map<String, List<String>> algMap = new HashMap<>();

    for (String problemId : providers.keySet()) {
      try {
        IProblemProvider<?> pp = providers.get(problemId);
        DataNode pi = pp.getProblemInfo();
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
          List<String> algList = algMap.get(alg.getValueStr("id"));
          if(algList == null) {
            algList = new ArrayList<>();
            algMap.put(alg.getValueStr("id"), algList);
          }
          algList.add(problemId);
        }
      } catch(Exception ex) {
        // Skip
      }
    }    
    
    List<String> algList = new ArrayList<>(algMap.keySet());
    Collections.sort(problemList, Collections.reverseOrder());
    
    String problemLine = "";
    String problemLineDashes = "";
    for(String problemId : problemList) {
      problemLine += problemId + " ";
    }
    problemLineDashes = problemLine.replaceAll("[A-Z]| ", "-");

    _logger.info(String.format("%-20s%s","-------------------", problemLineDashes));
    _logger.info(String.format("%-20s%s","Algorithm", problemLine));
    _logger.info(String.format("%-20s%s","-------------------", problemLineDashes));
    
    Collections.sort(algList);
    for (String alg : algList) {
      List<String> algProblems = algMap.get(alg);
      String line = String.format("%-20s", alg); 
      
      for(String problemId : problemList) {
        if(algProblems.contains(problemId)) {
          line += " ✓  ";
        } else {
          line += " -  ";
        }
      }
      _logger.info(line);      
    }
    _logger.info(String.format("%-20s%s","-------------------", problemLineDashes));
  }
}
