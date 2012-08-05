package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.ListDataRowReader;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.io.RepositoryStorer;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;

public class ExperimentLogImporter
{
	private String _logPath;
	private String _repoName;
	
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "SEAGE-RM-REPO";
	private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "/home/rick/Temp/SEAGE-RM-REPO";
	
	MemoryExampleTable _memoryTable;
	private List<Attribute> _attributes;
	private List<DataRow> _rmDataRows = new ArrayList<DataRow>();

	public ExperimentLogImporter(String logPath, String repoName) throws RepositoryException
	{
		_logPath = logPath;
		_repoName = repoName;
		
		//String rapidMinerHome = "/home/rick/Temp/rm-test";
		//System.setProperty("rapidminer.home", rapidMinerHome);
		
		_attributes = new ArrayList<Attribute>();
		_attributes.add(AttributeFactory
				.createAttribute("ExperimentID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("ProblemID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("AlgorithmID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("InstanceID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("ConfigID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("RunID", Ontology.NOMINAL));
		_attributes.add(AttributeFactory
				.createAttribute("SolutionValue", Ontology.REAL));
		
	}

	public void processLogs() throws OperatorException, OperatorCreationException, RepositoryException
	{
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Processing experiment logs ...");

		long t0 = System.currentTimeMillis();

		File logDir = new File(_logPath);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();

			for (File f : logDir.listFiles())
			{
				if (!f.getName().endsWith(".zip"))
					continue;
				Logger.getLogger(getClass().getName()).log(Level.INFO, f.getName());

				int numFiles = 0;

				ZipFile zf = new ZipFile(f);
				
				for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
				{
					numFiles++;
					ZipEntry ze = e.nextElement();
					String name = ze.getName();
					if (name.endsWith(".xml"))
					{
						InputStream in = zf.getInputStream(ze);
						// read from 'in'
						try
						{
							Document doc = builder.parse(in);
							importDocument(doc);
						}
						catch (SAXException ex)
						{
							Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage());
						}
						// DataNode dn = XmlHelper.readXml(in);
					}
				}
				
				zf.close();
				
				Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO, "" + numFiles);
			}			

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		writeDataTableToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO,
				"Processing experiment logs DONE - " + t1 + "s");
	}
	
	private void importDocument(Document doc)
	{
		Element root = doc.getDocumentElement();
		Element config = (Element)root.getElementsByTagName("Config").item(0);
		Element problem = (Element)config.getElementsByTagName("Problem").item(0);
		Element instance = (Element)problem.getElementsByTagName("Instance").item(0);
		Element algorithm = (Element)config.getElementsByTagName("Algorithm").item(0);
		Element algReport = (Element)root.getElementsByTagName("AlgorithmReport").item(0);
		Element stats = (Element)algReport.getElementsByTagName("Statistics").item(0);
		
		double[] arr = new double[7];
		arr[0] = _attributes.get(0).getMapping().mapString( root.getAttribute("experimentID"));
		arr[1] = _attributes.get(1).getMapping().mapString( problem.getAttribute("id") );
		arr[2] = _attributes.get(2).getMapping().mapString( algorithm.getAttribute("id") );
		arr[3] = _attributes.get(3).getMapping().mapString( instance.getAttribute("name") );
		arr[4] = _attributes.get(4).getMapping().mapString( config.getAttribute("configID")  );
		arr[5] = _attributes.get(5).getMapping().mapString( root.getAttribute("runID") );
		arr[6] = Double.parseDouble(stats.getAttribute("bestObjVal"));
		
		_rmDataRows.add(new DoubleArrayDataRow(arr));

	}
	
	private void writeDataTableToRepository() throws OperatorException, OperatorCreationException, RepositoryException
	{
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RepositoryManager.getInstance(null).addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
		RapidMiner.init();
		
		
		_memoryTable = new MemoryExampleTable(_attributes);
		
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
		
		_memoryTable.readExamples(new ListDataRowReader(_rmDataRows.iterator()));

		process.run(new IOContainer(_memoryTable.createExampleSet()));
	}
}
