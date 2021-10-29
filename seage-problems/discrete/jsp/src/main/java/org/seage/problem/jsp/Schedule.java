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
package org.seage.problem.jsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seage.data.Pair;

/**
 * JSP Schedule.
 */
public class Schedule
{
  private int _makeSpan;
  private ScheduleCell[] _lastCellInJob;
  private ScheduleCell[] _lastCellOnMachine;

  private ScheduleCell _mostDistantCell;

  public Schedule(int numJobs, int numMachines)
  {
    _lastCellInJob = new ScheduleCell[numJobs];
    _lastCellOnMachine = new ScheduleCell[numMachines];
    _makeSpan = 0;
  }

  public void addCell(int jobIndex, int machineIndex, ScheduleCell newCell)
  {
    if (_lastCellOnMachine[machineIndex] != null)
      _lastCellOnMachine[machineIndex].setNextCellOnMachine(newCell);

    newCell.setPreviousCellOnMachine(_lastCellOnMachine[machineIndex]);
    newCell.setPreviousCellInJob(_lastCellInJob[jobIndex]);

    _lastCellOnMachine[machineIndex] = newCell;
    _lastCellInJob[jobIndex] = newCell;

    if (_mostDistantCell == null)
      _mostDistantCell = newCell;
    else if (newCell.getEndTime() > _mostDistantCell.getEndTime())
      _mostDistantCell = newCell;
  }

  public List<Pair<ScheduleCell>> findCriticalPath() throws Exception
  {
    ArrayList<Pair<ScheduleCell>> results = new ArrayList<Pair<ScheduleCell>>();
    findCriticalPath(_mostDistantCell, results);
    return Collections.unmodifiableList(results);
  }

  private void findCriticalPath(ScheduleCell cell, ArrayList<Pair<ScheduleCell>> criticalPairs) throws Exception
  {  
    if (cell == null)
      throw new Exception("Critical path: cell is a null pointer");

    boolean breakFound = false;

    ScheduleCell currCell = cell;
    ScheduleCell prevCellOnMachine = null;
    ScheduleCell prevCellInJob = null;

    while (!breakFound)
    {
      if (currCell == null)
      {
        if (prevCellOnMachine == null && prevCellInJob == null)
          throw new Exception("ALL Null pointer");
        ;
        throw new Exception("While: Null pointer");
      }

      prevCellOnMachine = currCell.getPreviousCellOnMachine();
      prevCellInJob = currCell.getPreviousCellInJob();

      if (prevCellOnMachine == null && prevCellInJob == null)
        return;

      if (prevCellInJob == null)
      {
        currCell = prevCellOnMachine;
      }
      else
      {
        if (currCell.getStartTime() == prevCellInJob.getEndTime())
        {
          breakFound = true;
        }
        else
          currCell = prevCellOnMachine;
      }
    }

    if (prevCellInJob.getPreviousCellOnMachine() != null)
    {
      if (prevCellInJob.getStartTime() == prevCellInJob.getPreviousCellOnMachine().getEndTime())
      {
        if (prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob() == null ||
          !prevCellInJob.getPreviousCellOnMachine()
            .compareStart2EndTo(prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob()))    
          criticalPairs.add(new org.seage.data.Pair<ScheduleCell>(prevCellInJob.getPreviousCellOnMachine(), prevCellInJob));        
      }
    }

    if (currCell.getNextCellOnMachine() != null &&
      currCell.getNextCellOnMachine().getStartTime() == currCell.getEndTime())
    {
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
