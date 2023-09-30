package org.seage.hh.experimenter.singlealgorithm.evolution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.aal.problem.TestProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.FeedbackConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

/**
 * .
 */
public class SingleAlgorithmExperimentTaskEvaluatorTest {
  SingleAlgorithmExperimentTaskEvaluator evaluator;
  ProblemInfo problemInfo;
  HashMap<String, ProblemInstanceInfo> instancesInfo;
  List<String> instanceIDs;
  String problemID;
  String algorithmID;
  FeedbackConfigurator feedbackConfigurator;
  List<SingleAlgorithmExperimentTaskSubject> subjects;
  List<String> configIDs;


  private Void reportFn(ExperimentTaskRecord experiment) {
    // todo
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

    this.instanceIDs = new ArrayList<String>() {
      {
        add("test-instance-1");
        add("test-instance-2");
        add("test-instance-3");
      }
    };

    this.evaluator = new SingleAlgorithmExperimentTaskEvaluator(
      UUID.fromString("16578d4d-9ae4-4b3f-bcf3-7e7ce4737204"), 
      this.problemID, 
      this.instanceIDs, 
      this.algorithmID, 
      0, 
      problemInfo, 
      instancesInfo, 
      this::reportFn);

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
        add(new SingleAlgorithmExperimentTaskSubject(paramNames1, geneValues1));
        add(new SingleAlgorithmExperimentTaskSubject(paramNames2, geneValues2));
        add(new SingleAlgorithmExperimentTaskSubject(paramNames3, geneValues3));

      }
    };
  }

  @Test
  void testRankSubjectsObjValue() throws Exception {
    // TODO    
  }

  @Test 
  void testGetProblemScore() throws Exception {
    // TODO
    assertNull(null);
  }

  @Test
  void testSetSubjectsConfigScore() throws Exception {
    // TODO
  }

  @Test
  void testCreateTaskList() throws Exception {
    List<ExperimentTaskRequest> taskList = this.evaluator.createTaskList(
        subjects, this.instanceIDs.get(0));
    
    String configID1 = this.evaluator.subjectHashToConfigIDMap.get(
        this.subjects.get(0).hashCode());
    String configID2 = this.evaluator.subjectHashToConfigIDMap.get(
        this.subjects.get(1).hashCode());
    String configID3 = this.evaluator.subjectHashToConfigIDMap.get(
        this.subjects.get(2).hashCode());

    assertTrue(this.evaluator.configCache.containsKey(configID1));
    assertTrue(this.evaluator.configCache.containsKey(configID2));
    assertTrue(this.evaluator.configCache.containsKey(configID3));

    assertEquals(3, taskList.size());

    assertEquals(taskList.get(0).getAlgorithmID(), this.algorithmID);
    assertEquals(taskList.get(1).getAlgorithmID(), this.algorithmID);
    assertEquals(taskList.get(2).getAlgorithmID(), this.algorithmID);

    assertEquals(taskList.get(0).getConfigID(), configID1);
    assertEquals(taskList.get(1).getConfigID(), configID2);
    assertEquals(taskList.get(2).getConfigID(), configID3);
  }
}
