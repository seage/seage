package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.Connection;
import java.sql.DriverManager;

public class TableCreator
{
	protected String _dbPath;

	public TableCreator(String dbPath)
	{
		_dbPath = dbPath;
	}
	
	protected Connection createConnection() throws Exception
	{
		Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:"+_dbPath+";DATABASE_TO_UPPER=FALSE", "sa", "sa");
	}
}
