package org.seage.experimenter.reporting.h2.tablecreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.reporting.IDocumentProcessor;
import org.w3c.dom.Document;

public abstract class H2DataTableCreator
{
	protected Connection _conn;
	protected Hashtable<String, Hashtable<String, XmlHelper.XPath>> _versionedXPaths;

	public H2DataTableCreator(String dbPath) throws Exception
	{
		_conn = createConnection(dbPath);
		_versionedXPaths = new Hashtable<String, Hashtable<String,XmlHelper.XPath>>();
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
	
	protected String getVersionedValue(Document doc, String attName, String version) throws Exception
	{		
		XmlHelper.XPath xPath = _versionedXPaths.get(version).get(attName);
		String val = "";
		if(xPath == null)        
			throw new Exception(doc.getLocalName() + ": No XmlHelper.XPath defined for attribute: " + attName);                    
        else
        {
        	val = XmlHelper.getValueFromDocument(doc.getDocumentElement(), xPath);
        	val = postProcessValue(attName, val, version);
        }
		return val;
	}
	
	protected String postProcessValue(String attName, String val, String version)
	{
		return val;
	}
}
