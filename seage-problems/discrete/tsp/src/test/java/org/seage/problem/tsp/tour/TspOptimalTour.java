package org.seage.problem.tsp.tour;

import org.seage.problem.tsp.TspPhenotype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TspOptimalTour {
  private static Logger log = LoggerFactory.getLogger(TspOptimalTour.class.getName());

  public static final String Name = "";
  public static final Integer[] OptimalTour = {};
  public static final int OptimalLength = 0;

  protected TspOptimalTour() {}

  public static TspPhenotype mirrorTour(TspPhenotype tour) throws Exception {
    Integer[] array = tour.getSolution();
    Integer[] result = new Integer[array.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = array[array.length - i - 1];
    }

    return new TspPhenotype(result);
  }

  public static TspPhenotype shiftTour(TspPhenotype tour, int offset) throws Exception {
    Integer[] array = tour.getSolution();
    Integer[] result = new Integer[array.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = array[(i + offset) % array.length];
    }

    return new TspPhenotype(result);
  }

  public static TspPhenotype applySwapMove(TspPhenotype tour, int[] move) throws Exception {
    Integer[] array = tour.getSolution();
    Integer[] result = array.clone();

    int a = result[move[0]];
    result[move[0]] = result[move[1]];
    result[move[1]] = a;

    return new TspPhenotype(result);
  }

  public static void printTour(TspPhenotype tour) {
    for (int i = 0; i < tour.getSolution().length; i++) {
      log.debug(tour.getSolution()[i] + " ");
    }

    log.debug(" ");

  }
}
