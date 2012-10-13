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
		expValues.Attributes.add(new RMAttributeInfo("ExperimentID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("ProblemID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("AlgorithmID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("InstanceID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("ConfigID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("RunID", Ontology.NOMINAL));
		expValues.Attributes.add(new RMAttributeInfo("InitSolutionValue", Ontology.REAL));
		expValues.Attributes.add(new RMAttributeInfo("BestSolutionValue", Ontology.REAL));
		expValues.Attributes.add(new RMAttributeInfo("NrOfNewSolutions", Ontology.REAL));
		expValues.Attributes.add(new RMAttributeInfo("NrOfIterations", Ontology.REAL));
		expValues.Attributes.add(new RMAttributeInfo("LastIterNumberNewSol", Ontology.REAL));
		
		_rmDataTables.put(expValues, new ArrayList<DataRow>());
		
		_xPaths = new Hashtable<String, Hashtable<String,XPath>>(); 
		
		Hashtable<String, XPath> v01 = new Hashtable<String, XPath>();
		v01.put("ExperimentID", new XPath("/ExperimentTask/Config/@runID"));
		v01.put("ProblemID", new XPath("/ExperimentTask/Config/Problem/@id"));
		v01.put("AlgorithmID", new XPath("/ExperimentTask/Config/Algorithm/@id"));
		v01.put("InstanceID", new XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v01.put("ConfigID", new XPath("/ExperimentTask/Config/@configID"));
		v01.put("RunID", new XPath("/ExperimentTask/AlgorithmReport/@created"));
		v01.put("InitSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v01.put("BestSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v01.put("NrOfNewSolutions", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v01.put("LastIterNumberNewSol", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v01.put("NrOfIterations", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XPath> v02 = new Hashtable<String, XPath>();
		v02.put("ExperimentID", new XPath("/ExperimentTask/@experimentID"));
		v02.put("ProblemID", new XPath("/ExperimentTask/Config/Problem/@id"));
		v02.put("AlgorithmID", new XPath("/ExperimentTask/Config/Algorithm/@id"));
		v02.put("InstanceID", new XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v02.put("ConfigID", new XPath("/ExperimentTask/Config/@configID"));
		v02.put("RunID", new XPath("/ExperimentTask/@runID"));
		v02.put("InitSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v02.put("BestSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v02.put("NrOfNewSolutions", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v02.put("LastIterNumberNewSol", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v02.put("NrOfIterations", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XPath> v03 = new Hashtable<String, XPath>();
		v03.put("ExperimentID", new XPath("/ExperimentTask/@experimentID"));
		v03.put("ProblemID", new XPath("/ExperimentTask/Config/Problem/@id"));
		v03.put("AlgorithmID", new XPath("/ExperimentTask/Config/Algorithm/@id"));
		v03.put("InstanceID", new XPath("/ExperimentTask/Config/Problem/Instance/@name"));
		v03.put("ConfigID", new XPath("/ExperimentTask/Config/@configID"));
		v03.put("RunID", new XPath("/ExperimentTask/Config/@runID"));
		v03.put("InitSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@initObjVal"));
		v03.put("BestSolutionValue", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@bestObjVal"));
		v03.put("NrOfNewSolutions", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v03.put("LastIterNumberNewSol", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v03.put("NrOfIterations", new XPath("/ExperimentTask/AlgorithmReport/Statistics/@numberOfIter"));
		
		Hashtable<String, XPath> v04 = new Hashtable<String, XPath>();
		v04.put("ExperimentID", new XPath("/ExperimentTaskReport/@experimentID"));
		v04.put("ProblemID", new XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
		v04.put("AlgorithmID", new XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
		v04.put("InstanceID", new XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
		v04.put("ConfigID", new XPath("/ExperimentTaskReport/Config/@configID"));
		v04.put("RunID", new XPath("/ExperimentTaskReport/Config/@runID"));
		v04.put("InitSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
		v04.put("BestSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
		v04.put("NrOfNewSolutions", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
		v04.put("LastIterNumberNewSol", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
		v04.put("NrOfIterations", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
		
//		Hashtable<String, XPath> v04 = new Hashtable<String, XPath>();
//		v04.put("ExperimentID", new XPath("/ExperimentTaskReport/@experimentID"));
//		v04.put("ProblemID", new XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
//		v04.put("AlgorithmID", new XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
//		v04.put("InstanceID", new XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
//		v04.put("ConfigID", new XPath("/ExperimentTaskReport/Config/@configID"));
//		v04.put("RunID", new XPath("/ExperimentTaskReport/Config/@runID"));
//		v04.put("InitSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
//		v04.put("BestSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
//		v04.put("NrOfNewSolutions", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
//		v04.put("LastIterNumberNewSol", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
//		v04.put("NrOfIterations", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));		
//		
//		Hashtable<String, XPath> v05 = new Hashtable<String, XPath>();
//		v05.put("ExperimentID", new XPath("/ExperimentTaskReport/@experimentID"));
//		v05.put("ProblemID", new XPath("/ExperimentTaskReport/Config/Problem/@problemID"));
//		v05.put("AlgorithmID", new XPath("/ExperimentTaskReport/Config/Algorithm/@algorithmID"));
//		v05.put("InstanceID", new XPath("/ExperimentTaskReport/Config/Problem/Instance/@name"));
//		v05.put("ConfigID", new XPath("/ExperimentTaskReport/Config/@configID"));
//		v05.put("RunID", new XPath("/ExperimentTaskReport/Config/@runID"));
//		v05.put("InitSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@initObjVal"));
//		v05.put("BestSolutionValue", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@bestObjVal"));
//		v05.put("NrOfNewSolutions", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfNewSolutions"));
//		v05.put("LastIterNumberNewSol", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@lastIterNumberNewSol"));
//		v05.put("NrOfIterations", new XPath("/ExperimentTaskReport/AlgorithmReport/Statistics/@numberOfIter"));
		
		_xPaths.put("0.1", v01);
		_xPaths.put("0.2", v02);
		_xPaths.put("0.3", v03);
		_xPaths.put("0.4", v04);
//		_xPaths.put("0.5", v05);
	}

	public void processLogs() throws OperatorException, OperatorCreationException, RepositoryException
	{
	    _logger.info("Processing experiment logs ...");

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
			_logger.severe(ex.getMessage());
		}
		
		writeDataTablesToRepository();

		long t1 = (System.currentTimeMillis() - t0) / 1000;

		Logger.getLogger(ProcessPerformer.class.getName()).log(Level.INFO,
				"Processing experiment logs DONE - " + t1 + "s");
	}
	
	private synchronized void importDocument(Document doc) throws Exception
	{
		
		for(RMDataTableInfo tableInfo : _rmDataTables.keySet())
		{
			double[] valArray = new double[tableInfo.Attributes.size()];
			int i=0;
			String version = getReportVersion(doc);
			
			for(RMAttributeInfo attInfo : tableInfo.Attributes)
			{ 
				XPath xPath = _xPaths.get(version).get(attInfo.AttributeName);
				String val = getValueFromDocument(doc.getDocumentElement(), xPath.XPath);//(String) xpath.evaluate(attInfo.XPath, doc, XPathConstants.STRING);; //xpath
				//String val = (String) attInfo.XPath.evaluate( doc, XPathConstants.STRING);; //xpath
				if(val.equals(""))
					throw new Exception(xPath.XPathStr + " not found");
				if(attInfo.Type == Ontology.NOMINAL)
					valArray[i++] = attInfo.Attribute.getMapping().mapString(val);
				else
					valArray[i++] = Double.parseDouble(val);
			}
			
			_rmDataTables.get(tableInfo).add(new DoubleArrayDataRow(valArray));
		}

	}
	
	private String getReportVersion(Document doc)
	{	
		String version = doc.getDocumentElement().getAttribute("version");
		if(version.equals("0.2"))
			return "0.4";
		if(!version.equals(""))
			return version;
		if(doc.getDocumentElement().getAttributes().getLength()==0)
			return "0.1";
		if(doc.getDocumentElement().getAttributes().getLength()==1)
			return "0.3";
		return "0.2";
	}

	private String getValueFromDocument(Element elem, List<String> xPath) {
		return getValueFromDocument(elem, xPath, 0);
	}
	
	private String getValueFromDocument(Element elem, List<String> xPath, int level) {
		if(level+1 == xPath.size())
			return elem.getAttribute(xPath.get(level));
		else
		{
			String s = xPath.get(level);
			return getValueFromDocument((Element)elem.getElementsByTagName(s).item(0), xPath, level+1);
		}
	}

	private void writeDataTablesToRepository() throws OperatorException, OperatorCreationException, RepositoryException
	{
		System.setProperty("rapidminer.home", ".");
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
		_logger.fine("initRepository");
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
		
		_logger.fine("initRepository: saving");
		rm.save();
		_logger.fine("initRepository: done");
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
		//public List<String> XPath;		// Java's XPath is too slow, this is a hack
		public int Type;
		public Attribute Attribute;
		
		public RMAttributeInfo(String attributeName, int type) 
		{
			AttributeName = attributeName;
			Type = type;
			Attribute = AttributeFactory.createAttribute(attributeName, type);			
			
//			XPath = new ArrayList<String>();
//			String[] split = xPath.split("/"); 
//			for(int i=2;i<split.length;i++) // i=2 -> skip '/ExperimentTask'
//			{
//				if(split[i].startsWith("@"))
//					XPath.add(split[i].substring(1));
//				else
//					XPath.add(split[i]);
//				
//			}
		}
		
	}
	
	private class XPath						// Java's XPath is too slow, this is a hack
	{
		public String XPathStr;
		public List<String> XPath;

		public XPath(String xPath)
		{
			XPathStr = xPath;
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
				_logger.info(Thread.currentThread().getName()+ " - importing file: " + _zipFile.getName());
				
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
						catch (Exception ex)
						{
						    _logger.warning(name+" - "+ex.getMessage()+" - " +ex.toString());
						}
						// DataNode dn = XmlHelper.readXml(in);
					}
				}
				
				zf.close();
			}
			catch(Exception ex)
			{
				_logger.warning(ex.getMessage()+": "+_zipFile.getName());
			}
			
		}
		
	}
}
