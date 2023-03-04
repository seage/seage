package org.seage.problem.tsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;
import org.seage.problem.tsp.tour.TspOptimalTourPcb442;

public class TspMoveBasedEvaluatorTest {
  private City[] readCities(String relPath) throws IOException {
    String path = String.format("/org/seage/problem/tsp/%s", relPath);
    City[] cities = null;
    try (InputStream stream = getClass().getResourceAsStream(path)) {
      cities = CityProvider.readCities(stream);
    }
    return cities;
  }

  @Test
  void testEvaluatorForBerlin52() throws Exception {
    City[] cities = readCities("instances/berlin52.tsp");
    TspMoveBasedEvaluator eval = new TspMoveBasedEvaluator(cities);
    double[] v = eval.evaluate(TspOptimalTourBerlin52.OptimalTour, null, 0);
    assertEquals(TspOptimalTourBerlin52.OptimalLength, v[0]);
  }

  @Test
  void testEvaluatorForPcb442() throws Exception {
    City[] cities = readCities("instances/pcb442.tsp");
    TspMoveBasedEvaluator eval = new TspMoveBasedEvaluator(cities);
    double[] v = eval.evaluate(TspOptimalTourPcb442.OptimalTour, null, 0);
    assertEquals(TspOptimalTourPcb442.OptimalLength, v[0]);
  }
}

