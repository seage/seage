package org.seage.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.RepositoryProcessLocation;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObjectCollection;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.RepositoryStorer;
import com.rapidminer.repository.Entry;
import com.rapidminer.repository.ProcessEntry;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryLocation;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.XMLException;

public class RapidMinerTest {
	private static final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "SEAGE-RM-REPO-old2";
    private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "/home/rick/Temp/SEAGE-RM-REPO-old2";

    public static void main(String[] args)
    {
    	System.out.println("RepositoryProcessLocation");
    	testRepositoryProcessLocation();
    }
    
    private static void testRepositoryProcessLocation()
    {
    	RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		//RepositoryManager rm = RepositoryManager.getInstance(null);
		try
        {
      
            RepositoryLocation location;
            RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
            RapidMiner.init();
            location = new RepositoryLocation("//seage/processes/base/BestAlgParamsPerAlgorithm-AttsRemoved");
            Entry entry = location.locateEntry();
            if (entry instanceof ProcessEntry) {
                Process process = new RepositoryProcessLocation(location).load(null);

                IOContainer ioResult = process.run();
                @SuppressWarnings("unchecked")
				IOObjectCollection<SimpleExampleSet> result = (IOObjectCollection<SimpleExampleSet> )ioResult.getElementAt(0);
                for(SimpleExampleSet c : result.getObjects())
                {
                	System.out.println(c.getAttributes().getId().getName());
                	
                	for(Attribute att : c.getAttributes())
                	{
                		System.out.println(att);
                	}
                	for(int i=0;i<c.getExampleTable().size();i++)
                	{
                		System.out.println(c.getExample(i));
                	}
                }
            }
        }
        catch(RepositoryException re)
        {
        	System.out.println("No such a repository: seage");
        }
		catch (IOException e)
		{			
			e.printStackTrace();
		}
		catch (XMLException e)
		{			
			e.printStackTrace();
		}
		catch (OperatorException e)
		{			
			e.printStackTrace();
		}
    }
    
	public void testCustomExampleSetOnInput() {
		
		try {
			//LogHelper.loadConfig("/home/rick/Projects/seage/debug-logging.properties");
			
			//String rapidMinerHome = "/home/rick/Temp/rm-test";
			//System.setProperty("rapidminer.home", "/opt/rapidminer-5.1.014");
			//System.setProperty(RapidMiner..PROPERTY_CONNECTIONS_FILE_XML, "false");
			//System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_INIT_PLUGINS, "false");
			//System.setProperty(RapidMiner, "false");
			//System.setProperty("rapidminer.home", rapidMinerHome);
			RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
			RepositoryManager rm = RepositoryManager.getInstance(null);
			try
	        {
	            rm.getRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME);
	        }
	        catch(RepositoryException re)
	        {
	            rm.addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
	        }			
			RapidMiner.init();

			Process process = new Process(new File(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH+"/proc2.rmp"));			
			process.run();
			IOContainer op= process.getRootOperator().getResults();
			SimpleExampleSet ses = (SimpleExampleSet)op.getElementAt(0);
			ses.getExample(0);
			//op.getElementAt(0)
			//Process process2 = new Process(new File());

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
public void testCustomExampleSetOnInput0() {
		
		try {
			
			String rapidMinerHome = "/home/rick/Temp/rm-test";
			System.setProperty("rapidminer.home", rapidMinerHome);
			RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
			RepositoryManager.getInstance(null).addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
			RapidMiner.init();
			
			List<Attribute> attributes = new ArrayList<Attribute>();
			attributes.add(AttributeFactory
					.createAttribute("a1", Ontology.REAL));
			attributes.add(AttributeFactory
					.createAttribute("a2", Ontology.REAL));
			MemoryExampleTable mex = new MemoryExampleTable(attributes);

			List<DataRow> drl = new ArrayList<DataRow>();
			drl.add(new DoubleArrayDataRow(new double[] { 0.11, 0.99 }));
			drl.add(new DoubleArrayDataRow(new double[] { 0.34, 0.78 }));
			drl.add(new DoubleArrayDataRow(new double[] { 0.44, 0.95 }));
			drl.add(new DoubleArrayDataRow(new double[] { 0.14, 0.11 }));

			mex.readExamples(new ListDataRowReader(drl.iterator()));

			/* Save model */
			Operator modelWriter = OperatorService
					.createOperator(RepositoryStorer.class);
			modelWriter.setParameter(
					RepositoryStorer.PARAMETER_REPOSITORY_ENTRY,
					"//"+RAPIDMINER_LOCAL_REPOSITORY_NAME+"/qqq");

			Process process = new Process();

			process.getRootOperator().getSubprocess(0).addOperator(modelWriter);
			process.getRootOperator()
					.getSubprocess(0)
					.getInnerSources()
					.getPortByIndex(0)
					.connectTo(
							modelWriter.getInputPorts().getPortByName("input"));

			process.run(new IOContainer(mex.createExampleSet()));
			
			//Process process2 = new Process(new File());

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
