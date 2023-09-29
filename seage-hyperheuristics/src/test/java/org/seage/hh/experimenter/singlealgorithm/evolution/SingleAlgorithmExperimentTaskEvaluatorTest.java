package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

public class SingleAlgorithmExperimentTaskEvaluatorTest {
  SingleAlgorithmExperimentTaskEvaluator evaluator;
  ProblemInfo problemInfo;
  HashMap<String, ProblemInstanceInfo> instancesInfo;
  List<String> instanceIDs;
  String problemID;
  FeedbackConfigurator feedbackConfigurator;
  List<SingleAlgorithmExperimentTaskSubject> subjects;


  private Void reportFn(ExperimentTaskRecord experiment) {
    // todo
    return null;
  }

  List<SingleAlgorithmExperimentTaskSubject> initSubjects(
      ProblemInfo problemInfo, List<String>instanceslList, 
      String algorithmID, int numOfSubjects) throws Exception {
    List<SingleAlgorithmExperimentTaskSubject> result = new ArrayList<>();
    int numPerInstance = Math.max(
        instanceIDs.size(), 
        (int) Math.ceil(numOfSubjects / (double) instanceIDs.size()));
    int curNumOfSubjects = 0;

    for (String instanceID : instanceIDs) {
      if (curNumOfSubjects >= numOfSubjects) {
        break;
      }

      ProblemConfig[] pc = feedbackConfigurator.prepareConfigs(
        problemInfo, instanceID, algorithmID, numPerInstance);

      List<DataNode> params = problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID)
          .getDataNodes("Parameter");

      for (int i = 0; i < numPerInstance; i++) {
        if (curNumOfSubjects >= numOfSubjects) {
          break;
        }
        
        String[] names = new String[params.size()];
        Double[] values = new Double[params.size()];
        for (int j = 0; j < params.size(); j++) {
          names[j] = params.get(j).getValueStr("name");
          values[j] = pc[i].getDataNode(
            "Algorithm").getDataNode("Parameters").getValueDouble(names[j]);
        }
        result.add(new SingleAlgorithmExperimentTaskSubject(names, values));

        curNumOfSubjects += 1;
      }
    }

    // Return the right size of results
    return result.subList(0, numOfSubjects);
  }

  @BeforeAll
  void setEvaluator() throws Exception {
    this.problemID = "TSP";
    this.instanceIDs = new ArrayList<>() {
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
    this.problemInfo = 
      ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    this.instancesInfo = new HashMap<>();
    for (String instanceID : instanceIDs) {
      this.instancesInfo.put(instanceID, problemInfo.getProblemInstanceInfo(instanceID));
    }

    this.feedbackConfigurator = new FeedbackConfigurator(0.0);
    // Initialize subjects
    this.subjects = initSubjects(problemInfo, instanceIDs, problemID, 10);
  }
}
