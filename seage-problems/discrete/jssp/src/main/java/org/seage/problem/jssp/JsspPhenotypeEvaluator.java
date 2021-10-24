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

public class JsspPhenotypeEvaluator implements IPhenotypeEvaluator<JsspPhenotype>
{
  private JobsDefinition _jobsDefinition;
  
  public JsspPhenotypeEvaluator(JobsDefinition jobsDefinition)
  {
    _jobsDefinition = jobsDefinition;
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
      
  public double[] evaluateSchedule(Integer[] jobArray)
  {
    return evaluateSchedule(jobArray, false);
  }
      
  public double[] evaluateSchedule(Integer[] jobArray, boolean buildSchedule)
  {
    Schedule schedule = null;

    int numJobs = _jobsDefinition.getJobsCount();
    int numMachines = _jobsDefinition.getMachinesCount();;

    int[] lastActivityInJobIndex = new int[numJobs];;
    int[] lastActivityOnMachineIndex = new int[numMachines];;

    int[] endTimeInJob = new int[numJobs];;
    int[] endTimeOnMachine = new int[numMachines];;

    if (buildSchedule)
      schedule = new Schedule(numJobs, numMachines);
    
    JobInfo currentJob;
    OperationInfo currentOper;

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;

    for (int i = 0; i < numJobs; i++)
    {
      lastActivityInJobIndex[i] = 0;
      endTimeInJob[i] = 0;
    }
    for (int i = 0; i < numMachines; i++)
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
      
      if (buildSchedule)
      {
        schedule.addCell(indexCurrentJob, indexCurrentMachine,
            new ScheduleCell(i, endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
                currentOper.Length)); 
      }
    }
    
    if(buildSchedule)
      schedule.Commit();
    
    return new double[] { maxMakeSpan };
  }
      
  public Schedule getSchedule()
  {
    return null; // _schedule; // TODO: Fix this
  }
}
