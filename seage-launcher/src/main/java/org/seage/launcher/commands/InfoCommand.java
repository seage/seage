package org.seage.launcher.commands;

import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Parameters(commandDescription = "Show table of implemented problems and algorithms")
public class InfoCommand extends Command {
  private static final Logger logger = LoggerFactory.getLogger(InfoCommand.class.getName());

  @Override
  public void performCommand() throws Exception {
    logger.info("Implemented problems and algorithms");

    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    List<String> problemList = new ArrayList<>(providers.keySet());
    Map<String, List<String>> algMap = new HashMap<>();

    for (Entry<String, IProblemProvider<Phenotype<?>>> providerEntry : providers.entrySet()) {
      try {
        String problemId = providerEntry.getKey();
        IProblemProvider<?> pp = providerEntry.getValue();
        DataNode pi = pp.getProblemInfo();
        
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
          String algorithmID = alg.getValueStr("id");
          Class<?> c = pp.getAlgorithmFactory(algorithmID).getClass();
          if (c.getAnnotation(Annotations.NotReady.class) != null) {
            continue;
          }
          List<String> algList = algMap.get(algorithmID);
          if (algList == null) {
            algList = new ArrayList<>();
            algMap.put(algorithmID, algList);
          }
          algList.add(problemId);
        }
      } catch (Exception ex) {
        logger.warn("Reading the problem provider info failed: {}", providerEntry.getKey(), ex);
      }
    }    
    
    Collections.sort(problemList, Collections.reverseOrder());
    
    String problemLine = "";
    String problemLineDashes = "";
    for (String problemId : problemList) {
      problemLine += problemId + " ";
    }
    problemLineDashes = problemLine.replaceAll("[A-Z]| ", "-");

    logger.info("-------------------   {}", problemLineDashes);
    logger.info("Algorithm / Problem   {}", problemLine);
    logger.info("-------------------   {}", problemLineDashes);
    
    List<String> algList = new ArrayList<>(algMap.keySet());
    Collections.sort(algList);
    for (String alg : algList) {
      List<String> algProblems = algMap.get(alg);
      String line = String.format("%-22s", alg); 
      
      for (String problemId : problemList) {
        if (algProblems.contains(problemId)) {
          line += " ✓  ";
        } else {
          line += " -  ";
        }
      }
      logger.info(line);      
    }
    logger.info("-------------------   {}", problemLineDashes);
  }
}
