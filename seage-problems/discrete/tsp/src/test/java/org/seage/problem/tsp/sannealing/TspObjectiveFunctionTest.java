package org.seage.problem.tsp.sannealing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;
import org.seage.problem.tsp.tour.TspOptimalTour;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;

public class TspObjectiveFunctionTest {
  private TspOptimalTour _optimalTour;
  private TspObjectiveFunction _objFunc;
  private City[] _cities;

  @BeforeEach
  public void setUp() throws Exception {

    _optimalTour = new TspOptimalTourBerlin52();
    // _optimalTour = new TspOptimalTourPcb442();

    IProblemProvider<?> provider = ProblemProvider.getProblemProviders().get("TSP");
    assertNotNull(provider);
    TspProblemInstance instance = (TspProblemInstance) provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo(_optimalTour.Name));

    _cities = instance.getCities();
    _objFunc = new TspObjectiveFunction(_cities);
  }

  @Test
  public void testEvaluate() throws Exception {
    testEvaluateSolution(null);
    testEvaluateSolution(new int[] { 0, 1 });
    testEvaluateSolution(new int[] { 0, _cities.length - 1 });
    testEvaluateSolution(new int[] { 5, 6 });
    testEvaluateSolution(new int[] { 5, 10 });
  }

  private void testEvaluateSolution(int[] move) throws Exception {
    TspSolution s = new TspGreedySolution(_cities);
    s.setTour(_optimalTour.OptimalTour.clone());

    if (move != null) {
      int a = s.getTour()[move[0]];
      s.getTour()[move[0]] = s.getTour()[move[1]];
      s.getTour()[move[1]] = a;

      s.setObjectiveValue(_objFunc.getObjectiveValue(s));
    }

    double o = _objFunc.evaluate(s, move)[0];
    assertEquals(_optimalTour.OptimalLength, (int) o);
  }

  // @Test
  // public void testGetObjectiveValue() {
  // fail("Not yet implemented");
  // }

}
