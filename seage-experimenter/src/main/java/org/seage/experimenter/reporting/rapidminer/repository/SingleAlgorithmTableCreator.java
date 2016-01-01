package org.seage.experimenter.reporting.rapidminer.repository;

import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.w3c.dom.Document;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.tools.Ontology;

public class SingleAlgorithmTableCreator extends RMDataTableCreator
{

    public SingleAlgorithmTableCreator(String repositoryPath, String tableName)
    {
        super(repositoryPath, tableName);

        _attributes.add(AttributeFactory.createAttribute("ExperimentType", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("ExperimentID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("ProblemID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("AlgorithmID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("InstanceID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("RunID", Ontology.NOMINAL));
        _attributes.add(AttributeFactory.createAttribute("InitSolutionValue", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("BestSolutionValue", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("NrOfNewSolutions", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("NrOfIterations", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("LastIterNumberNewSol", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("DurationInSeconds", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("TimeoutInSeconds", Ontology.REAL));
        _attributes.add(AttributeFactory.createAttribute("ComputerName", Ontology.NOMINAL));

        Hashtable<String, XmlHelper.XPath> v01 = new Hashtable<String, XmlHelper.XPath>();
        //v01.put("ExperimentType", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v01.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v01.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
        v01.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
        v01.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
        v01.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
        v01.put("RunID", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/@created"));
        v01.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
        v01.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
        v01.put("NrOfNewSolutions",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v01.put("LastIterNumberNewSol",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v01.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));

        Hashtable<String, XmlHelper.XPath> v02 = new Hashtable<String, XmlHelper.XPath>();
        //v02.put("ExperimentType", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v02.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/@experimentID"));
        v02.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
        v02.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
        v02.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
        v02.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
        v02.put("RunID", new XmlHelper.XPath("/ExperimentTask/@runID"));
        v02.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
        v02.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
        v02.put("NrOfNewSolutions",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v02.put("LastIterNumberNewSol",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v02.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));

        Hashtable<String, XmlHelper.XPath> v03 = new Hashtable<String, XmlHelper.XPath>();
        //v03.put("ExperimentType", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v03.put("ExperimentID", new XmlHelper.XPath("/ExperimentTask/@experimentID"));
        v03.put("ProblemID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/@id"));
        v03.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTask/Config/Algorithm/@id"));
        v03.put("InstanceID", new XmlHelper.XPath("/ExperimentTask/Config/Problem/Instance/@name"));
        v03.put("ConfigID", new XmlHelper.XPath("/ExperimentTask/Config/@configID"));
        v03.put("RunID", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v03.put("InitSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
        v03.put("BestSolutionValue", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
        v03.put("NrOfNewSolutions",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v03.put("LastIterNumberNewSol",
                new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v03.put("NrOfIterations", new XmlHelper.XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));

        Hashtable<String, XmlHelper.XPath> v04 = new Hashtable<String, XmlHelper.XPath>();
        //v04.put("ExperimentType", new XmlHelper.XPath("/ExperimentTask/Config/@runID"));
        v04.put("ExperimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v04.put("ProblemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v04.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
        v04.put("InstanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v04.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v04.put("RunID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v04.put("InitSolutionValue",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v04.put("BestSolutionValue",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v04.put("NrOfNewSolutions",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v04.put("LastIterNumberNewSol",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v04.put("NrOfIterations",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v04.put("DurationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v04.put("TimeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        v04.put("ComputerName", new XmlHelper.XPath("/ExperimentTaskReport/@machineName"));

        Hashtable<String, XmlHelper.XPath> v05 = new Hashtable<String, XmlHelper.XPath>();
        v05.put("ExperimentType", new XmlHelper.XPath("/ExperimentTaskReport/@experimentType"));
        v05.put("ExperimentID", new XmlHelper.XPath("/ExperimentTaskReport/@experimentID"));
        v05.put("ProblemID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
        v05.put("AlgorithmID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
        v05.put("InstanceID", new XmlHelper.XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
        v05.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
        v05.put("RunID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@runID"));
        v05.put("InitSolutionValue",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
        v05.put("BestSolutionValue",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
        v05.put("NrOfNewSolutions",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
        v05.put("LastIterNumberNewSol",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
        v05.put("NrOfIterations",
                new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
        v05.put("DurationInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@durationS"));
        v05.put("TimeoutInSeconds", new XmlHelper.XPath("/ExperimentTaskReport/@timeoutS"));
        v05.put("ComputerName", new XmlHelper.XPath("/ExperimentTaskReport/@machineName"));

        _versionedXPaths.put("0.1", v01);
        _versionedXPaths.put("0.2", v02);
        _versionedXPaths.put("0.3", v03);
        _versionedXPaths.put("0.4", v04);
        _versionedXPaths.put("0.5", v05);
    }

    @Override
    public synchronized boolean isInvolved(Document doc)
    {
        return true;
    }

    @Override
    public synchronized void processDocument(Document doc, String containerFileName) throws Exception
    {
        double[] valArray = new double[_attributes.size()];
        int i = 0;
        String version = getReportVersion(doc);

        for (Attribute attInfo : _attributes)
        {
            XmlHelper.XPath xPath = _versionedXPaths.get(version).get(attInfo.getName());
            String val = null;
            if (attInfo.getName().equals("ExperimentType") && xPath == null)
                val = "SingleAlgorithmRandom";
            else
            {
                if (xPath == null)
                {
                    _logger.warn(
                            doc.getLocalName() + ": No XmlHelper.XPath defined for attribute: " + attInfo.getName());
                    continue;
                }
                else
                {
                    val = XmlHelper.getValueFromDocument(doc.getDocumentElement(), xPath);//(String) XmlHelper.XPath.evaluate(attInfo.XmlHelper.XPath, doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
                    val = postProcessValue(val, version, attInfo);
                }
            }
            //String val = (String) attInfo.XmlHelper.XPath.evaluate( doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
            if (val.equals(""))
                throw new Exception(xPath.XPathStr + " not found");
            if (attInfo.getValueType() == Ontology.NOMINAL)
                valArray[i++] = attInfo.getMapping().mapString(val);
            else
                valArray[i++] = Double.parseDouble(val);
        }

        _dataTable.add(new DoubleArrayDataRow(valArray));
    }

    private String postProcessValue(String val, String version, Attribute attInfo)
    {
        if (/*version.equals("0.4") &&*/ attInfo.getName().equals("InstanceID") && val.contains("."))
            return val.split("\\.")[0];
        return val;
    }

    private String getReportVersion(Document doc)
    {
        String version = doc.getDocumentElement().getAttribute("version");
        if (version.equals("0.2"))
            return "0.4";
        if (!version.equals(""))
            return version;
        if (doc.getDocumentElement().getAttributes().getLength() == 0)
            return "0.1";
        if (doc.getDocumentElement().getAttributes().getLength() == 1)
            return "0.3";
        return "0.2";
    }

}
