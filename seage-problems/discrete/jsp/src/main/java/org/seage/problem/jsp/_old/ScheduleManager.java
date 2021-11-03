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
package org.seage.problem.jsp._old;

import org.seage.data.ds.DataCell;
import org.seage.data.ds.DataRow;
import org.seage.data.ds.DataStore;
import org.seage.data.ds.DataTable;
import org.seage.problem.jsp.JobInfo;
import org.seage.problem.jsp.OperationInfo;
import org.seage.problem.jsp.Schedule;
import org.seage.problem.jsp.ScheduleCell;

/**
 * Summary description for ScheduleManager.
 */
@SuppressWarnings("all")
public class ScheduleManager
{
    DataTable _jobsTable;

    private int _numJobs;
    private int _numMachines;

    private int[] _lastActivityInJobIndex;
    private int[] _lastActivityOnMachineIndex;

    private int[] _endTimeInJob;
    private int[] _endTimeOnMachine;

    public ScheduleManager()
    {
        _jobsTable = DataStore.getInstance().getDataTable("jobs");
        _numJobs = _jobsTable.getRowCount();
        _numMachines = getNumMachines();

        _lastActivityInJobIndex = new int[_numJobs];
        _lastActivityOnMachineIndex = new int[_numMachines];

        _endTimeInJob = new int[_numJobs];
        _endTimeOnMachine = new int[_numMachines];

    }

    private int getNumMachines()
    {
        int val = 0;
        int numMachines = Integer.MIN_VALUE;

        for (int i = 0; i < _jobsTable.getRowCount(); i++)
        {
            for (int j = 0; j < _jobsTable.get(i).size(); j++)
            {
                val = ((OperationInfo) _jobsTable.get(i).get(j).getCellProperty()).MachineID;
                if (val > numMachines)
                    numMachines = val;
            }
        }
        return numMachines;
    }

    public Object[] evaluateSchedule(int[] jobArray, boolean buildSchedule)
    {
        Schedule schedule = null;
        if (buildSchedule)
            schedule = new Schedule(null);

        JobInfo currentJob;
        OperationInfo currentOper;

        DataRow currentRow;
        DataCell currentCell;

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

        for (int i = 0; i < jobArray.length; i++)
        {
            indexCurrentJob = jobArray[i] - 1;

            indexCurrentOper = _lastActivityInJobIndex[indexCurrentJob]++;

            currentRow = _jobsTable.get(indexCurrentJob);
            currentCell = currentRow.get(indexCurrentOper);

            currentJob = (JobInfo) currentRow.getRowProperty();
            currentOper = (OperationInfo) currentCell.getCellProperty();

            //indexCurrentMachine = _lastActivityOnMachineIndex[currentOper.MachineID - 1]++;
            indexCurrentMachine = currentOper.MachineID - 1;
            //System.out.print(indexCurrentMachine + ":" + indexCurrentJob + " ");
            //System.out.print(indexCurrentJob + ":" + indexCurrentOper + " ");

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

            int a = 0;
            // if (buildSchedule)
            //     schedule.addCell(indexCurrentJob,
            //             indexCurrentMachine,
            //             new ScheduleCell(i,
            //                     _endTimeOnMachine[indexCurrentMachine] - currentOper.Length,
            //                     currentOper.Length));

        }
        //System.out.println();

        Object[] res = { new Integer(maxMakeSpan), schedule/*, _numJobs, _numMachines */ };
        return res;
    }
}
