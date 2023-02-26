/**
 * Class tests the ScoreCalculator class
 * 
 * @author David Omrai
 */

package org.seage.aal.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


public class ScoreCalculatorTest {

  @Test
  public void testCalculatingOptimalInstanceScore() throws Exception {
    assertEquals(1.0,
        ScoreCalculator.calculateInstanceScore(12.0, 42.0, 12.0), 0.1);
  }

  @Test
  public void testCalculatingGreedyInstanceScore() throws Exception {
    assertEquals(0.0,
        ScoreCalculator.calculateInstanceScore(12.0, 42.0, 42.0), 0.1);
  }

  @Test
  public void testCalculatingMidsectionInstanceScore() throws Exception {
    assertEquals(0.5,
        ScoreCalculator.calculateInstanceScore(12.0, 42.0,27.0), 0.1);
  }

  @Test
  public void testCalculatingBetterThanOptimumInstanceScore() throws Exception {
    assertThrows(Exception.class,
        () -> ScoreCalculator.calculateInstanceScore(12.0, 42.0, 11.0));
  }

  @Test
  public void testCalculatingWorseThanGreedyInstanceScore() throws Exception {
    assertEquals(0.0, ScoreCalculator.calculateInstanceScore(12.0, 42.0, 43.0),
        0.1);
  }

  @Test
  public void testCalculatingProblemScore() throws Exception {
    double[] instanceScores = {0.4, 0.2, 0.1, 0.6, 0.3};    
    List<Double> instanceSizes =  new ArrayList<>() {{
        add(10.0);
        add(11.0); 
        add(12.0);
        add(13.0); 
        add(15.0);
      }
    };

    double weightedScoresSum = 0.0;
    double insSizesSum = 0.0;

    for (int i = 0; i < instanceSizes.size(); i++) {
      weightedScoresSum += instanceSizes.get(i) * instanceScores[i];
      insSizesSum += instanceSizes.get(i);
    }

    double weightedMean = weightedScoresSum / insSizesSum;

    assertEquals(weightedMean, 
        ScoreCalculator.calculateProblemScore(instanceSizes, instanceScores));
  }

  @Test
  public void testCalculatingBasicMeanOfScores() throws Exception {
    double[] instanceScores = {0.4, 0.2, 0.1, 0.6, 0.3};    
    List<Double> instanceSizes =  new ArrayList<>() {{
        add(1.0);
        add(1.0); 
        add(1.0);
        add(1.0); 
        add(1.0);
      }
    };
    double mean = Arrays.stream(instanceScores).sum() / instanceScores.length;

    assertEquals(mean, ScoreCalculator.calculateProblemScore(instanceSizes, instanceScores));
  }

  @Test
  public void testCalculatingProblemScoreException() throws Exception {
    double[] instanceScores = {0.0, 0.0, 0.0, 0.0, 0.0};    
    List<Double> instanceSizes =  new ArrayList<>() {{
        add(0.0);
        add(0.0); 
        add(0.0);
        add(0.0); 
        add(0.0);
      }
    };

    assertThrows(ArithmeticException.class,
        () -> ScoreCalculator.calculateProblemScore(instanceSizes, instanceScores));
  
  }

  @Test
  public void testCalculateExperimentScore() throws Exception {
    List<Double> problemsScores = new ArrayList<>() {{
        add(10.0);
        add(5.0);
        add(3.0);
      }
    };

    assertEquals(6.0, ScoreCalculator.calculateExperimentScore(problemsScores), 0.1);
  }

  @Test
  public void testCalculateScoreDelta() throws Exception {
    assertEquals(6.0, ScoreCalculator.calculateScoreDelta(6.0, 12.0), 0.1);
  }

  /**
   * This test checks if the given initial score, 
   * which should be smaller than the last score results
   * in a negative delta score. (indication of worse score)
   * @throws Exception .
   */
  @Test
  public void testCalculateScoreDeltaNegative() throws Exception {
    assertEquals(-6.0, ScoreCalculator.calculateScoreDelta(12.0, 6.0), 0.1);
  }
}
