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
    // 
    City[] cities = readCities("instances/berlin52.tsp");
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);

    TspOptimalTourBerlin52 tour = new TspOptimalTourBerlin52();
    TspSolution s = new TspSolution();
    s.setTour(tour.OptimalTour);
    assertEquals(tour.OptimalLength, fn.evaluate(s, null)[0]);
  }

  @Test
  void testObjectiveFunctionForPcb442() throws Exception{
    City[] cities = readCities("instances/pcb442.tsp");
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);

    TspOptimalTourPcb442 tour = new TspOptimalTourPcb442();
    TspSolution s = new TspSolution();
    s.setTour(tour.OptimalTour);
    assertEquals(tour.OptimalLength, fn.evaluate(s, null)[0]);
  }

  @Test
  void testObjectiveFunctionForRm() throws Exception{
    City[] cities = readCities("test-instances/rm4.tsp");
    TspObjectiveFunction fn = new TspObjectiveFunction(cities);

    TspSolution s = new TspSolution();
    
    s.setTour(new Integer[] {1,2,3,4});
    assertEquals(4, fn.evaluate(s, null)[0]);

    s.setTour(new Integer[] {1,3,4,2});
    assertEquals(4.8284, fn.evaluate(s, null)[0], 0.0001);
  }
}
