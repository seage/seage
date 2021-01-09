package org.seage.aal.algorithm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;

public class AlgorithmAdapterTestBase<S> {

  protected IAlgorithmAdapter<TestPhenotype, S> algAdapter;
  protected AlgorithmParams algParams;
  protected AlgorithmReport algReport;

  protected static final int NUM_SOLUTIONS = 10;
  protected static final int SOLUTION_LENGTH = 100;

  public void setAlgParameters(AlgorithmParams params) throws Exception {
    this.algParams = params;
  }

  /** Test method testAlgorithm().*/
  public void testAlgorithm() throws Exception {
    TestPhenotype[] p1 = createTestPhenotypeSolutions();
    this.algAdapter.solutionsFromPhenotype(p1);
    this.algAdapter.startSearching(this.algParams);
    TestPhenotype[] p2 = this.algAdapter.solutionsToPhenotype();
    assertEquals(p1.length, p2.length);
  }

  /** Test method testAlgorithmWithParamsNull().*/
  public void testAlgorithmWithParamsNull() throws Exception {
    assertThrows(Exception.class, () -> {
      this.algParams = null;
      testAlgorithm();
    });
  }

  /** Test method testAlgorithmWithParamsAtZero().*/
  public void testAlgorithmWithParamsAtZero() throws Exception {
    this.algAdapter.solutionsFromPhenotype(createTestPhenotypeSolutions());
    this.algAdapter.startSearching(this.algParams);
  }

  /** Test method testAsyncRunning().*/
  public void testAsyncRunning() throws Exception {
    this.algAdapter.solutionsFromPhenotype(createTestPhenotypeSolutions());
    this.algAdapter.startSearching(this.algParams, true);
    assertTrue(this.algAdapter.isRunning());

    this.algAdapter.stopSearching();

    assertFalse(this.algAdapter.isRunning());
  }

  /** Test method testReport().*/
  public void testReport() throws Exception {
    testAlgorithm();
    algReport = this.algAdapter.getReport();
    assertNotNull(algReport);
    assertTrue(this.algReport.containsNode("Parameters"));
    assertTrue(this.algReport.containsNode("Log"));
    assertTrue(this.algReport.containsNode("Statistics"));

    for (String attName : this.algReport.getDataNode("Parameters").getValueNames()) {
      assertEquals(this.algParams.getValue(attName), 
          this.algReport.getDataNode("Parameters").getValue(attName));
    }

    DataNode stats = this.algReport.getDataNode("Statistics");
    assertTrue(stats.getValueInt("numberOfIter") > 1);
    assertTrue(stats.getValueDouble("initObjVal") > 0);
    assertTrue(stats.getValueInt("avgObjVal") > 0);
    assertTrue(stats.getValueInt("bestObjVal") > 0);
    assertFalse(stats.getValueInt("initObjVal") < stats.getValueInt("bestObjVal"));
    assertTrue((stats.getValueInt("initObjVal") != stats.getValueInt("bestObjVal"))
        || (stats.getValueInt("numberOfNewSolutions") == 1));
    assertTrue(stats.getValueInt("bestObjVal") == stats.getValueDouble("initObjVal")
        || stats.getValueInt("lastIterNumberNewSol") > 1);
    assertTrue(stats.getValueInt("numberOfNewSolutions") > 0 
        || stats.getValueInt("lastIterNumberNewSol") == 0);
    assertTrue(stats.getValueInt("lastIterNumberNewSol") > 1 
        || stats.getValueInt("numberOfNewSolutions") == 1);
  }

  private TestPhenotype[] createTestPhenotypeSolutions() {
    Random rnd = new Random(4);
    TestPhenotype[] solutions = new TestPhenotype[NUM_SOLUTIONS];

    for (int i = 0; i < NUM_SOLUTIONS; i++) {
      Integer[] array = new Integer[SOLUTION_LENGTH];
      for (int j = 0; j < SOLUTION_LENGTH; j++) {
        array[j] = j + 1;
      }

      for (int j = 0; j < SOLUTION_LENGTH; j++) {
        int ix1 = rnd.nextInt(SOLUTION_LENGTH);
        int ix2 = rnd.nextInt(SOLUTION_LENGTH);
        Integer a = array[ix1];
        array[ix1] = array[ix2];
        array[ix2] = a;
      }
      solutions[i] = new TestPhenotype(array);
    }

    return solutions;

  }
}
