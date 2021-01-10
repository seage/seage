package org.seage.knowledgebase.importing.db.tablecreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.seage.knowledgebase.importing.IDocumentProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class ExperimentTasksTableCreator extends DataTableCreator implements IDocumentProcessor {
  private static Logger _logger = LoggerFactory.getLogger(ExperimentTasksTableCreator.class.getName());

  public ExperimentTasksTableCreator(String dbPath) throws Exception {
    super(dbPath);

    Hashtable<String, XmlHelper.XPath> v04 = new Hashtable<String, XmlHelper.XPath>();
    v04.put("experimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
    v04.put("problemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
    v04.put("instanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
    v04.put("algorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
    v04.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
    v04.put("runID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
    v04.put("initSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
    v04.put("bestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
    v04.put("nrOfNewSolutions",
        new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
    v04.put("lastIterNumberNewSol",
        new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
    v04.put("nrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
    v04.put("durationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
    v04.put("timeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));

    Hashtable<String, XmlHelper.XPath> v05 = new Hashtable<String, XmlHelper.XPath>();
    v05.put("experimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
    v05.put("problemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
    v05.put("instanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
    v05.put("algorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
    v05.put("configID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
    v05.put("runID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
    v05.put("initSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
    v05.put("bestSolutionValue", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
    v05.put("nrOfNewSolutions",
        new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
    v05.put("lastIterNumberNewSol",
        new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
    v05.put("nrOfIterations", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
    v05.put("durationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
    v05.put("timeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));

    _versionedXPaths.put("0.4", v04);
    _versionedXPaths.put("0.5", v05);
    _versionedXPaths.put("0.6", v05);

    String queryCreate = "CREATE TABLE IF NOT EXISTS ExperimentTasks" + "(" + "fileName VARCHAR,"
        + "experimentID VARCHAR," + "problemID VARCHAR," + "instanceID VARCHAR," + "algorithmID VARCHAR,"
        + "configID VARCHAR," + "runID VARCHAR," + "initSolutionValue DOUBLE PRECISION,"
        + "bestSolutionValue DOUBLE PRECISION," + "nrOfNewSolutions DOUBLE PRECISION,"
        + "nrOfIterations DOUBLE PRECISION," + "lastIterNumberNewSol DOUBLE PRECISION,"
        + "durationInSeconds DOUBLE PRECISION," + "timeoutInSeconds REAL" + ")";

    try (Connection _conn = createConnection("")) {
      Statement stmt = _conn.createStatement();
      stmt.execute(queryCreate);
    } catch (SQLException ex) {
      _logger.error(ex.getMessage(), ex);
    }

  }

  @Override
  public boolean isInvolved(Document doc) {
    return true;
  }

  @Override
  public synchronized void processDocument(Document doc, String containerFileName) throws Exception {
    String version = doc.getDocumentElement().getAttribute("version");
    if (version.compareToIgnoreCase("0.4") < 0)
      throw new Exception("Unsupported version: " + version);
    String insertQuery = "INSERT INTO ExperimentTasks VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try (Connection conn = createConnection("")) {
      PreparedStatement stmt = conn.prepareStatement(insertQuery);
      stmt.clearParameters();
      stmt.setString(1, containerFileName);
      stmt.setString(2, getVersionedValue(doc, "experimentID", version));
      stmt.setString(3, getVersionedValue(doc, "problemID", version));
      stmt.setString(4, getVersionedValue(doc, "instanceID", version));
      stmt.setString(5, getVersionedValue(doc, "algorithmID", version));
      stmt.setString(6, getVersionedValue(doc, "configID", version));
      stmt.setString(7, getVersionedValue(doc, "runID", version));
      stmt.setDouble(8, Double.parseDouble(getVersionedValue(doc, "initSolutionValue", version)));
      stmt.setDouble(9, Double.parseDouble(getVersionedValue(doc, "bestSolutionValue", version)));
      stmt.setDouble(10, Double.parseDouble(getVersionedValue(doc, "nrOfNewSolutions", version)));
      stmt.setDouble(11, Double.parseDouble(getVersionedValue(doc, "nrOfIterations", version)));
      stmt.setDouble(12, Double.parseDouble(getVersionedValue(doc, "lastIterNumberNewSol", version)));
      stmt.setDouble(13, Double.parseDouble(getVersionedValue(doc, "durationInSeconds", version)));
      stmt.setDouble(14, Double.parseDouble(getVersionedValue(doc, "timeoutInSeconds", version)));

      stmt.executeUpdate();
    } catch (SQLException ex) {
      _logger.error(ex.getMessage(), ex);
    }

  }

  @Override
  protected String postProcessValue(String attName, String val, String version) {
    if (/* version.equals("0.4") && */ attName.equals("instanceID") && val.contains("."))
      return val.split("\\.")[0];
    return val;
  }
}
