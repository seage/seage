package org.seage.aal.problem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.seage.aal.problem.ProblemInfo;

public class ProblemScoreCalculatorTest {
  protected ProblemScoreCalculator problemScoreCalculator;

  /**
   * Method crates new instance of ProblemScoreCalculator.
   * @param problemName Name of a problem.
   */
  public void setUp(String problemName) {
    try {
      ProblemInfo problemInfo = new ProblemInfo(problemName);
      problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
    } catch (Exception ex) {
      fail(ex);
    }
  }

  @Test
  public void testCalculatingOptimalInstanceScore() {

  }

  @Test
  public void testCalculatingRandomInstanceScore() {

  }

  @Test
  public void testCalculatingMidsectionInstanceScore() {

  }

  @Test
  public void testCalculatingBetterThanOptimumInstanceScore() {

  }

  @Test
  public void testCalculatingWorseThanRandomInstanceScore() {

  }

  public void testCalculatingProblemScore() {

  }
}
