package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public abstract class TableCreator
{
	protected Connection _conn;

	public TableCreator(String dbPath) throws Exception
	{
		_conn = createConnection(dbPath);
	}

	protected Connection createConnection(String dbPath) throws Exception
	{
		Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:"+dbPath+";DATABASE_TO_UPPER=FALSE", "sa", "sa");
	}
	
	public void close() throws SQLException
	{
		if(_conn != null)
			_conn.close();
		
	}
}
