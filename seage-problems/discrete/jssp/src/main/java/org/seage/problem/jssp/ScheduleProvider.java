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
    JobInfo[] jobInfos = jobs.getJobInfos();

    int _numJobs = jobs.getJobsCount();
    int _numMachines = jobInfos[0].getOperationInfos().length;

    Integer[] greedySolution = new Integer[_numJobs*_numMachines];

    int[] _lastActivityInJobIndex = new int[_numJobs];
    int[] _lastActivityOnMachineIndex = new int[_numMachines];

    int[] _endTimeInJob = new int[_numJobs];
    int[] _endTimeOnMachine = new int[_numMachines];

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;
    int tmpMakeSpan = 0;
    int tmpMinMakeSpan = 0;

    JobInfo currentJob;
    OperationInfo currentOper;

    int nextJobId;

    for (int i = 0; i < greedySolution.length; i++) {
      
      nextJobId = 0;
      for (int jobId = 1; jobId <= _numJobs; jobId++) {
        indexCurrentJob = jobId - 1;
        indexCurrentOper = _lastActivityInJobIndex[indexCurrentJob];

        currentJob = jobs.getJobInfos()[indexCurrentJob];
        if ( indexCurrentJob >= currentJob.getOperationInfos().length)
          continue;
        currentOper = currentJob.getOperationInfos()[indexCurrentOper];

        indexCurrentMachine = currentOper.MachineID - 1;

        if (_endTimeOnMachine[indexCurrentMachine] > _endTimeInJob[indexCurrentJob]) {
          tmpMakeSpan = _endTimeOnMachine[indexCurrentJob] + currentOper.Length;
        }
        else {
          tmpMakeSpan = _endTimeInJob[indexCurrentJob] + currentOper.Length;
        }

        if (tmpMinMakeSpan == 0 || tmpMinMakeSpan > tmpMakeSpan) {
          tmpMinMakeSpan = tmpMakeSpan;
          nextJobId = jobId;
        }
      }

      // Strore the next new solution
      greedySolution[i] = nextJobId;
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
