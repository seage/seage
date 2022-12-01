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

/**
 * Summary description for ScheduleCell.
 */
public class ScheduleCell
{
  private int _index;
  private int _startTime;
  private int _length;

  private ScheduleCell _prevCellOnMachine;
  private ScheduleCell _nextCellOnMachine;
  private ScheduleCell _previousCellInJob;

  public ScheduleCell(int index, int startTime, int length)
  {
    _index = index;
    _startTime = startTime;
    _length = length;
  }

  public ScheduleCell getPreviousCellOnMachine()
  {
    return _prevCellOnMachine;
  }

  public void setPreviousCellOnMachine(ScheduleCell prev)
  {
    _prevCellOnMachine = prev;
  }

  public ScheduleCell getNextCellOnMachine()
  {
    return _nextCellOnMachine;
  }

  public void setNextCellOnMachine(ScheduleCell next)
  {
    _nextCellOnMachine = next;
  }

  public ScheduleCell getPreviousCellInJob()
  {
    return _previousCellInJob;
  }

  public void setPreviousCellInJob(ScheduleCell prev)
  {
    _previousCellInJob = prev;
  }

  public int getStartTime()
  {
    return _startTime;
  }

  public int getEndTime()
  {
    return _startTime + _length;
  }

  public int getLength()
  {
    return _length;
  }

  public int getIndex()
  {
    return _index;
  }

  public int compareStartTo(ScheduleCell comp)
  {
    return _startTime - comp._startTime;
  }

  public boolean compareStart2EndTo(ScheduleCell comp)
  {
    if (this._startTime == comp._startTime + comp._length)
      return true;
    else
      return false;
  }
      
  @Override
  public String toString()
  {
    return _index + " - " + getStartTime()+":" + getEndTime();
    
  }
}
