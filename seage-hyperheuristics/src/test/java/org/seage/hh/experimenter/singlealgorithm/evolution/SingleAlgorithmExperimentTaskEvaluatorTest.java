package org.seage.hh.experimenter.singlealgorithm.evolution;

import static org.junit.jupiter.api.Assertions.assertNull;

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
  FeedbackConfigurator feedbackConfigurator;
  List<SingleAlgorithmExperimentTaskSubject> subjects;


  private Void reportFn(ExperimentTaskRecord experiment) {
    // todo
    return null;
  }

  @BeforeEach
  void setEvaluator() throws Exception {
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

    this.problemInfo = new ProblemInfo("TEST");
    this.problemInfo.putDataNode(ins);

    this.instancesInfo = new HashMap<>();
    this.instancesInfo.put("test-instance-1", 
        this.problemInfo.getProblemInstanceInfo("test-instance-1"));
    this.instancesInfo.put("test-instance-2", 
        this.problemInfo.getProblemInstanceInfo("test-instance-2"));
    this.instancesInfo.put("test-instance-3", 
        this.problemInfo.getProblemInstanceInfo("test-instance-3"));
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
    // TODO
  }
}
