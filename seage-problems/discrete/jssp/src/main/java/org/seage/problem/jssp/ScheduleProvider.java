package org.seage.problem.jssp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.aal.algorithm.Phenotype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleProvider {
  private static Logger logger = LoggerFactory.getLogger(ScheduleProvider.class.getName());
  
  /**
   * Method creates soluton with the use of greedy algorithm
   * 
   * @param jobs Problem instance definition
   * @param randomSeed Random seed.
   */
  public static JsspPhenotype createGreedySchedule(JobsDefinition jobs, long randomSeed) throws Exception {
    return null;
  }

  /**
   * Method creates random schedule
   * 
   * @params length Length of schedule
   * @params randomSeed Random seed
   */
  public static JsspPhenotype createRandomSchedule(int length, long randomSeed) throws Exception {
    Random rnd = new Random(randomSeed);

    Integer[] randSol = new Integer[length];

    for (int j = 0; j < length; j++)
      randSol[j] = j + 1;

    // Random permutation
    for(int j=0;j<length*2;j++) {
      int ix1=rnd.nextInt(length);
      int ix2=rnd.nextInt(length);
      int a = randSol[ix1];
      randSol[ix1] = randSol[ix2];
      randSol[ix2] = a;
    }

    return new JsspPhenotype(randSol);
  }
}
