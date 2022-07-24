/**
 * Class tests the ScoreCalculator class
 * 
 * @author David Omrai
 */

package org.seage.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
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

    double numerator = 0.0;
    double denominator = 0.0;

    for (int i = 0; i < instanceSizes.size(); i++) {
      numerator += instanceSizes.get(i) * instanceScores[i];
      denominator += instanceSizes.get(i);
    }

    double weightedMean = numerator / denominator;

    assertEquals(weightedMean, 
        ScoreCalculator.calculateProblemScore(instanceSizes, instanceScores));
  }
}
