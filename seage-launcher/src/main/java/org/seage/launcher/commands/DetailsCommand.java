package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
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


@Parameters(commandDescription = "Show problem details")
public class DetailsCommand extends Command {
  @Parameter(names = "-p", required = true, description = "ProblemID")
  String problemID;

  private static final Logger log = LoggerFactory.getLogger(DetailsCommand.class.getName());

  @Override
  public void performCommand() throws Exception {
    log.info("Problems details");
    log.info("----------------");

    DataNode problems = new DataNode("Problems");
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();

    try {
      IProblemProvider<?> pp = providers.get(problemID);
      if (pp == null) {
        throw new Exception("Unknown problem id: " + problemID);
      }
      DataNode pi = pp.getProblemInfo();
      problems.putDataNode(pi);

      String name = pi.getValueStr("name");
      log.info(problemID + " - " + name);

      log.info("\tinstances:");

      List<String> instanceIDs = new ArrayList<>();
      for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
        instanceIDs.add(inst.getValueStr("id"));
      }

      Collections.sort(instanceIDs);
      String line = "";
      for (int i = 0; i < instanceIDs.size(); i++) {
        line += String.format("%-25s", instanceIDs.get(i));
        if ((i + 1) % 4 == 0) {
          log.info("\t\t" + line);
          line = "";
        }
      }
      log.info("");

      log.info("\talgorithms:");
      for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes()) {
        log.info("\t\t" + alg.getValueStr("id"));

        for (DataNode param : alg.getDataNodes("Parameter")) {
          String msg = String.format("\t\t\t%s (%s, %s, %s)", param.getValueStr("name"),
              param.getValueStr("min"), param.getValueStr("max"), param.getValueStr("init"));
          log.info(msg);
        }
      }
    } catch (Exception ex) {
      log.error(problemID + ": " + ex.getMessage(), ex);
    }
  }
}
