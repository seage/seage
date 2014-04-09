package org.seage.experimenter.singlealgorithm.feedback;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.reporting.rapidminer.RapidMinerManager;
import org.seage.experimenter.singlealgorithm.random.RandomConfigurator;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.RepositoryProcessLocation;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObjectCollection;
import com.rapidminer.repository.Entry;
import com.rapidminer.repository.ProcessEntry;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.db.DBRepository;

public class FeedbackConfigurator extends Configurator
{
	protected IOObjectCollection<SimpleExampleSet> _feedbackParams;
	
	@SuppressWarnings("unchecked")
	public FeedbackConfigurator() throws Exception
	{
		RapidMinerManager.init();
		RapidMinerManager.initDatabaseConnection();

        RepositoryLocation location = new RepositoryLocation("//seage/processes/base/BestAlgParamsPerAlgorithm-AttsRemoved");        
        //RepositoryLocation location = new RepositoryLocation("//seage/processes/testDB");
        Entry entry = location.locateEntry();
        
        if (entry instanceof ProcessEntry) {
            Process process = new RepositoryProcessLocation(location).load(null);
            
            IOContainer ioResult = process.run();
            _feedbackParams = (IOObjectCollection<SimpleExampleSet> )ioResult.getElementAt(0);           
        }
        else 
        	throw new Exception("Can't find "+location.getPath()+" in RM's repository.");

	}
	
	@Override
	public ProblemConfig[] prepareConfigs(ProblemInfo problemInfo, String instanceID, String algorithmID, int numConfigs) throws Exception
	{		
		ExampleSet exampleSetParams = null;
		for(ExampleSet c : _feedbackParams.getObjects())
        {
        	if(c.getAttributes().getId().getName().equals(algorithmID))
        	{	
        		exampleSetParams = c;
        		break;
        	}
        }
		List<ProblemConfig> results = new ArrayList<ProblemConfig>();
        if(exampleSetParams==null)
        {
        	_logger.warning("There are no usable experiments in the repository, random param values generated.");
        	return new RandomConfigurator().prepareConfigs(problemInfo, instanceID, algorithmID, numConfigs);
        }

        //System.out.println(instanceInfo.getValue("path"));
        int feedbackConfigs = exampleSetParams.size();//.getExampleTable().size();
        int realNumConfigs = Math.min(numConfigs, feedbackConfigs);
        
        for (int i = 0; i < realNumConfigs; i++)
        {        	
        	Example example = exampleSetParams.getExample(i);
            ProblemConfig config = createProblemConfig(problemInfo, instanceID , algorithmID);
            
            for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter"))
            {            	
                String name = paramNode.getValueStr("name");
                Attribute att = exampleSetParams.getAttributes().get(name);                
                
                double val = example.getValue(att);
                if(name.equals("iterationCount") && Double.compare( val, Double.NaN)==0)
                	val = Double.MAX_VALUE;
                
                config.getDataNode("Algorithm").getDataNode("Parameters").putValue(name, val);
            }

            config.putValue("configID", FileHelper.md5fromString(XmlHelper.getStringFromDocument(config.toXml())));
            results.add(config);
        }
        
        if(realNumConfigs < numConfigs)
        {
        	int rndConfigs = numConfigs - realNumConfigs;  
        	ProblemConfig[] pc = new RandomConfigurator().prepareConfigs(problemInfo, instanceID, algorithmID, rndConfigs);
        	
        	for(int j=0;j<pc.length;j++)
        		results.add(pc[j]);
        }
        //
        return results.toArray(new ProblemConfig[0]);
	}
}
