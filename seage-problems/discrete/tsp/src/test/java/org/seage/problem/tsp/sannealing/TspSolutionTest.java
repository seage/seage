package org.seage.problem.tsp.sannealing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;
import org.seage.problem.tsp.TspProblemProvider;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;

public class TspSolutionTest {
  private City[] _cities;

  @BeforeEach
  public void setUp() throws Exception {
    ProblemProvider.registerProblemProviders(new Class<?>[] { TspProblemProvider.class });

    IProblemProvider<?> provider = ProblemProvider.getProblemProviders().get("TSP");
    TspProblemInstance instance = (TspProblemInstance) provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(TspOptimalTourBerlin52.Name));

    _cities = instance.getCities();
  }

  @Test
  public void testTourValidity() throws Exception {
    testTourValidity(new TspRandomSolution(_cities.length));
    testTourValidity(new TspSortedSolution(_cities.length));
    testTourValidity(new TspGreedySolution(_cities));
  }

  private void testTourValidity(TspSolution solution) {
    assertEquals(TspOptimalTourBerlin52.OptimalTour.length, solution.getTour().length);
    Arrays.sort(solution.getTour());
    assertNotSame(0, solution.getTour()[0]);
    assertEquals(1, (int) solution.getTour()[0]);
    assertEquals(TspOptimalTourBerlin52.OptimalTour.length, (int) solution.getTour()[TspOptimalTourBerlin52.OptimalTour.length - 1]);

    for (int i = 0; i < TspOptimalTourBerlin52.OptimalTour.length - 1; i++) {
      assertEquals((int) solution.getTour()[i], solution.getTour()[i + 1] - 1);
    }
  }

}
