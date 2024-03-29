package org.seage.problem.jsp;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspScheduleProvider {
  private static Logger log = LoggerFactory.getLogger(JspScheduleProvider.class.getName());
  
  /**
   * Method creates soluton with the use of greedy algorithm.
   * 
   * @param jobs Problem instance definition
   * @param jspPhenoEval Phenotype evaluator
   */
  public static JspPhenotype createGreedySchedule(
      JspPhenotypeEvaluator jspPhenoEval, JspJobsDefinition jobs) throws Exception {
    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] greedySolution = new Integer[numJobs * numOpers];

    int[] numFinJobOpers = new int[numJobs];
    for (int i = 0; i < numJobs; i++) {
      numFinJobOpers[i] = 0;
    }

    double tmpMakeSpan = 0;
    double tmpMinMakeSpan = 0;

    int nextJobId = 0;
    Random rnd = new Random(System.currentTimeMillis());
    int firstJob = rnd.nextInt(numJobs);
    for (int i = 0; i < greedySolution.length; i++) {
      tmpMinMakeSpan = 0;
      nextJobId = 0;

      for (int j = 0; j <= numJobs; j++) {
        int jobIx = (firstJob + j) % numJobs;
        if (numFinJobOpers[jobIx] >= numOpers) {
          continue;
        }
        
        greedySolution[i] = jobIx + 1;

        tmpMakeSpan = jspPhenoEval
          .evaluateSchedule(Arrays.copyOfRange(greedySolution, 0, i + 1));
        
        if (tmpMinMakeSpan == 0 || tmpMakeSpan < tmpMinMakeSpan) {
          tmpMinMakeSpan = tmpMakeSpan;
          nextJobId = jobIx + 1;
        }
      }

      // Store the next new solution
      greedySolution[i] = nextJobId;
      numFinJobOpers[nextJobId - 1] += 1;
    }
    var result = new JspPhenotype(greedySolution);
    result.setObjValue(tmpMinMakeSpan);
    
    return result;
  }

  /**
   * Method creates random schedule.
   * 
   * @param jspPhenoEval Phenotype evaluator
   * @param jobs Jobs to schedule
   * @param randomSeed Random seed
   */
  public static JspPhenotype createRandomSchedule(
      JspPhenotypeEvaluator jspPhenoEval, 
      JspJobsDefinition jobs, long randomSeed
  ) throws Exception {
    Random rnd = new Random(randomSeed);

    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] randSol = new Integer[numJobs * numOpers];


    int i = 0;
    for (int jobID = 1; jobID <= numJobs; jobID++) {
      for (int operID = 1; operID <= numOpers; operID++) {
        randSol[i++] = jobID;
      }
    }    

    // Random permutation
    for (int j = 0; j < randSol.length * 2; j++) {
      int ix1 = rnd.nextInt(randSol.length);
      int ix2 = rnd.nextInt(randSol.length);
      int a = randSol[ix1];
      randSol[ix1] = randSol[ix2];
      randSol[ix2] = a;
    }

    var result = new JspPhenotype(randSol);
    var makeSpan = jspPhenoEval.evaluateSchedule(randSol);
    result.setObjValue((double)makeSpan);

    return result;
  }

  /**
   * .
   * @param args .
   * @throws Exception .
   */
  public static void main(String[] args) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    List<ProblemInstanceInfo> instances = problemProvider
        .getProblemInfo().getProblemInstanceInfos();

    for (ProblemInstanceInfo jobInfo : instances) {
      JspJobsDefinition jobs = null;
      try (InputStream stream = JspScheduleProvider.class.getResourceAsStream(
          jobInfo.getPath())
      ) {    
        jobs = new JspJobsDefinition(jobInfo, stream);
      }
      ProblemInfo pi = problemProvider.getProblemInfo();
      JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, jobs);

      JspPhenotype ph1 = JspScheduleProvider.createRandomSchedule(evaluator, jobs, 1);
      double[] val1 = evaluator.evaluate(ph1);
      JspPhenotype ph2 = JspScheduleProvider.createGreedySchedule(evaluator, jobs);
      double[] val2 = evaluator.evaluate(ph2);
      
      log.debug("{} - {} - {}", jobInfo.getInstanceID(), val2[0], val1[0]);
    }
  }
}
