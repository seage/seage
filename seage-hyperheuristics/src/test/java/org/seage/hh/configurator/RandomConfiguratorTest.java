package org.seage.hh.experiment.configurator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.hh.configurator.RandomConfigurator;

public class RandomConfiguratorTest {
  private ProblemInfo pi;

  @BeforeEach
  void setup() throws Exception {
    pi = new ProblemInfo("TestProblem");
    pi.putValue("id", "TestProblem");
    pi.putDataNode(new DataNode("Instances"));
    pi.putDataNode(new DataNode("Algorithms"));

    DataNode ii = new DataNode("Instance");
    ii.putValue("id", "Instance01");
    ii.putValue("name", "Instance01");
    ii.putValue("type", "file");
    ii.putValue("path", "test");
    pi.getDataNode("Instances").putDataNode(ii);

    DataNode ai = new DataNode("Algorithm");
    ai.putValue("id", "Algorithm01");

    DataNode param1 = new DataNode("Parameter");
    param1.putValue("name", "param1");
    param1.putValue("min", "0");
    param1.putValue("max", "100");
    param1.putValue("init", "50");
    ai.putDataNode(param1);

    DataNode param2 = new DataNode("Parameter");
    param2.putValue("name", "param2");
    param2.putValue("min", "0");
    param2.putValue("max", "1");
    param2.putValue("init", "0.5");
    ai.putDataNode(param2);

    pi.getDataNode("Algorithms").putDataNode(ai);
  }

  @Test
  void testRandomConfigurator() throws Exception {
    RandomConfigurator configurator = new RandomConfigurator();

    ProblemConfig[] pc = configurator.prepareConfigs(pi, "Instance01", "Algorithm01", 100);
    assertEquals(100, pc.length);
    for(DataNode c : pc) {
      Double param1 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param1");
      assertTrue(param1 >= 0, "Parameter must be greater than 0");
      assertTrue(param1 <= 100, "Parameter must be smaller than 100");
      Double param2 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param2");
      assertTrue(param2 >= 0, "Parameter must be greater than 0");
      assertTrue(param2 <= 1, "Parameter must be smaller than 1");
    }    
  }
}
