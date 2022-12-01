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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

package org.seage.problem.jsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seage.data.Pair;

/**
 * JSP Schedule.
 */
public class Schedule {
  private int makeSpan;
  private ScheduleCell[] lastCellInJob;
  private ScheduleCell[] lastCellOnMachine;

  private ScheduleCell mostDistantCell;
  private JspJobsDefinition jobsDefinition;
  private int numJobs;
  private int numMachines;
  private int[] lastActivityInJobIndex;
  private int[] lastActivityOnMachineIndex;
  private int[] endTimeInJob;
  private int[] endTimeOnMachine;

  /**
   * .
   * @param jobsDefinition .
   */
  public Schedule(JspJobsDefinition jobsDefinition) {
    this.jobsDefinition = jobsDefinition;
    numJobs = jobsDefinition.getJobsCount();
    numMachines = jobsDefinition.getMachinesCount();
    
    makeSpan = 0;
  }

  public Schedule(JspJobsDefinition jobsDefinition, Integer[] jobArray) {
    this(jobsDefinition);    
    createSchedule(jobArray);
  }

  /**
   * .
   * @param jobArray .
   */
  public void createSchedule(Integer[] jobArray) {
    // Reset the schedule
    lastCellInJob = new ScheduleCell[numJobs];
    lastCellOnMachine = new ScheduleCell[numMachines];
    lastActivityInJobIndex = new int[numJobs];
    lastActivityOnMachineIndex = new int[numMachines];
    endTimeInJob = new int[numJobs];
    endTimeOnMachine = new int[numMachines];
    // Reset done

    ScheduleJobInfo currentJob;
    ScheduleOperationInfo currentOper;

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;

    for (int i = 0; i < numJobs; i++) {
      lastActivityInJobIndex[i] = 0;
      endTimeInJob[i] = 0;
    }
    for (int i = 0; i < numMachines; i++) {
      lastActivityOnMachineIndex[i] = 0;
      endTimeOnMachine[i] = 0;
    }

    for (int i = 0; i < jobArray.length; i++) {
      indexCurrentJob = jobArray[i] - 1;

      indexCurrentOper = lastActivityInJobIndex[indexCurrentJob]++;
      
      currentJob = jobsDefinition.getJobInfos()[indexCurrentJob];
      currentOper = currentJob.getOperationInfos()[indexCurrentOper];

      indexCurrentMachine = currentOper.MachineID - 1;

      if (endTimeOnMachine[indexCurrentMachine] > endTimeInJob[indexCurrentJob]) {
        endTimeOnMachine[indexCurrentMachine] += currentOper.Length;
        endTimeInJob[indexCurrentJob] = endTimeOnMachine[indexCurrentMachine];
      } else {
        endTimeInJob[indexCurrentJob] += currentOper.Length;
        endTimeOnMachine[indexCurrentMachine] = endTimeInJob[indexCurrentJob];
      }

      if (endTimeOnMachine[indexCurrentMachine] > maxMakeSpan) {
        maxMakeSpan = endTimeOnMachine[indexCurrentMachine];
        makeSpan = maxMakeSpan;
      } 
      addCell(indexCurrentJob, indexCurrentMachine,
          new ScheduleCell(i, endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
              currentOper.Length)); 
    }
  }

  private void addCell(int jobIndex, int machineIndex, ScheduleCell newCell) {
    if (lastCellOnMachine[machineIndex] != null) {
      lastCellOnMachine[machineIndex].setNextCellOnMachine(newCell);
    }

    newCell.setPreviousCellOnMachine(lastCellOnMachine[machineIndex]);
    newCell.setPreviousCellInJob(lastCellInJob[jobIndex]);

    lastCellOnMachine[machineIndex] = newCell;
    lastCellInJob[jobIndex] = newCell;

    if (mostDistantCell == null 
        || newCell.getEndTime() > mostDistantCell.getEndTime()) {
      mostDistantCell = newCell;
    }
  }

  /**
   * .
   * @return .
   * @throws Exception .
   */
  public List<Pair<ScheduleCell>> findCriticalPath() throws Exception {
    ArrayList<Pair<ScheduleCell>> results = new ArrayList<Pair<ScheduleCell>>();
    findCriticalPath(mostDistantCell, results);
    return Collections.unmodifiableList(results);
  }

  /** 
   * .
   * @param cell .
   * @param criticalPairs .
   * @throws Exception .
   */
  private void findCriticalPath(
      ScheduleCell cell, ArrayList<Pair<ScheduleCell>> criticalPairs) throws Exception {  
    if (cell == null) {
      throw new Exception("Critical path: cell is a null pointer");
    }

    boolean breakFound = false;

    ScheduleCell currCell = cell;
    ScheduleCell prevCellOnMachine = null;
    ScheduleCell prevCellInJob = null;

    while (!breakFound) {
      if (currCell == null) {
        if (prevCellOnMachine == null && prevCellInJob == null) {
          throw new Exception("ALL Null pointer");
        }
        throw new Exception("While: Null pointer");
      }

      prevCellOnMachine = currCell.getPreviousCellOnMachine();
      prevCellInJob = currCell.getPreviousCellInJob();

      if (prevCellOnMachine == null && prevCellInJob == null) {
        return;
      }

      if (prevCellInJob == null) {
        currCell = prevCellOnMachine;
      } else {
        if (currCell.getStartTime() == prevCellInJob.getEndTime()) {
          breakFound = true;
        } else {
          currCell = prevCellOnMachine;
        }
      }
    }

    
    if (prevCellInJob.getPreviousCellOnMachine() != null 
        && prevCellInJob.getStartTime() == prevCellInJob.getPreviousCellOnMachine().getEndTime()
        && (prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob() == null 
        || !prevCellInJob.getPreviousCellOnMachine().compareStart2EndTo(
        prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob()))) { 
      criticalPairs.add(
          new org.seage.data.Pair<ScheduleCell>(prevCellInJob.getPreviousCellOnMachine(),
          prevCellInJob));
    }
    

    if (currCell.getNextCellOnMachine() != null 
        && currCell.getNextCellOnMachine().getStartTime() == currCell.getEndTime()
    ) {
      criticalPairs.add(new Pair<ScheduleCell>(currCell, currCell.getNextCellOnMachine()));
    }

    findCriticalPath(prevCellInJob, criticalPairs);

  }

  public int getMakeSpan() {
    return makeSpan;
  }

  public void setMakeSpan(int makeSpan) {
    this.makeSpan = makeSpan;
  }
}
 