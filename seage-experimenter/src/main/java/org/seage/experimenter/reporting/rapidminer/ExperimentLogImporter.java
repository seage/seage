package org.seage.experimenter.reporting.rapidminer;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.seage.thread.TaskRunner;
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
import com.rapidminer.repository.Repository;
import com.rapidminer.repository.RepositoryException;
import com.rapidminer.repository.RepositoryManager;
import com.rapidminer.repository.local.LocalRepository;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;

public class ExperimentLogImporter
{
	private static Logger _logger = Logger.getLogger(ExperimentLogImporter.class.getName());
	private String _logPath;
	private String _repoName;
	
	private final String RAPIDMINER_LOCAL_REPOSITORY_NAME = "seage";
	private final String RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH = "repository";
	
	private Hashtable<RMDataTableInfo, List<DataRow>> _rmDataTables;
	
	private Hashtable<String, Hashtable<String, XPath>> _xPaths;
	
	public ExperimentLogImporter(String logPath, String repoName) throws RepositoryException
	{
		_logPath = logPath;
		_repoName = repoName;
		
		_rmDataTables = new Hashtable<RMDataTableInfo, List<DataRow>>();
		
		RMDataTableInfo expValues = new RMDataTableInfo("ExperimentValues");
		expValues.Attributes.add(new RMAttributeInfo("ExperimentID", Ontology.NOMINAL, "/ExperimentTask/@experimentID"));
		expValues.Attributes.add(new RMAttributeInfo("ProblemID", Ontology.NOMINAL, "/ExperimentTask/Config/Problem/@id"));
		expValues.Attributes.add(new RMAttributeInfo("AlgorithmID", Ontology.NOMINAL, "/ExperimentTask/Config/Algorithm/@id"));
		expValues.Attributes.add(new RMAttributeInfo("InstanceID", Ontology.NOMINAL, "/ExperimentTask/Config/Problem/Instance/@name"));
		expValues.Attributes.add(new RMAttributeInfo("ConfigID", Ontology.NOMINAL, "/ExperimentTask/Config/@configID"));
		expValues.Attributes.add(new RMAttributeInfo("RunID", Ontology.NOMINAL, "/ExperimentTask/@runID"));
		expValues.Attributes.add(new RMAttributeInfo("InitSolutionValue", Ontology.REAL, "/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		expValues.Attributes.add(new RMAttributeInfo("SolutionValue", Ontology.REAL, "/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		expValues.Attributes.add(new RMAttributeInfo("NrOfSolutions", Ontology.REAL, "/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		expValues.Attributes.add(new RMAttributeInfo("LastIterNumberNewSol", Ontology.REAL, "/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		expValues.Attributes.add(new RMAttributeInfo("NrOfIterations", Ontology.REAL, "/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		
		_rmDataTables.put(expValues, new ArrayList<DataRow>());
		
		_xPaths = new Hashtable<String, Hashtable<String,XPath>>(); 
		
		Hashtable<String, XPath> v01 = new Hashtable<String, XPath>();
		v01.put("ExperimentID", new XPath("/ExperimentTask/@experimentID"));
		v01.put("ProblemID", new XPath("/ExperimentTask/Config/Problem/@id"));
		v01.put("AlgorithmID", new XPath("/ExperimentTask/Config/Algorithm/@id"));
		v01.put("InstanceID", new XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v01.put("ConfigID", new XPath("/ExperimentTask/Config/@configID"));
		v01.put("RunID", new XPath("/ExperimentTask/@runID"));
		v01.put("InitSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v01.put("SolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v01.put("NrOfSolutions", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v01.put("LastIterNumberNewSol", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v01.put("NrOfIterations", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XPath> v02 = new Hashtable<String, XPath>();
		v02.put("ExperimentID", new XPath("/ExperimentTask/@experimentID"));
		v02.put("ProblemID", new XPath("/ExperimentTask/Config/Problem/@problemID"));
		v02.put("AlgorithmID", new XPath("/ExperimentTask/Config/Algorithm/@algorithmID"));
		v02.put("InstanceID", new XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v02.put("ConfigID", new XPath("/ExperimentTask/Config/@configID"));
		v02.put("RunID", new XPath("/ExperimentTask/Config/@runID"));
		v02.put("InitSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v02.put("SolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v02.put("NrOfSolutions", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v02.put("LastIterNumberNewSol", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v02.put("NrOfIterations", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		_xPaths.put("0.1", v01);
		_xPaths.put("0.2", v02);
	}

	public void processLogs() throws OperatorException, OperatorCreationException, RepositoryException
	{
		Logger.getLogger(getClass().getName()).log(Level.INFO, "Processing experiment logs ...");

		long t0 = System.currentTimeMillis();

		File logDir = new File(_logPath);

		List<Runnable> tasks = new ArrayList<Runnable>();

		try
		{			
			for (File f : logDir.listFiles())
			{
				if (!f.getName().endsWith(".zip"))
					continue;				

				tasks.add(new ProcessZipTask(f));				
			}	
			
			new TaskRunner().runTasks(tasks, Runtime.getRuntime().availableProcessors());

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		writeDataTablesToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO,
				"Processing experiment logs DONE - " + t1 + "s");
	}
	
	private synchronized void importDocument(Document doc)
	{
		
		for(RMDataTableInfo tableInfo : _rmDataTables.keySet())
		{
			double[] valArray = new double[tableInfo.Attributes.size()];
			int i=0;
			for(RMAttributeInfo attInfo : tableInfo.Attributes)
			{ 
				XPath xPath = _xPaths.get("0.1").get(attInfo.Attribute.getName());
				String version = doc.getDocumentElement().getAttribute("version");
				if(!version.equals(""))
					xPath = _xPaths.get(version).get(attInfo.AttributeName);
				String val = getValueFromDocument(doc.getDocumentElement(), xPath.XPath);//(String) xpath.evaluate(attInfo.XPath, doc, XPathConstants.STRING);; //xpath
				//String val = (String) attInfo.XPath.evaluate( doc, XPathConstants.STRING);; //xpath
				if(attInfo.Type == Ontology.NOMINAL)
					valArray[i++] = attInfo.Attribute.getMapping().mapString(val);
				else
					valArray[i++] = Double.parseDouble(val);
			}
			
			_rmDataTables.get(tableInfo).add(new DoubleArrayDataRow(valArray));
		}

	}
	
	private String getValueFromDocument(Element elem, List<String> xPath) {
		if(xPath.size() == 1)
			return elem.getAttribute(xPath.get(0));
		else
		{
			String s = xPath.remove(0);
			return getValueFromDocument((Element)elem.getElementsByTagName(s).item(0), xPath);
		}
	}

	private void writeDataTablesToRepository() throws OperatorException, OperatorCreationException, RepositoryException
	{
		RapidMiner.setExecutionMode(RapidMiner.ExecutionMode.COMMAND_LINE);
		RapidMiner.init();
		
		initRepository();
		
		for(RMDataTableInfo tableInfo : _rmDataTables.keySet())
		{
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			for(RMAttributeInfo attInfo : tableInfo.Attributes)
				attributes.add(attInfo.Attribute);
			
			MemoryExampleTable memoryTable = new MemoryExampleTable(attributes);
			
			/* Save model */
			Operator modelWriter = OperatorService
					.createOperator(RepositoryStorer.class);
			modelWriter.setParameter(
					RepositoryStorer.PARAMETER_REPOSITORY_ENTRY,
					"//"+RAPIDMINER_LOCAL_REPOSITORY_NAME+"/databases/experiments/"+tableInfo.TableName);
	
			Process process = new Process();
	
			process.getRootOperator().getSubprocess(0).addOperator(modelWriter);
			process.getRootOperator()
					.getSubprocess(0)
					.getInnerSources()
					.getPortByIndex(0)
					.connectTo(
							modelWriter.getInputPorts().getPortByName("input"));
			
			memoryTable.readExamples(new ListDataRowReader(_rmDataTables.get(tableInfo).iterator()));
	
			process.run(new IOContainer(memoryTable.createExampleSet()));
		}
		
	}

	private void initRepository() throws RepositoryException
	{
		RepositoryManager rm = RepositoryManager.getInstance(null);

		List<Repository> reposToRemove = new ArrayList<Repository>();
		
		for(Repository repo : rm.getRepositories())
		{
			if(!(repo instanceof LocalRepository))
				continue;
			LocalRepository r = (LocalRepository)repo;
			if(r.getName().equals(RAPIDMINER_LOCAL_REPOSITORY_NAME))
			{
				if(!r.getFile().getPath().equals(RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH))
					reposToRemove.add(r);
					
			}
		}
		
		for(Repository repo : reposToRemove)
			rm.removeRepository(repo);			
		
		try
		{
			rm.getRepository(RAPIDMINER_LOCAL_REPOSITORY_NAME);
		}
		catch(RepositoryException re)
		{
			rm.addRepository(new LocalRepository( RAPIDMINER_LOCAL_REPOSITORY_NAME , new File( RAPIDMINER_LOCAL_REPOSITORY_DIR_PATH )));
		}
		
		rm.save();
		
	}
	
	private class RMDataTableInfo
	{
		public String TableName;
		public List<RMAttributeInfo> Attributes;
		
		public RMDataTableInfo(String tableName) {
			TableName = tableName;
			Attributes = new ArrayList<RMAttributeInfo>();
		}
		
	}
	private class RMAttributeInfo
	{
		public String AttributeName;
		public List<String> XPath;		// Java's XPath is too slow, this is a hack
		public int Type;
		public Attribute Attribute;
		
		public RMAttributeInfo(String attributeName, int type, String xPath) 
		{
			AttributeName = attributeName;
			Type = type;
			Attribute = AttributeFactory.createAttribute(attributeName, type);			
			
			XPath = new ArrayList<String>();
			String[] split = xPath.split("/"); 
			for(int i=2;i<split.length;i++) // i=2 -> skip '/ExperimentTask'
			{
				if(split[i].startsWith("@"))
					XPath.add(split[i].substring(1));
				else
					XPath.add(split[i]);
				
			}
		}
		
	}
	
	private class XPath
	{
		public List<String> XPath;

		public XPath(String xPath)
		{
			XPath = new ArrayList<String>();
			String[] split = xPath.split("/"); 
			for(int i=2;i<split.length;i++) // i=2 -> skip '/ExperimentTask'
			{
				if(split[i].startsWith("@"))
					XPath.add(split[i].substring(1));
				else
					XPath.add(split[i]);
				
			}
		}
		
	}
	
	private class ProcessZipTask implements Runnable
	{
		private File _zipFile;
		
		private ProcessZipTask(File zipFile)
		{

			_zipFile = zipFile;
		}

		@Override
		public void run()
		{
			try
			{
				Logger.getLogger(getClass().getName()).log(Level.INFO, Thread.currentThread().getName()+ " - importing file: " + _zipFile.getName());
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				//XPath xpath = XPathFactory.newInstance().newXPath();
				
				ZipFile zf = new ZipFile(_zipFile);
				
				for (Enumeration<? extends ZipEntry> e = zf.entries(); e.hasMoreElements();)
				{
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
			}
			catch(Exception ex)
			{
				_logger.log(Level.SEVERE, ex.getMessage());
			}
			
		}
		
	}
}
