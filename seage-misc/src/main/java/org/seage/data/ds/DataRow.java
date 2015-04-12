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
package org.seage.data.ds;

import java.util.ArrayList;

/**
 * Summary description for DataRow.
 */
public class DataRow extends ArrayList<DataCell>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1940031810667352202L;
	protected Object _rowProperty;

	public DataRow()
	{		
	}
    
    public DataRow(int numCells)
	{		
        for(int i=0;i<numCells;i++)
            add(new DataCell());
	}

	public Object getRowProperty()
	{
		return _rowProperty;
	}

	public void setRowProperty(Object o)
	{
		_rowProperty = o;
	}

	public String toString()
	{
		String res = "";
		for (int i = 0; i < size(); i++)
			res += get(i).toString() + "\n";
		return res;
	}

	public void initDataCells(int size)
	{
		// TODO Auto-generated method stub
		
	}
}
