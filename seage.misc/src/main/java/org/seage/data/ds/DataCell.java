package org.seage.data.ds;


/**
 * Summary description for DataCell.
 */
public class DataCell
{
	protected Object _cellProperty;
	
	public Object getCellProperty()
	{
		return _cellProperty;
	}

	public void setCellProperty(Object o)
	{
		_cellProperty = o;
	}

	public String toString()
	{
		return _cellProperty.toString();
	}
}
