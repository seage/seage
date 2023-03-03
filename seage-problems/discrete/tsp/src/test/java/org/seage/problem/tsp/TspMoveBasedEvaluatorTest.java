package org.seage.problem.tsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.seage.problem.tsp.tour.TspOptimalTour;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;

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
  void testEvaluator() throws Exception {
    City[] cities = readCities("instances/berlin52.tsp");
    TspMoveBasedEvaluator eval = new TspMoveBasedEvaluator(cities);
    
    TspOptimalTourBerlin52 tour = new TspOptimalTourBerlin52();

    double[] v = eval.evaluate(TspOptimalTour.OptimalTour, null, 0);
    assertEquals(tour.OptimalLength, v[0]);
  }
}
