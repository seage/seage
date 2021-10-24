package org.seage.problem.jssp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
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
  public static JsspPhenotype createGreedySchedule(JobsDefinition jobs) throws Exception {
    int numJobs = jobs.getJobsCount();
    int numMachines = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] greedySolution = new Integer[numJobs*numMachines];

    int[] numFinJobOpers = new int[numJobs];
    for (int i = 0; i < numJobs; i++)
      numFinJobOpers[i] = 0;

    JsspPhenotypeEvaluator jsspPhenoEval = new JsspPhenotypeEvaluator(jobs);

    double tmpMakeSpan = 0;
    double tmpMinMakeSpan = 0;

    int nextJobId = 0;

    for (int i = 0; i < greedySolution.length; i++) {
      
      for (int jobId = 1; jobId <= numJobs; jobId++) {
        if ( numFinJobOpers[jobId-1] >= numMachines )
          continue;
        
        greedySolution[i] = jobId;

        tmpMakeSpan = jsspPhenoEval
          .evaluateSchedule(Arrays.copyOfRange(greedySolution, 0, i+1))[0];
        
        if (tmpMakeSpan == 0 || tmpMakeSpan < tmpMinMakeSpan) {
          tmpMinMakeSpan = tmpMakeSpan;
          nextJobId = jobId;
        }
      }

      // Strore the next new solution
      greedySolution[i] = nextJobId;
      numFinJobOpers[nextJobId-1] += 1;
    }
    
    return new JsspPhenotype(greedySolution);
  }

  /**
   * Method creates random schedule
   * 
   * @params length Length of schedule
   * @params randomSeed Random seed
   */
  public static JsspPhenotype createRandomSchedule(JobsDefinition jobs, long randomSeed) throws Exception {
    Random rnd = new Random(randomSeed);

    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] randSol = new Integer[numJobs*numOpers];


    int i = 0;
    for (int jobIndex = 0; jobIndex < numJobs; jobIndex++)
      for (int operIndex = 0; operIndex < numOpers; operIndex++)
        randSol[i++] = jobIndex + 1;

    // Random permutation
    for(int j=0;j<randSol.length*2;j++) {
      int ix1=rnd.nextInt(randSol.length);
      int ix2=rnd.nextInt(randSol.length);
      int a = randSol[ix1];
      randSol[ix1] = randSol[ix2];
      randSol[ix2] = a;
    }

    return new JsspPhenotype(randSol);
  }
}
