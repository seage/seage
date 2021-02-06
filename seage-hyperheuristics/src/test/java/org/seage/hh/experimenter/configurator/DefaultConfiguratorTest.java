package org.seage.hh.experimenter.configurator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

public class DefaultConfiguratorTest {
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

    DataNode param3 = new DataNode("Parameter");
    param3.putValue("name", "param3");
    param3.putValue("min", "0");
    param3.putValue("max", "1");
    param3.putValue("init", "0.99");
    ai.putDataNode(param3);

    DataNode param4 = new DataNode("Parameter");
    param4.putValue("name", "param4");
    param4.putValue("min", "0");
    param4.putValue("max", "1");
    param4.putValue("init", "0.01");
    ai.putDataNode(param4);

    pi.getDataNode("Algorithms").putDataNode(ai);
  }

  @Test
  void testDefaultConfigurator() throws Exception {
    DefaultConfigurator configurator = new DefaultConfigurator(0.1);

    ProblemConfig[] pc = configurator.prepareConfigs(pi, "Instance01", "Algorithm01", 100);
    assertEquals(100, pc.length);
    
    Double param1 = pc[0].getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param1");
    assertEquals(50, param1);
    Double param2 = pc[0].getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param2");      
    assertEquals(0.5, param2);
    Double param3 = pc[0].getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param3");      
    assertEquals(0.99, param3);
    Double param4 = pc[0].getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param4");      
    assertEquals(0.01, param4);
    
    for(DataNode c : pc) {
      param1 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param1");
      assertTrue(param1 >= 40, "Parameter must be greater than 40, got: " + param1);
      assertTrue(param1 <= 60, "Parameter must be smaller than 60, got: " + param1);
      
      param2 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param2");      
      assertTrue(param2 >= 0.40, "Parameter must be greater than 0.40, got: " + param2);
      assertTrue(param2 <= 0.60, "Parameter must be smaller than 0.60, got: " + param2);

      param3 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param3");      
      assertTrue(param3 >= 0.8, "Parameter must be greater than 0.8, got: " + param3);
      assertTrue(param3 <= 1.0, "Parameter must be smaller than 1.0, got: " + param3);
      
      param4 = c.getDataNodeById("Algorithm01").getDataNode("Parameters").getValueDouble("param4");      
      assertTrue(param4 >= 0, "Parameter must be greater than 0.0, got: " + param4);
      assertTrue(param4 <= 0.11, "Parameter must be smaller than 0.11, got: " + param4);
    }    
  }
}
