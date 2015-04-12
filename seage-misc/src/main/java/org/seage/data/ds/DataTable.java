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
 * Summary description for DataTable.
 */
public class DataTable extends ArrayList<DataRow>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4842401883256968640L;
	protected Object _tableProperty;

	public DataTable()
	{
	}
    
    public DataTable(int numRow)
	{
        this(numRow, 0);
	}
    
    public DataTable(int numRow, int numCells)
	{
        for(int i=0;i<numRow;i++)
            add(new DataRow(numCells));
        
	}

	public DataRow getLastRow()
	{
		return this.get(this.size()-1);
	}

	public int getCellCount()
	{
		int count = 0;
		for(int i=0;i<size();i++)
		{
			for(int j=0;j<get(i).size();j++) count++;
		}
		return count;
	}

	public Object getTableProperty()
	{
		return _tableProperty;
	}

	public void setTableProperty(Object o)
	{
		_tableProperty = o;
	}

	public int getRowCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
