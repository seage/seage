package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

public class SingleAlgorithmExperimentTaskEvaluatorTest {
  SingleAlgorithmExperimentTaskEvaluator evaluator;

  private Void reportFn(ExperimentTaskRecord experiment) {
    // todo
    return null;
  }

  @BeforeAll
  void setEvaluator() throws Exception {
    String problemID = "TSP";
    List<String> instanceIDs = new ArrayList<>() {
      {
        add("berlin52");
        add("eil51");
        add("eil76");
        add("pr76");
        add("st70");
      }
    };
    ProblemInfo problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    HashMap<String, ProblemInstanceInfo> instancesInfo = new HashMap<>();
    
    for (String instanceID : instanceIDs) {
      instancesInfo.put(instanceID, problemInfo.getProblemInstanceInfo(instanceID));
    }

    this.evaluator = new SingleAlgorithmExperimentTaskEvaluator(
      null, 
      problemID, 
      instanceIDs, 
      "GeneticAlgorithm", 
      0, 
      instancesInfo, 
      this::reportFn);
  }
}
