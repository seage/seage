package org.seage.problem.fsp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FspScheduleProvider {
  private static Logger logger = LoggerFactory.getLogger(FspScheduleProvider.class.getName());

  static Random rnd = new Random();

  /**
   * Method creates solution with the use of greedy algorithm.
   * 
   * @param jobs Problem instance definition
   * @param jspPhenoEval Phenotype evaluator
   */
  public static JspPhenotype createGreedySchedule0(JspPhenotypeEvaluator jspPhenoEval,
      JspJobsDefinition jobs) throws Exception {
    int numJobs = jobs.getJobsCount();
    // int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] greedySolution = new Integer[numJobs];

    int[] numFinJobOpers = new int[numJobs];
    for (int i = 0; i < numJobs; i++) {
      numFinJobOpers[i] = 0;
    }

    double tmpMakeSpan = 0;
    double tmpMinMakeSpan = 0;

    int nextJobId = 0;
    for (int k = 0; k < greedySolution.length; k++) {
      for (int i = 0; i < greedySolution.length; i++) {
        tmpMinMakeSpan = 0;
        nextJobId = 0;

        for (int j = 0; j <= numJobs; j++) {
          int jobIx = (k + j) % numJobs;
          if (numFinJobOpers[jobIx] >= 1) {
            continue;
          }

          greedySolution[i] = jobIx + 1;

          tmpMakeSpan = jspPhenoEval.evaluateSchedule(Arrays.copyOfRange(greedySolution, 0, i + 1));

          if (tmpMinMakeSpan == 0 || tmpMakeSpan < tmpMinMakeSpan) {
            tmpMinMakeSpan = tmpMakeSpan;
            nextJobId = jobIx + 1;
          }
        }

        // Store the next new solution
        greedySolution[i] = nextJobId;
        numFinJobOpers[nextJobId - 1] += 1;
      }
    }
    var result = new JspPhenotype(greedySolution);
    result.setObjValue(tmpMinMakeSpan);

    return result;
  }

  /**
   * .
   */
  public static JspPhenotype createGreedySchedule(JspPhenotypeEvaluator jspPhenoEval,
      JspJobsDefinition jobs) throws Exception {
    int length = jobs.getJobsCount();
    PermutationEvaluator permEval = new PermutationEvaluator() {
      public double evaluate(Integer[] solution) throws Exception {
        ArrayList<Integer> s = new ArrayList<>();
        for (int i = 0; i < solution.length; i++) {
          if (solution[i] == -1) {
            break;
          }
          s.add(solution[i] + 1);
        }
        return jspPhenoEval.evaluateSchedule(s.toArray(new Integer[0]));
      }
    };
    JspPhenotype res = new JspPhenotype(new Integer[] {});
    res.setObjValue(greedy(length, permEval));
    return res;
  }

  interface PermutationEvaluator {
    double evaluate(Integer[] solution) throws Exception;
  }

  private static double greedy(int length, PermutationEvaluator evaluator) throws Exception {
    double[] roundBestSolutions = new double[length];
    Random rnd = new Random(1);

    List<Integer> rounds = new ArrayList<>(length);
    for (int k = 0; k < length; k++) {
      rounds.add(k);
    }
    rounds.parallelStream().forEach((k) -> {
      try {
        Integer[] solution = new Integer[length];
        boolean[] avail = new boolean[length];
        for (int i = 0; i < avail.length; i++) {
          solution[i] = -1;
          avail[i] = true;
        }
        solution[0] = k;
        avail[solution[0]] = false;

        for (int i = 1; i < length; i++) {
          int next = -1;
          double bestObjValue = Double.MAX_VALUE;
          for (int j = 0; j < length; j++) {
            if (!avail[j]) {
              continue;
            }
            solution[i] = j;
            double newObjValue = evaluator.evaluate(solution);
            solution[i] = -1;
            if (newObjValue < bestObjValue && newObjValue != 0) {
              bestObjValue = newObjValue;
              next = j;
            }
          }
          if (next == -1) {
            int ix = rnd.nextInt(avail.length);
            while (true) {
              if (avail[++ix % avail.length]) {
                break;
              }
            }
            next = ix % avail.length;
          }
          solution[i] = next;
          avail[next] = false;
        }
        double currObjValue = evaluator.evaluate(solution);
        roundBestSolutions[k] = currObjValue;
      } catch (Exception ex) {
        roundBestSolutions[k] = -1;
      }
    });
    return Arrays.stream(roundBestSolutions).min().getAsDouble();
  }

  /**
   * Method creates random schedule.
   * 
   * @param jspPhenoEval Phenotype evaluator
   * @param jobs Jobs to schedule
   */
  public static JspPhenotype createRandomSchedule(JspPhenotypeEvaluator jspPhenoEval,
      JspJobsDefinition jobs) throws Exception {

    int numJobs = jobs.getJobsCount();
    int numOpers = jobs.getJobInfos()[0].getOperationInfos().length;

    Integer[] randSolPart = new Integer[numJobs];
    Integer[] randSol = new Integer[numJobs * numOpers];

    for (int jobID = 1; jobID <= numJobs; jobID++) {
      randSolPart[jobID - 1] = jobID;
    }

    // Random permutation
    for (int j = 0; j < randSolPart.length * 2; j++) {
      int ix1 = rnd.nextInt(randSolPart.length);
      int ix2 = rnd.nextInt(randSolPart.length);
      int a = randSolPart[ix1];
      randSolPart[ix1] = randSolPart[ix2];
      randSolPart[ix2] = a;
    }

    for (int i = 0; i < numJobs; i++) {
      for (int j = 0; j < numOpers; j++) {
        randSol[i * numOpers + j] = randSolPart[i];
      }
    }

    var pheno = new JspPhenotype(randSol);
    var result = new JspPhenotype(randSolPart);
    double[] objVals = jspPhenoEval.evaluate(pheno);
    result.setObjValue(objVals[0]);
    result.setScore(objVals[1]); 

    return result;
  }

  /**
   * .
   */
  public static void main(String[] args) throws Exception {
    FspProblemProvider problemProvider = new FspProblemProvider();
    List<ProblemInstanceInfo> instances =
        problemProvider.getProblemInfo().getProblemInstanceInfos();

    Collections.sort(instances, (i1, i2) -> (i1.getInstanceID().compareTo(i2.getInstanceID())));

    for (ProblemInstanceInfo jobInfo : instances) {
      try {
        JspJobsDefinition jobs = null;
        try (
            InputStream stream = FspScheduleProvider.class.getResourceAsStream(jobInfo.getPath())) {
          jobs = new FspJobsDefinition(jobInfo, stream);
        }
        ProblemInfo pi = problemProvider.getProblemInfo();
        JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, jobs);

        // JspPhenotype ph1 = FspScheduleProvider.createRandomSchedule(evaluator, jobs, 1);
        // double val1 = evaluator.evaluateSchedule(ph1.getSolution());
        JspPhenotype ph2 = FspScheduleProvider.createGreedySchedule(evaluator, jobs);
        double val2 = ph2.getObjValue();

        logger.debug("{} - {} - ", jobInfo.getInstanceID(), val2);
      } catch (Exception ex) {
        logger.error(String.format("Error instance %s", jobInfo.getInstanceID()), ex);
      }
    }
  }
}
