package org.seage.experimenter.singlealgorithm.feedback;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.data.file.FileHelper;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.config.Configurator;

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

public class FeedbackConfigurator extends Configurator
{
	protected IOObjectCollection<SimpleExampleSet> _feedbackParams;
	
	@SuppressWarnings("unchecked")
	public FeedbackConfigurator() throws Exception
	{
		System.setProperty("rapidminer.home", ".");
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
        RapidMiner.init();        
             
        RepositoryLocation location = new RepositoryLocation("//seage/processes/base/BestAlgParamsPerAlgorithm-AttsRemoved");        
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
        	
//        	for(Attribute att : c.getAttributes())
//        	{
//        		System.out.println(att);
//        	}
//        	for(int i=0;i<c.getExampleTable().size();i++)
//        	{
//        		System.out.println(c.getExample(i));
//        	}
        }
		List<ProblemConfig> results = new ArrayList<ProblemConfig>();
        if(exampleSetParams==null)
        {
        	_logger.warning("There are no usable experiments in the repository.");
        	return results.toArray(new ProblemConfig[0]);
        }

        //System.out.println(instanceInfo.getValue("path"));
        int feedbackConfigs = exampleSetParams.size();//.getExampleTable().size();
        int realNumConfigs = numConfigs < feedbackConfigs ? numConfigs : feedbackConfigs; 
        for (int i = 0; i < realNumConfigs; i++)
        {        	
        	Example example = exampleSetParams.getExample(i);
            ProblemConfig config = createProblemConfig(problemInfo, instanceID , algorithmID);
            
            for (DataNode paramNode : problemInfo.getDataNode("Algorithms").getDataNodeById(algorithmID).getDataNodes("Parameter"))
            {            	
                String name = paramNode.getValueStr("name");
                Attribute att = exampleSetParams.getAttributes().get(name);                
                
                double val = example.getValue(att);
                
                config.getDataNode("Algorithm").getDataNode("Parameters").putValue(name, val);
            }

            config.putValue("configID", FileHelper.md5fromString(XmlHelper.getStringFromDocument(config.toXml())));
            results.add(config);
        }

        //

        return results.toArray(new ProblemConfig[0]);
	}
}
