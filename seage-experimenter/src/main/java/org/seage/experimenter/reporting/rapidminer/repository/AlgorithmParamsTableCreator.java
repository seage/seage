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
            _attributes.add(AttributeFactory.createAttribute("crossLengthPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("eliteSubjectPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("iterationCount", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("mutateLengthPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("mutateSubjectPct", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("numSolutions", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("randomSubjectPct", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algGAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algGAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algGAXPaths.put("randomSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@randomSubjectPct"));
            algGAXPaths.put("crossLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@crossLengthPct"));
            algGAXPaths.put("eliteSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@eliteSubjectPct"));
            algGAXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@iterationCount"));
            algGAXPaths.put("mutateLengthPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateLengthPct"));
            algGAXPaths.put("mutateSubjectPct", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@mutateSubjectPct"));
            algGAXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));

            _versionedXPaths.put(VERSION, algGAXPaths);
        }
    }
    
    public static class TabuSearch extends AlgorithmParamsTableCreator
    {
        public TabuSearch(String repositoryPath)
        {
            super(repositoryPath, "TabuSearch");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("numIteration", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("tabuListLength", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("numSolutions", Ontology.REAL));

            Hashtable<String, XmlHelper.XPath> algTSXPaths = new Hashtable<String, XmlHelper.XPath>();
            algTSXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algTSXPaths.put("numIteration", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@numIteration"));
            algTSXPaths.put("tabuListLength", new XmlHelper.XPath("/ExperimentTaskReport/AlgorithmReport/Parameters/@tabuListLength"));
            algTSXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            
            _versionedXPaths.put(VERSION, algTSXPaths);
        }
    }
    
    public static class AntColony extends AlgorithmParamsTableCreator
    {
        public AntColony(String repositoryPath)
        {
            super(repositoryPath, "AntColony");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("alpha", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("beta", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("defaultPheromone", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("iterationCount", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("numSolutions", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("qantumOfPheromone", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algAntXPaths = new Hashtable<String, XmlHelper.XPath>();
            algAntXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algAntXPaths.put("alpha", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@alpha"));
            algAntXPaths.put("beta", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@beta"));
            algAntXPaths.put("defaultPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@defaultPheromone"));
            algAntXPaths.put("iterationCount", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@iterationCount"));
            algAntXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            algAntXPaths.put("qantumOfPheromone", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@qantumOfPheromone"));

            _versionedXPaths.put(VERSION, algAntXPaths);
        }
    }
    
    public static class SimulatedAnnealing extends AlgorithmParamsTableCreator
    {
        public SimulatedAnnealing(String repositoryPath)
        {
            super(repositoryPath, "SimulatedAnnealing");
            
            _attributes.add(AttributeFactory.createAttribute("ConfigID", Ontology.NOMINAL));
            _attributes.add(AttributeFactory.createAttribute("annealCoeficient", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("maxInnerIterations", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("maxTemperature", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("minTemperature", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("numInnerSuccesses", Ontology.REAL));
            _attributes.add(AttributeFactory.createAttribute("numSolutions", Ontology.REAL));
            
            Hashtable<String, XmlHelper.XPath> algSAXPaths = new Hashtable<String, XmlHelper.XPath>();
            algSAXPaths.put("ConfigID", new XmlHelper.XPath("/ExperimentTaskReport/Config/@configID"));
            algSAXPaths.put("annealCoeficient", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@annealCoeficient"));
            algSAXPaths.put("maxInnerIterations", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxInnerIterations"));
            algSAXPaths.put("maxTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@maxTemperature"));
            algSAXPaths.put("minTemperature", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@minTemperature"));
            algSAXPaths.put("numInnerSuccesses", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numInnerSuccesses"));
            algSAXPaths.put("numSolutions", new XmlHelper.XPath("/ExperimentTaskReport/Config/Algorithm/Parameters/@numSolutions"));
            _versionedXPaths.put(VERSION, algSAXPaths);
        }
    }
}
