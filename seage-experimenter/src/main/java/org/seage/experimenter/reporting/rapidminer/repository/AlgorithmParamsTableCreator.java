package org.seage.experimenter.reporting.rapidminer.repository;

import java.util.Hashtable;

import org.seage.data.xml.XmlHelper;
import org.w3c.dom.Document;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.tools.Ontology;

public abstract class AlgorithmParamsTableCreator extends RMDataTableCreator
{
    protected final String VERSION="0.1";
    private Hashtable<String, String> _rmAlgParamsConfigIDs;
    
    protected String _algorithmID;
    
    public AlgorithmParamsTableCreator(String repositoryPath, String tableName)
    {
        super(repositoryPath, tableName);
        _algorithmID = _tableName;
        _rmAlgParamsConfigIDs = new Hashtable<String, String>();
    }

    @Override
    public synchronized Boolean isInvolved(Document doc)
    {
        String algorithmID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
        if(!algorithmID.equals(_algorithmID))
            return false;
        
        String configID = XmlHelper.getValueFromDocument(doc.getDocumentElement(), new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
      
        if(_rmAlgParamsConfigIDs.containsKey(configID))
            return false;
        else
            _rmAlgParamsConfigIDs.put(configID, configID);
        return true;
    }

    @Override
    public synchronized void processDocument(Document doc) throws Exception
    {
        Hashtable<String, XmlHelper.XPath> xPaths = _versionedXPaths.get(VERSION);
                
        double[] valArray = new double[_attributes.size()];
        int i=0;
        
        
        for(Attribute att : _attributes)
        { 
            XmlHelper.XPath xPath = xPaths.get(att.getName());
            if(xPath == null)
            {
                _logger.warning("No XmlHelper.XPath defined for attribute: " + att.getName());
                continue;
            }
            String val = XmlHelper.getValueFromDocument(doc.getDocumentElement(), xPath);//(String) XmlHelper.XPath.evaluate(attInfo.XmlHelper.XPath, doc, XmlHelper.XPathConstants.STRING);; //XmlHelper.XPath
            if(val.equals(""))
                throw new Exception(xPath.XPathStr + " not found");
            if(att.getValueType() == Ontology.NOMINAL)
                valArray[i++] = att.getMapping().mapString(val);
            else
                valArray[i++] = Double.parseDouble(val);
        }
        
        _dataTable.add(new DoubleArrayDataRow(valArray));
        
    }
    
    public static class GeneticAlgorithm extends AlgorithmParamsTableCreator
    {
        public GeneticAlgorithm(String repositoryPath)
        {
            super(repositoryPath, "GeneticAlgorithm");
                 
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("CrossLengthPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("EliteSubjectPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("IterationCount", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("MutateLengthPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("MutateSubjectPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("NumSolutions", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("RandomSubjectPct", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algGAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algGAXPaths.put("RandomSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));
            algGAXPaths.put("CrossLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
            algGAXPaths.put("EliteSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
            algGAXPaths.put("IterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algGAXPaths.put("MutateLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
            algGAXPaths.put("MutateSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
            algGAXPaths.put("NumSolutions", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numSolutions"));

            _versionedXPaths.put(VERSION, algGAXPaths);
        }
    }
    
    public static class TabuSearch extends AlgorithmParamsTableCreator
    {
        public TabuSearch(String repositoryPath)
        {
            super(repositoryPath, "TabuSearch");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("NumIteration", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("TabuListLength", Ontology.REAL));

            Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("NumIteration", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
            algTSXPaths.put("TabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));

            _versionedXPaths.put(VERSION, algTSXPaths);
        }
    }
    
    public static class AntColony extends AlgorithmParamsTableCreator
    {
        public AntColony(String repositoryPath)
        {
            super(repositoryPath, "AntColony");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("Alpha", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("Beta", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("DefaultPheromone", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("IterationCount", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("NumberOfAnts", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("QantumOfPheromone", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algAntXPaths = new Hashtable<String, XmlHelper.XPath>();
            algAntXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algAntXPaths.put("Alpha", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@alpha"));
            algAntXPaths.put("Beta", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@beta"));
            algAntXPaths.put("DefaultPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@defaultPheromone"));
            algAntXPaths.put("IterationCount", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@iterationCount"));
            algAntXPaths.put("NumberOfAnts", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algAntXPaths.put("QantumOfPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@qantumOfPheromone"));

            _versionedXPaths.put(VERSION, algAntXPaths);
        }
    }
    
    public static class SimulatedAnnealing extends AlgorithmParamsTableCreator
    {
        public SimulatedAnnealing(String repositoryPath)
        {
            super(repositoryPath, "SimulatedAnnealing");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("AnnealCoeficient", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("MaxInnerIterations", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("MaxTemperature", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("MinTemperature", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("NumInnerSuccesses", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algSAXPaths.put("AnnealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("MaxInnerIterations", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxInnerIterations"));
            algSAXPaths.put("MaxTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("MinTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            algSAXPaths.put("NumInnerSuccesses", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numInnerSuccesses"));

            _versionedXPaths.put(VERSION, algSAXPaths);
        }
    }
}
