package org.seage.knowledgebase.importing.db.tablecreator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import org.seage.data.xml.XmlHelper;
import org.w3c.dom.Document;

public abstract class DataTableCreator
{
    protected static Connection _conn;
    protected Hashtable<String, Hashtable<String, XmlHelper.XPath>> _versionedXPaths;

    public DataTableCreator(String dbPath) throws Exception
    {
        if(_conn == null)
            _conn = createConnection(dbPath);
        _versionedXPaths = new Hashtable<String, Hashtable<String, XmlHelper.XPath>>();
    }

    protected static Connection createConnection(String dbPath) throws Exception
    {
        //Class.forName("org.h2.Driver");
        //return DriverManager.getConnection("jdbc:h2:" + dbPath + ";DATABASE_TO_UPPER=FALSE", "sa", "sa");
        
        //Class.forName("org.hsqldb.jdbcDriver");
        //return DriverManager.getConnection("jdbc:hsqldb:file:"+dbPath+"testdb;shutdown=true;hsqldb.large_data=true;", "sa", "");
        
        Class.forName("org.postgresql.Driver");
        Properties props = new Properties();
        props.setProperty("user","test");
        props.setProperty("password","test");
        props.setProperty("ssl","false");
        return DriverManager.getConnection("jdbc:postgresql://db-dev.dmz:5432/seage", props);
        
    }

    public void close() throws SQLException
    {
        if (_conn != null)
            _conn.close();

    }

    protected String getVersionedValue(Document doc, String attName, String version) throws Exception
    {
        XmlHelper.XPath xPath = _versionedXPaths.get(version).get(attName);
        String val = "";
        if (xPath == null)
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
