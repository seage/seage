package org.seage.problem.jsp;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
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
  public static JspPhenotype createGreedySchedule(JspPhenotypeEvaluator jspPhenoEval, JobsDefinition jobs) throws Exception {
    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] greedySolution = new Integer[numJobs*numOpers];

    int[] numFinJobOpers = new int[numJobs];
    for (int i = 0; i < numJobs; i++)
      numFinJobOpers[i] = 0;

    double tmpMakeSpan = 0;
    double tmpMinMakeSpan = 0;

    int nextJobId = 0;

    for (int i = 0; i < greedySolution.length; i++) {
      tmpMinMakeSpan = 0;
      nextJobId = 0;

      for (int jobId = 1; jobId <= numJobs; jobId++) {
        if ( numFinJobOpers[jobId-1] >= numOpers )
          continue;
        
        greedySolution[i] = jobId;

        tmpMakeSpan = jspPhenoEval
          .createSchedule(Arrays.copyOfRange(greedySolution, 0, i+1), true).getMakeSpan();
        
        if (tmpMinMakeSpan == 0 || tmpMakeSpan < tmpMinMakeSpan) {
          tmpMinMakeSpan = tmpMakeSpan;
          nextJobId = jobId;
        }
      }

      // Strore the next new solution
      greedySolution[i] = nextJobId;
      numFinJobOpers[nextJobId-1] += 1;
    }
    var result = new JspPhenotype(greedySolution);
    result.setObjValue(tmpMinMakeSpan);
    
    return result;
  }

  /**
   * Method creates random schedule
   * 
   * @params length Length of schedule
   * @params randomSeed Random seed
   */
  public static JspPhenotype createRandomSchedule(JspPhenotypeEvaluator jspPhenoEval, JobsDefinition jobs, long randomSeed) throws Exception {
    Random rnd = new Random(randomSeed);

    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] randSol = new Integer[numJobs*numOpers];


    int i = 0;
    for (int jobID = 1; jobID <= numJobs; jobID++ ) {
      for (int operID = 1; operID <= numOpers; operID++) {
        randSol[i++] = jobID;
      }
    }
    

    // Random permutation
    for(int j = 0; j < randSol.length * 2; j++) {
      int ix1=rnd.nextInt(randSol.length);
      int ix2=rnd.nextInt(randSol.length);
      int a = randSol[ix1];
      randSol[ix1] = randSol[ix2];
      randSol[ix2] = a;
    }

    var result = new JspPhenotype(randSol);
    var makeSpan = jspPhenoEval.createSchedule(randSol, true).getMakeSpan();
    result.setObjValue((double)makeSpan);

    return result;
  }

  public static void main(String[] args) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    List<ProblemInstanceInfo> instances = problemProvider.getProblemInfo().getProblemInstanceInfos();

    for(ProblemInstanceInfo jobInfo : instances) {
      JobsDefinition jobs = null;
      try(InputStream stream = ScheduleProvider.class.getResourceAsStream(jobInfo.getPath())) {    
        jobs = new JobsDefinition(jobInfo, stream);
      }
      ProblemInfo pi = problemProvider.getProblemInfo();
      JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, jobs);

      JspPhenotype ph1 = ScheduleProvider.createRandomSchedule(evaluator, jobs, 1);
      double[] val1 = evaluator.evaluate(ph1);
      JspPhenotype ph2 = ScheduleProvider.createGreedySchedule(evaluator, jobs);
      double[] val2 = evaluator.evaluate(ph2);
      
      logger.debug(jobInfo.getInstanceID() + " - " + val2[0] + " - " + val1[0]);
    }
  }
}
