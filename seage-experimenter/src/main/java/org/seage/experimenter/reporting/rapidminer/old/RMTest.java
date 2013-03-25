package org.seage.experimenter.reporting.rapidminer.old;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.io.RepositoryStorer;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.OutputPorts;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;

public class RMTest {
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "SEAGE-RM-REPO";
    private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "/home/rick/Temp/SEAGE-RM-REPO";

	public void testCustomExampleSetOnInput() {
		
		try {
			
			String rapidMinerHome = "/home/rick/Temp/rm-test";
			System.setProperty("rapidminer.home", rapidMinerHome);
			//System.setProperty(RapidMiner..PROPERTY_CONNECTIONS_FILE_XML, "false");
			System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_INIT_PLUGINS, "false");
			//System.setProperty(RapidMiner, "false");
			//System.setProperty("rapidminer.home", rapidMinerHome);
			RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
			RepositoryManager.getInstance(null).addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
			RapidMiner.init();

			Process process = new Process(new File(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH+"/proc2.rmp"));			
			process.run();
			IOContainer op= process.getRootOperator().getResults();
			SimpleExampleSet ses = (SimpleExampleSet)op.getElementAt(0);
			ses.getExample(0);
			//op.getElementAt(0)
			int a = 0;
			//Process process2 = new Process(new File());

		} catch (Exception e) {
			// TODO Auto-generated catch block
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
