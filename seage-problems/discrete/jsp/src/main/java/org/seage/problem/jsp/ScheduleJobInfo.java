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
 * Summary description for Class1.
 */
public class ScheduleJobInfo
{
  private int _id;
  private int _priority;
  private ScheduleOperationInfo[] _operations;
      
  public ScheduleJobInfo(int id, ScheduleOperationInfo[] operations/*, int priority*/)
  {
    _id = id;
    //_priority = priority;
    _operations = operations;
  }

  public int getID()
  {
    return _id;
  }

  public int getPriority()
  {
    return _priority;
  }
      
  public ScheduleOperationInfo[] getOperationInfos()
  {
    return _operations;
  }

}
