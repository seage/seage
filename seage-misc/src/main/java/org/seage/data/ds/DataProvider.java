package org.seage.data.ds;

import org.seage.data.ds.DataTable;

/**
 * Summary description for DataProvider.
 */
public interface DataProvider
{
	DataTable	getDataTable(String queryString);
	void		saveDataTable(String queryString);
}
