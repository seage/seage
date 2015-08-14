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
package org.seage.problem.jssp;

import java.util.ArrayList;

/**
 * Summary description for Schedule.
 */
public class Schedule
{
    private ScheduleCell[] _lastCellInJob;
    private ScheduleCell[] _lastCellOnMachine;

    private ScheduleCell _furthestCell;

    public Schedule(int numJobs, int numMachines)
    {
        _lastCellInJob = new ScheduleCell[numJobs];
        _lastCellOnMachine = new ScheduleCell[numMachines];
    }

    public void addCell(int jobIndex, int machineIndex, ScheduleCell newCell)
    {
        if (_lastCellOnMachine[machineIndex] != null)
            _lastCellOnMachine[machineIndex].setNextCellOnMachine(newCell);

        newCell.setPreviousCellOnMachine(_lastCellOnMachine[machineIndex]);
        newCell.setPreviousCellInJob(_lastCellInJob[jobIndex]);

        _lastCellOnMachine[machineIndex] = newCell;
        _lastCellInJob[jobIndex] = newCell;

        if (_furthestCell == null)
            _furthestCell = newCell;
        else if (newCell.getEndTime() > _furthestCell.getEndTime())
            _furthestCell = newCell;
    }

    public ScheduleCell[] findCriticalPath() throws Exception
    {
        ArrayList results = new ArrayList();
        ScheduleCell sc = _furthestCell;

        findBreak(sc, results);

        ScheduleCell[] scArray = new ScheduleCell[results.size()];

        for (int i = 0; i < results.size(); i++)
        {
            scArray[i] = (ScheduleCell) results.get(i);
        }

        return scArray;
    }

    private void findBreak(ScheduleCell cell, ArrayList array) throws Exception
    {
        if (cell == null)
            throw new Exception("Critical path: Null pointer");

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
                if (prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob() == null)
                {
                    array.add(prevCellInJob.getPreviousCellOnMachine());
                    array.add(prevCellInJob);
                }
                else
                {
                    if (!prevCellInJob.getPreviousCellOnMachine()
                            .compareStart2EndTo(prevCellInJob.getPreviousCellOnMachine().getPreviousCellInJob()))
                    {
                        array.add(prevCellInJob.getPreviousCellOnMachine());
                        array.add(prevCellInJob);
                    }
                }
            }
        }

        if (currCell.getNextCellOnMachine() != null)
        {
            if (currCell.getNextCellOnMachine().getStartTime() == currCell.getEndTime())
            {
                array.add(currCell);
                array.add(currCell.getNextCellOnMachine());
            }
        }

        findBreak(prevCellInJob, array);

    }
}
