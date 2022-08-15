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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.seage.data.Pair;

/**
 * JSP Schedule.
 */
public class Schedule {
  private int _makeSpan;
  private ScheduleCell[] _lastCellInJob;
  private ScheduleCell[] _lastCellOnMachine;

  private ScheduleCell _mostDistantCell;
  private JspJobsDefinition _jobsDefinition;
  private int _numJobs;
  private int _numMachines;
  private int[] _lastActivityInJobIndex;
  private int[] _lastActivityOnMachineIndex;
  private int[] _endTimeInJob;
  private int[] _endTimeOnMachine;

  /**
   * .
   * @param jobsDefinition .
   */
  public Schedule(JspJobsDefinition jobsDefinition) {
    _jobsDefinition = jobsDefinition;
    _numJobs = jobsDefinition.getJobsCount();
    _numMachines = jobsDefinition.getMachinesCount();
    
    _makeSpan = 0;
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
    _lastCellInJob = new ScheduleCell[_numJobs];
    _lastCellOnMachine = new ScheduleCell[_numMachines];
    _lastActivityInJobIndex = new int[_numJobs];
    _lastActivityOnMachineIndex = new int[_numMachines];
    _endTimeInJob = new int[_numJobs];
    _endTimeOnMachine = new int[_numMachines];
    // Reset done

    ScheduleJobInfo currentJob;
    ScheduleOperationInfo currentOper;

    int indexCurrentMachine = 0;
    int indexCurrentJob = 0;
    int indexCurrentOper = 0;

    int maxMakeSpan = 0;

    for (int i = 0; i < _numJobs; i++) {
      _lastActivityInJobIndex[i] = 0;
      _endTimeInJob[i] = 0;
    }
    for (int i = 0; i < _numMachines; i++) {
      _lastActivityOnMachineIndex[i] = 0;
      _endTimeOnMachine[i] = 0;
    }

    for (int i = 0; i < jobArray.length; i++) {
      indexCurrentJob = jobArray[i] - 1;

      indexCurrentOper = _lastActivityInJobIndex[indexCurrentJob]++;
      
      currentJob = _jobsDefinition.getJobInfos()[indexCurrentJob];
      currentOper = currentJob.getOperationInfos()[indexCurrentOper];

      indexCurrentMachine = currentOper.MachineID - 1;

      if (_endTimeOnMachine[indexCurrentMachine] > _endTimeInJob[indexCurrentJob]) {
        _endTimeOnMachine[indexCurrentMachine] += currentOper.Length;
        _endTimeInJob[indexCurrentJob] = _endTimeOnMachine[indexCurrentMachine];
      } else {
        _endTimeInJob[indexCurrentJob] += currentOper.Length;
        _endTimeOnMachine[indexCurrentMachine] = _endTimeInJob[indexCurrentJob];
      }

      if (_endTimeOnMachine[indexCurrentMachine] > maxMakeSpan) {
        maxMakeSpan = _endTimeOnMachine[indexCurrentMachine];
        _makeSpan = maxMakeSpan;
      } 
      addCell(indexCurrentJob, indexCurrentMachine,
          new ScheduleCell(i, _endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
              currentOper.Length)); 
    }
  }

  private void addCell(int jobIndex, int machineIndex, ScheduleCell newCell) {
    if (_lastCellOnMachine[machineIndex] != null) {
      _lastCellOnMachine[machineIndex].setNextCellOnMachine(newCell);
    }

    newCell.setPreviousCellOnMachine(_lastCellOnMachine[machineIndex]);
    newCell.setPreviousCellInJob(_lastCellInJob[jobIndex]);

    _lastCellOnMachine[machineIndex] = newCell;
    _lastCellInJob[jobIndex] = newCell;

    if (_mostDistantCell == null 
        || newCell.getEndTime() > _mostDistantCell.getEndTime()) {
      _mostDistantCell = newCell;
    }
  }

  /**
   * .
   * @return .
   * @throws Exception .
   */
  public List<Pair<ScheduleCell>> findCriticalPath() throws Exception {
    ArrayList<Pair<ScheduleCell>> results = new ArrayList<Pair<ScheduleCell>>();
    findCriticalPath(_mostDistantCell, results);
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
      }
      else {
        if (currCell.getStartTime() == prevCellInJob.getEndTime()) {
          breakFound = true;
        } else {
          currCell = prevCellOnMachine;
        }
      }
    }

    if (prevCellInJob.getPreviousCellOnMachine() != null) {
      if (prevCellInJob.getStartTime() == prevCellInJob.getPreviousCellOnMachine().getEndTime()
          && (prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob() == null 
          || !prevCellInJob.getPreviousCellOnMachine().compareStart2EndTo(
          prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob()))) { 
        criticalPairs.add(
            new org.seage.data.Pair<ScheduleCell>(prevCellInJob.getPreviousCellOnMachine(),
            prevCellInJob));
      }
    }

    if (currCell.getNextCellOnMachine() != null &&
        currCell.getNextCellOnMachine().getStartTime() == currCell.getEndTime()) {
      criticalPairs.add(new Pair<ScheduleCell>(currCell, currCell.getNextCellOnMachine()));
    }

    findCriticalPath(prevCellInJob, criticalPairs);

  }

  public int getMakeSpan() {
    return _makeSpan;
  }

  public void setMakeSpan(int makeSpan) {
    _makeSpan = makeSpan;
  }
}
