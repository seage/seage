package org.seage.hh.experiment.singlealgorithm.evolution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.TestProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.configurator.FeedbackConfigurator;
import org.seage.hh.experiment.ExperimentTaskRequest;
import org.seage.hh.experiment.singlealgorithm.evolution.SingleAlgorithmConfigsEvolutionSubject;
import org.seage.hh.experiment.singlealgorithm.evolution.SingleAlgorithmConfigsEvolutionSubjectEvaluator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

/**
 * .
 */
class SingleAlgorithmExperimentTaskEvaluatorTest {
  SingleAlgorithmConfigsEvolutionSubjectEvaluator evaluator;
  ProblemInfo problemInfo;
  HashMap<String, ProblemInstanceInfo> instancesInfo;
  List<String> instanceIDs;
  String problemID;
  String algorithmID;
  FeedbackConfigurator feedbackConfigurator;
  List<SingleAlgorithmConfigsEvolutionSubject> subjects;
  List<String> configIDs;


  private Void reportFn(ExperimentTaskRecord experiment) {
    // Empty
    return null;
  }

  @BeforeEach
  void setEvaluator() throws Exception {
    this.problemID = "TEST";
    this.algorithmID = "test-algorithm";
    ProblemProvider.registerProblemProviders(new Class<?>[] {TestProblemProvider.class});

    DataNode ins = new DataNode("Instances");

    DataNode dn1 = new DataNode("test-instance-1");
    dn1.putValue("id", "test-instance-1");
    dn1.putValue("type", "resource");
    dn1.putValue("path", "");
    dn1.putValue("optimum", 2.0);
    dn1.putValue("random", 84.0);
    dn1.putValue("greedy", 42.0);
    dn1.putValue("size", 10);
    ins.putDataNode(dn1);

    DataNode dn2 = new DataNode("test-instance-2");
    dn2.putValue("id", "test-instance-2");
    dn2.putValue("type", "resource");
    dn2.putValue("path", "");
    dn2.putValue("optimum", 2.0);
    dn2.putValue("random", 40.0);
    dn2.putValue("greedy", 20.0);
    dn2.putValue("size", 100);
    ins.putDataNode(dn2);

    DataNode dn3 = new DataNode("test-instance-3");
    dn3.putValue("id", "test-instance-3");
    dn3.putValue("type", "resource");
    dn3.putValue("path", "");
    dn3.putValue("optimum", 1.0);
    dn3.putValue("random", 24.0);
    dn3.putValue("greedy", 12.0);
    dn3.putValue("size", 1000);
    ins.putDataNode(dn3);

    this.problemInfo = new ProblemInfo(this.problemID);
    this.problemInfo.putDataNode(ins);

    this.instancesInfo = new HashMap<>();
    this.instancesInfo.put("test-instance-1", 
        this.problemInfo.getProblemInstanceInfo("test-instance-1"));
    this.instancesInfo.put("test-instance-2", 
        this.problemInfo.getProblemInstanceInfo("test-instance-2"));
    this.instancesInfo.put("test-instance-3", 
        this.problemInfo.getProblemInstanceInfo("test-instance-3"));

    this.instanceIDs = List.of("test-instance-1", "test-instance-2", "test-instance-3");

    this.evaluator = new SingleAlgorithmConfigsEvolutionSubjectEvaluator(
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      this.problemID, 
      this.instanceIDs, 
      this.algorithmID, 
      0, 
      problemInfo, 
      instancesInfo);

    String[] paramNames1 = {
      "test-par-1-1",
      "test-par-1-2",
      "test-par-1-3",
      "test-par-1-4"
    };
    String[] paramNames2 = {
      "test-par-2-1",
      "test-par-2-2",
      "test-par-2-3",
      "test-par-2-4"
    };
    String[] paramNames3 = {
      "test-par-3-1",
      "test-par-3-2",
      "test-par-3-3",
      "test-par-3-4"
    };
    Double[] geneValues1 = {
      0.1,
      0.2,
      0.3,
      0.4,
    };
    Double[] geneValues2 = {
      0.2,
      0.4,
      0.6,
      0.8,
    };
    Double[] geneValues3 = {
      0.11,
      0.21,
      0.31,
      0.41,
    };

    this.subjects = new ArrayList<>() {
      {
        add(new SingleAlgorithmConfigsEvolutionSubject(paramNames1, geneValues1));
        add(new SingleAlgorithmConfigsEvolutionSubject(paramNames2, geneValues2));
        add(new SingleAlgorithmConfigsEvolutionSubject(paramNames3, geneValues3));

      }
    };
  }

  @Test
  void testCreateTaskList() throws Exception {
    Map<String, Map<String, Double>>subjectsObjValues = new HashMap<>();
    List<ExperimentTaskRequest> taskList = this.evaluator.createTaskList(
      this.instanceIDs.get(0), this.subjects, subjectsObjValues
    );
    
    String configID1 = this.subjects.get(0).getAlgorithmParams().hash();
    String configID2 = this.subjects.get(1).getAlgorithmParams().hash();
    String configID3 = this.subjects.get(2).getAlgorithmParams().hash();

    assertEquals(0, subjectsObjValues.keySet().size());

    assertEquals(3, taskList.size());

    assertEquals(taskList.get(0).getAlgorithmID(), this.algorithmID);
    assertEquals(taskList.get(1).getAlgorithmID(), this.algorithmID);
    assertEquals(taskList.get(2).getAlgorithmID(), this.algorithmID);

    assertEquals(taskList.get(0).getConfigID(), configID1);
    assertEquals(taskList.get(1).getConfigID(), configID2);
    assertEquals(taskList.get(2).getConfigID(), configID3);
  }

  @Test
  void testCalculateProblemScores() throws Exception {
    Map<String, Map<String, Double>> subjectsObjValues = new HashMap<>();
    String configID1 = this.subjects.get(0).getAlgorithmParams().hash();
    
    subjectsObjValues.put(configID1, new HashMap<>());
    subjectsObjValues.get(configID1).put(
        this.instanceIDs.get(0), 5.0);
    subjectsObjValues.get(configID1).put(
        this.instanceIDs.get(1), 6.0);
    subjectsObjValues.get(configID1).put(
        this.instanceIDs.get(2), 3.0);

    Map<String, Double> scores = this.evaluator.calculateProblemScores(subjectsObjValues);

    assertEquals(1, scores.size());

    assertEquals(0.81, scores.get(configID1), 0.01);
  }

  @Test
  void testCalculateRankTable() throws Exception {
    Map<String, Map<String, Double>> subjectsObjValues = new HashMap<>();
    String configID1 = this.subjects.get(0).getAlgorithmParams().hash();
    String configID2 = this.subjects.get(1).getAlgorithmParams().hash();
    String configID3 = this.subjects.get(2).getAlgorithmParams().hash();

    subjectsObjValues.put(configID1, new HashMap<>());
    subjectsObjValues.put(configID2, new HashMap<>());
    subjectsObjValues.put(configID3, new HashMap<>());

    subjectsObjValues.get(configID1).put(
        this.instanceIDs.get(0), 5.0);
    subjectsObjValues.get(configID2).put(
        this.instanceIDs.get(0), 6.0);
    subjectsObjValues.get(configID3).put(
        this.instanceIDs.get(0), 3.0);

    Map<String, Map<String, Integer>> rankTable = 
        this.evaluator.calculateRankTable(subjectsObjValues);

    assertEquals(3, rankTable.keySet().size());
    assertEquals(2, rankTable.get(configID1).get(instanceIDs.get(0)));
    assertEquals(3, rankTable.get(configID2).get(instanceIDs.get(0)));
    assertEquals(1, rankTable.get(configID3).get(instanceIDs.get(0)));
  }

  @Test
  void testCalculateWeightedRanks() throws Exception {
    String configID1 = this.subjects.get(0).getAlgorithmParams().hash();
    String configID2 = this.subjects.get(1).getAlgorithmParams().hash();
    String configID3 = this.subjects.get(2).getAlgorithmParams().hash();

    Map<String, Map<String, Integer>> rankTable = new HashMap<>();
    rankTable.put(configID1, new HashMap<>());
    rankTable.get(configID1).put(instanceIDs.get(0), 1);
    rankTable.get(configID1).put(instanceIDs.get(1), 3);
    rankTable.get(configID1).put(instanceIDs.get(2), 1);

    rankTable.put(configID2, new HashMap<>());
    rankTable.get(configID2).put(instanceIDs.get(0), 2);
    rankTable.get(configID2).put(instanceIDs.get(1), 2);
    rankTable.get(configID2).put(instanceIDs.get(2), 3);

    rankTable.put(configID3, new HashMap<>());
    rankTable.get(configID3).put(instanceIDs.get(0), 3);
    rankTable.get(configID3).put(instanceIDs.get(1), 1);
    rankTable.get(configID3).put(instanceIDs.get(2), 2);


    Map<String, Double> weightedRanks = evaluator.calculateWeightedRanks(rankTable);

    assertEquals(1.18, weightedRanks.get(configID1), 0.01);
    assertEquals(2.90, weightedRanks.get(configID2), 0.01);
    assertEquals(1.91, weightedRanks.get(configID3), 0.01);
  }
}
