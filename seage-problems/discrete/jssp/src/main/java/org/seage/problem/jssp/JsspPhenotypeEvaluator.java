/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *   Richard Malek
 *   - Initial implementation
 *   David Omrai
 *   - Adding the JSSP phenotype implementation
 *   - Fixing the compare method
 */
package org.seage.problem.jssp;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemScoreCalculator;

public class JsspPhenotypeEvaluator implements IPhenotypeEvaluator<JsspPhenotype>
{
  private String _instanceID;
  private JobsDefinition _jobsDefinition;
  private int _numJobs;
  private int _numMachines;

  private ProblemScoreCalculator scoreCalculator;

  public JsspPhenotypeEvaluator(ProblemInfo problemInfo, JobsDefinition jobsDefinition) throws Exception
  {
    _jobsDefinition = jobsDefinition;
    _instanceID = jobsDefinition.getProblemInstanceInfo().getInstanceID();
    
    _numJobs = _jobsDefinition.getJobsCount();
    _numMachines = _jobsDefinition.getMachinesCount();
    scoreCalculator = new ProblemScoreCalculator(problemInfo);
  }
      
  /**
   * Method evalueates the given phenotype with a number, makespan
   * @param phenotype solution
   * @return double[] with one number representing the makespan
   * @throws Exception
   */
  @Override
  public double[] evaluate(JsspPhenotype phenotype) throws Exception
  {
    return evaluateSchedule(phenotype.getSolution());
  }
      
  @Override
  public int compare(double[] arg0, double[] arg1)
  {
    return (int)(arg1[0] - arg0[0]);
  }
      
  public double[] evaluateSchedule(Integer[] jobArray) throws Exception
  {
    double makeSpan = createSchedule(jobArray, true).getMakeSpan();
    double score = this.scoreCalculator.calculateInstanceScore(_instanceID, makeSpan);
    return new double[] { makeSpan, score };
  }
    
  public Schedule createSchedule(Integer[] jobArray) {
    return createSchedule(jobArray, false);
  }

  public Schedule createSchedule(Integer[] jobArray, boolean emptySchedule)
  {
    Schedule schedule = new Schedule(_numJobs, _numMachines);;

    int[] lastActivityInJobIndex = new int[_numJobs];
    int[] lastActivityOnMachineIndex = new int[_numMachines];

    int[] endTimeInJob = new int[_numJobs];
    int[] endTimeOnMachine = new int[_numMachines];
    
    JobInfo currentJob;
    OperationInfo currentOper;

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;

    for (int i = 0; i < _numJobs; i++)
    {
      lastActivityInJobIndex[i] = 0;
      endTimeInJob[i] = 0;
    }
    for (int i = 0; i < _numMachines; i++)
    {
      lastActivityOnMachineIndex[i] = 0;
      endTimeOnMachine[i] = 0;
    }

    for (int i = 0; i < jobArray.length; i++)
    {
      indexCurrentJob = jobArray[i] - 1;

      indexCurrentOper = lastActivityInJobIndex[indexCurrentJob]++;

      currentJob = _jobsDefinition.getJobInfos()[indexCurrentJob];
      currentOper = currentJob.getOperationInfos()[indexCurrentOper];

      indexCurrentMachine = currentOper.MachineID - 1;

      if (endTimeOnMachine[indexCurrentMachine] > endTimeInJob[indexCurrentJob])
      {
        endTimeOnMachine[indexCurrentMachine] += currentOper.Length;
        endTimeInJob[indexCurrentJob] = endTimeOnMachine[indexCurrentMachine];
      }
      else
      {
        endTimeInJob[indexCurrentJob] += currentOper.Length;
        endTimeOnMachine[indexCurrentMachine] = endTimeInJob[indexCurrentJob];
      }

      if (endTimeOnMachine[indexCurrentMachine] > maxMakeSpan)
      {
        maxMakeSpan = endTimeOnMachine[indexCurrentMachine];
      } 
      
      if (!emptySchedule)
      {
        schedule.addCell(indexCurrentJob, indexCurrentMachine,
            new ScheduleCell(i, endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
                currentOper.Length)); 
      }
    }

    schedule.setMakeSpan(maxMakeSpan);
    
    return schedule;
  }
}
