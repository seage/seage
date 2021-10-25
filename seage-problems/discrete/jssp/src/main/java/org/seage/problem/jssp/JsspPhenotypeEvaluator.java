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
  private Schedule _schedule;

  private int _numJobs;
  private int _numMachines;

  private int[] _lastActivityInJobIndex;
  private int[] _lastActivityOnMachineIndex;

  private int[] _endTimeInJob;
  private int[] _endTimeOnMachine;

  public JsspPhenotypeEvaluator(JobsDefinition jobsDefinition)
  {
    _jobsDefinition = jobsDefinition;
    
    _numJobs = _jobsDefinition.getJobsCount();
    _numMachines = _jobsDefinition.getMachinesCount();

    _lastActivityInJobIndex = new int[_numJobs];
    _lastActivityOnMachineIndex = new int[_numMachines];

    _endTimeInJob = new int[_numJobs];
    _endTimeOnMachine = new int[_numMachines];
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
    _schedule = null;
    return evaluateSchedule(jobArray, false);
  }
      
  public double[] evaluateSchedule(Integer[] jobArray, boolean buildSchedule)
  {
    if (buildSchedule)
      _schedule = new Schedule(_numJobs, _numMachines);
    
    JobInfo currentJob;
    OperationInfo currentOper;

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;

    for (int i = 0; i < _numJobs; i++)
    {
      _lastActivityInJobIndex[i] = 0;
      _endTimeInJob[i] = 0;
    }
    for (int i = 0; i < _numMachines; i++)
    {
      _lastActivityOnMachineIndex[i] = 0;
      _endTimeOnMachine[i] = 0;
    }

    try {
      for (int i = 0; i < jobArray.length; i++)
      {
        indexCurrentJob = jobArray[i] - 1;

        indexCurrentOper = _lastActivityInJobIndex[indexCurrentJob]++;

        currentJob = _jobsDefinition.getJobInfos()[indexCurrentJob];
        currentOper = currentJob.getOperationInfos()[indexCurrentOper];

        indexCurrentMachine = currentOper.MachineID - 1;

        if (_endTimeOnMachine[indexCurrentMachine] > _endTimeInJob[indexCurrentJob])
        {
          _endTimeOnMachine[indexCurrentMachine] += currentOper.Length;
          _endTimeInJob[indexCurrentJob] = _endTimeOnMachine[indexCurrentMachine];
        }
        else
        {
          _endTimeInJob[indexCurrentJob] += currentOper.Length;
          _endTimeOnMachine[indexCurrentMachine] = _endTimeInJob[indexCurrentJob];
        }

        if (_endTimeOnMachine[indexCurrentMachine] > maxMakeSpan)
        {
          maxMakeSpan = _endTimeOnMachine[indexCurrentMachine];
        } 
        
        if (buildSchedule)
        {
          _schedule.addCell(indexCurrentJob, indexCurrentMachine,
              new ScheduleCell(i, _endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
                  currentOper.Length)); 
        }
      }
    } catch (Exception ex) {
      int[] numNums = new int[_numMachines + 1];
      for (int j = 0; j < numNums.length; j++) {
        numNums[j] = 0;
      }
      for (int k = 0; k < jobArray.length; k++) {
        numNums[jobArray[k]] += 1;
      }
      System.out.println(ex);
    }
    
    if(buildSchedule)
      _schedule.Commit();
    
    return new double[] { maxMakeSpan };
  }
      
  public Schedule getSchedule()
  {
    return _schedule;
  }
}
