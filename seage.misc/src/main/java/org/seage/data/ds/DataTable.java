package org.seage.data.ds;
import java.util.*;
import java.util.ArrayList;

/**
 * Summary description for DataTable.
 */
public class DataTable extends ArrayList<DataRow>
{
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
}
