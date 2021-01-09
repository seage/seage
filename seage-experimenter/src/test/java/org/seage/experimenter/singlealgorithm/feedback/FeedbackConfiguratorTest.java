package org.seage.experimenter.singlealgorithm.feedback;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.config.FeedbackConfigurator;

public class FeedbackConfiguratorTest {

  // @BeforeAll
  public static void setUpBeforeClass() throws Exception {
  }

  // @Test
  public void testPrepareConfigs() throws Exception {
    FeedbackConfigurator fc = new FeedbackConfigurator();

    ProblemInfo pi = new ProblemInfo("TestProblem");
    pi.putValue("id", "TestProblem");
    pi.putDataNode(new DataNode("Instances"));
    pi.putDataNode(new DataNode("Algorithms"));

    DataNode ii = new DataNode("Instance");
    ii.putValue("id", "Instance01");
    ii.putValue("name", "Instance01");
    ii.putValue("type", "");
    ii.putValue("path", "");
    pi.getDataNode("Instances").putDataNode(ii);

    DataNode ai = new DataNode("Algorithm");
    ai.putValue("id", "Algorithm01");
    pi.getDataNode("Algorithms").putDataNode(ai);

    ProblemConfig[] pc = fc.prepareConfigs(pi, "Instance01", "Algorithm01", 100);
    assertTrue(pc.length == 100);

  }

}
