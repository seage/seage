package org.seage.data.ds;

import java.util.ArrayList;

/**
 * Summary description for DataRow.
 */
public class DataRow extends ArrayList<DataCell>
{
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
}
