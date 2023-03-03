package org.seage.problem.tsp.tabusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;
import org.seage.problem.tsp.tour.TspOptimalTourPcb442;

public class TspObjectiveFunctionTest {

  private City[] readCities(String relPath) throws IOException {
    String path = String.format("/org/seage/problem/tsp/%s", relPath);
    City[] cities = null;
    try (InputStream stream = getClass().getResourceAsStream(path)) {
      cities = CityProvider.readCities(stream);
    }
    return cities;
  }

  @Test
  void testObjectiveFunctionForBerlin52() throws Exception{
    City[] cities = readCities(String.format("instances/%s.tsp", TspOptimalTourBerlin52.Name));
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);
    
    TspSolution s = new TspSolution();
    s.setTour(TspOptimalTourBerlin52.OptimalTour);
    assertEquals(TspOptimalTourBerlin52.OptimalLength, fn.evaluate(s, null)[0]);
  }

  @Test
  void testObjectiveFunctionForPcb442() throws Exception{
    City[] cities = readCities(String.format("instances/%s.tsp", TspOptimalTourPcb442.Name));
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);
    
    TspSolution s = new TspSolution();
    s.setTour(TspOptimalTourPcb442.OptimalTour);
    assertEquals(TspOptimalTourPcb442.OptimalLength, fn.evaluate(s, null)[0]);
  }

  @Test
  void testObjectiveFunctionForRm() throws Exception{
    City[] cities = readCities("test-instances/rm4.tsp");
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);

    TspSolution s = new TspSolution();
    
    s.setTour(new Integer[] {1,2,3,4});
    assertEquals(4, fn.evaluate(s, null)[0]);

    s.setTour(new Integer[] {1,3,4,2});
    assertEquals(4.0, fn.evaluate(s, null)[0], 0.0001);
  }
}
