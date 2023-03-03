package org.seage.problem.tsp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.tsp.tour.TspOptimalTour;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;
import org.seage.problem.tsp.tour.TspOptimalTourPcb442;

public class TspPhenotypeTest {
  private IProblemProvider<?> _provider;

  @BeforeEach
  public void initEvaluator() throws Exception {
    ProblemProvider.registerProblemProviders(new Class<?>[] {TspProblemProvider.class});
    _provider = ProblemProvider.getProblemProviders().get("TSP");
  }

  @Test
  public void testPhenotypeDeserialization() throws Exception {
    // TspOptimalTour tour = new TspOptimalTourBerlin52();
    ProblemInstance instance = _provider.initProblemInstance(
        _provider.getProblemInfo().getProblemInstanceInfo(TspOptimalTourBerlin52.Name));
    TspPhenotypeEvaluator evaluator =
        new TspPhenotypeEvaluator(_provider.getProblemInfo(), (TspProblemInstance) instance);

    TspPhenotype p1 = new TspPhenotype(TspOptimalTourBerlin52.OptimalTour);
    String t1 = p1.toText();
    assertNotNull(t1);
    double[] v1 = evaluator.evaluate(p1);
    assertEquals(TspOptimalTourBerlin52.OptimalLength, v1[0]);

    TspPhenotype p2 = new TspPhenotype(new Integer[TspOptimalTourBerlin52.OptimalTour.length]);
    p2.fromText(t1);

    String t2 = p2.toText();
    assertEquals(t1, t2);

    assertNotNull(p2.getSolution()[0]);
    double[] v2 = evaluator.evaluate(p2);

    assertArrayEquals(v1, v2);
  }

  @Test
  public void testEvaluate() throws Exception {
    testEvaluateTour(
        TspOptimalTourBerlin52.Name, 
        TspOptimalTourBerlin52.OptimalLength,
        TspOptimalTourBerlin52.OptimalTour);
    testEvaluateTour(
          TspOptimalTourPcb442.Name, 
          TspOptimalTourPcb442.OptimalLength,
          TspOptimalTourPcb442.OptimalTour);
  }

  private <T extends TspOptimalTour> void testEvaluateTour(String name, int optimum,
      Integer[] optimalTour) throws Exception {
    ProblemInstance instance =
        _provider.initProblemInstance(_provider.getProblemInfo().getProblemInstanceInfo(name));
    TspPhenotypeEvaluator evaluator =
        new TspPhenotypeEvaluator(_provider.getProblemInfo(), (TspProblemInstance) instance);

    TspPhenotype s = new TspPhenotype(optimalTour);
    // TspOptimalTour.printTour(s);
    assertEquals(optimum, (int) evaluator.evaluate(s)[0]);

    s = TspOptimalTour.mirrorTour(s);
    // TspOptimalTour.printTour(s);
    assertEquals(optimum, (int) evaluator.evaluate(s)[0]);

    s = TspOptimalTour.shiftTour(s, 10);
    // TspOptimalTour.printTour(s);
    assertEquals(optimum, (int) evaluator.evaluate(s)[0]);

    s = TspOptimalTour.mirrorTour(TspOptimalTour.shiftTour(s, 10));
    // TspOptimalTour.printTour(s);
    assertEquals(optimum, (int) evaluator.evaluate(s)[0]);

    s = TspOptimalTour.applySwapMove(s, new int[] {0, 1});
    s = TspOptimalTour.applySwapMove(s, new int[] {10, 12});
    s = TspOptimalTour.applySwapMove(s, new int[] {6, 5});
    s = TspOptimalTour.applySwapMove(s, new int[] {4, 8});
    s = TspOptimalTour.applySwapMove(s, new int[] {3, 7});
    s = TspOptimalTour.applySwapMove(s, new int[] {0, 1});
    s = TspOptimalTour.applySwapMove(s, new int[] {10, 12});
    s = TspOptimalTour.applySwapMove(s, new int[] {6, 5});
    s = TspOptimalTour.applySwapMove(s, new int[] {4, 8});
    s = TspOptimalTour.applySwapMove(s, new int[] {3, 7});
    assertEquals(optimum, (int) evaluator.evaluate(s)[0]);
  }
}
