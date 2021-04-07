package org.seage.aal.problem;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.seage.data.DataNode;

public class ProblemScoreCalculatorTest {
  protected ProblemScoreCalculator problemScoreCalculator;
  
  @BeforeAll
  static void init() {
    ProblemProvider.providerClasses =
        new Class<?>[] {TestProblemProvider.class};
  }

  @Test
  public void testCalculatingOptimalInstanceScore() throws Exception {
    DataNode dn = new DataNode("test-instance");
    dn.putValue("id", "test-instance");
    dn.putValue("type", "resource");
    dn.putValue("path", "");
    dn.putValue("optimum", 2.0);
    dn.putValue("random", 42.0);
    DataNode ins = new DataNode("Instances");
    ins.putDataNode(dn);
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("optimum");
   
    assertEquals(
        1.0, problemScoreCalculator.calculateInstanceScore("test-instance", optimum), 0.1);
  }


  @Test
  public void testCalculatingRandomInstanceScore() throws Exception {
    DataNode dn = new DataNode("test-instance");
    dn.putValue("id", "test-instance");
    dn.putValue("type", "resource");
    dn.putValue("path", "");
    dn.putValue("optimum", 2.0);
    dn.putValue("random", 42.0);
    DataNode ins = new DataNode("Instances");
    ins.putDataNode(dn);
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double random = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("random");
    
    assertEquals(
        0.0, problemScoreCalculator.calculateInstanceScore("test-instance", random), 0.1);
  }

  @Test
  public void testCalculatingMidsectionInstanceScore() throws Exception {
    DataNode dn = new DataNode("test-instance");
    dn.putValue("id", "test-instance");
    dn.putValue("type", "resource");
    dn.putValue("path", "");
    dn.putValue("optimum", 2.0);
    dn.putValue("random", 42.0);
    DataNode ins = new DataNode("Instances");
    ins.putDataNode(dn);
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("optimum");
    double random = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("random");
    double midsection = (random - optimum) / 2;

    assertEquals(
        0.5, problemScoreCalculator.calculateInstanceScore("test-instance", midsection), 0.1);
  }

  @Test
  public void testCalculatingBetterThanOptimumInstanceScore() throws Exception {
    DataNode dn = new DataNode("test-instance");
    dn.putValue("id", "test-instance");
    dn.putValue("type", "resource");
    dn.putValue("path", "");
    dn.putValue("optimum", 2.0);
    dn.putValue("random", 42.0);
    DataNode ins = new DataNode("Instances");
    ins.putDataNode(dn);
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("optimum");
    
    assertThrows(
        Exception.class, 
        () -> problemScoreCalculator.calculateInstanceScore("test-instance", optimum - 1));
  }

  @Test
  public void testCalculatingWorseThanRandomInstanceScore() throws Exception {
    DataNode dn = new DataNode("test-instance");
    dn.putValue("id", "test-instance");
    dn.putValue("type", "resource");
    dn.putValue("path", "");
    dn.putValue("optimum", 2.0);
    dn.putValue("random", 42.0);
    DataNode ins = new DataNode("Instances");
    ins.putDataNode(dn);
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double random = problemInfo.getProblemInstanceInfo("test-instance").getValueDouble("random");
  
    assertEquals(0.0, 
        problemScoreCalculator.calculateInstanceScore("test-instance", random + 1), 0.1);
  }

  @Test
  public void testCalculatingProblemScore() throws Exception {
    String[] names = {"1", "2", "3", "4", "5"};
    double[] values = {0.4, 0.2, 0.1, 0.6, 0.3};
    double[] weights = {10.0, 11.0, 12.0, 13.0, 15.0};

    DataNode ins = new DataNode("Instances");

    for (int i = 0; i < names.length; i++) {
      DataNode dn = new DataNode(names[i]);
      dn.putValue("id", names[i]);
      dn.putValue("type", "resource");
      dn.putValue("path", "");
      dn.putValue("optimum", 2.0);
      dn.putValue("random", 42.0);
      dn.putValue("size", weights[i]);

      ins.putDataNode(dn);
    }
    
    ProblemInfo problemInfo = new ProblemInfo("TEST");
    problemInfo.putDataNode(ins);


    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);


    int arrayLength = names.length;

    double numerator = 0.0;
    double denominator = 0.0;
    for (int i = 0; i < arrayLength; i++) {
      // Weight
      double instanceSize = problemInfo
          .getProblemInstanceInfo(names[i]).getValueDouble("size");
      
      numerator += instanceSize * values[i];
      denominator += instanceSize;
    }

    double weightedMean = numerator / denominator;

    assertEquals(weightedMean, problemScoreCalculator.calculateProblemScore(names, values), 0.1);
  }
}
