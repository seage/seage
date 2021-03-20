package org.seage.launcher.commands;

import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Parameters(commandDescription = "List implemented problems and algorithms")
public class ListCommand extends Command {
  private static final Logger _logger = LoggerFactory.getLogger(ListCommand.class.getName());

  @Override
  public void performCommad() throws Exception {
    _logger.info("List of implemented problems and algorithms:");
    _logger.info("--------------------------------------------");

    DataNode problems = new DataNode("Problems");
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();

    for (String problemId : providers.keySet()) {
      try {
        IProblemProvider<?> pp = providers.get(problemId);
        DataNode pi = pp.getProblemInfo();
        problems.putDataNode(pi);

        String name = pi.getValueStr("name");
        _logger.info(problemId + " - " + name);

        _logger.info("\talgorithms:");
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
          _logger.info("\t\t" + alg.getValueStr("id"));

          for (DataNode param : alg.getDataNodes("Parameter")) {
            String msg = String.format("\t\t\t%s (%s, %s, %s)", 
                param.getValueStr("name"), 
                param.getValueStr("min"),
                param.getValueStr("max"),
                param.getValueStr("init"));
            _logger.info(msg);
          }
        }
        _logger.info("\tinstances:");

        List<String> instanceIDs = new ArrayList<>();
        for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
          instanceIDs.add(inst.getValueStr("id"));
        }

        Collections.sort(instanceIDs);
        String line = "";
        for (int i = 0;i < instanceIDs.size();i++) {
          line += String.format("%-18s", instanceIDs.get(i));
          if ((i + 1) % 5 == 0) {            
            _logger.info("\t\t" + line);
            line = "";
          }
          
        }
        _logger.info("");
      } catch (Exception ex) {
        _logger.error(problemId + ": " + ex.getMessage(), ex);
      }
    }
  }
}
