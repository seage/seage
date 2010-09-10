package jssp;

import ailibrary.data.*;
import jssp.data.*;
import jssp.algorithm.*;
import jssp.algorithm.genetics.*;
import ailibrary.*;
import jssp.algorithm.tabusearch.*;
//import ailibrary2.zedgraph.*;

/**
 * Summary description for Program
 */
public class Program
{
	private static String DATA_PATH = "D:\\_diplom\\Thesis\\Data\\Experiments\\Benchmarks\\SAT\\";
	//private static String CONFIG_PATH = "D:\\_diplom\\AI3S\\AILibrary2\\AILibrary2\\data\\";
        private static String CONFIG_PATH = "C:\\Documents and Settings\\Rick\\Desktop\\AI3S2\\AILibrary2\\data\\";
	private static String CONFIG_PATH_GS = CONFIG_PATH + "configGS.xml";
	private static String CONFIG_PATH_GS2 = CONFIG_PATH + "configGS2.xml";
	private static String CONFIG_PATH_GS1 = CONFIG_PATH + "configGS";
	private static String CONFIG_PATH_TS = CONFIG_PATH + "configTS.xml";
	private static String CONFIG_PATH_TS1 = CONFIG_PATH + "configTS";
	private static String CONFIG_PATH_BOTH = CONFIG_PATH + "configBoth.xml";
	private static String CONFIG_PATH_BOTH2 = CONFIG_PATH + "configBoth2.xml";
	private static String CONFIG_PATH_STRATEGY = CONFIG_PATH + "configStrategy.xml";

	private static String[] configs = { CONFIG_PATH_GS, CONFIG_PATH_GS2, CONFIG_PATH_TS, CONFIG_PATH_BOTH, CONFIG_PATH_STRATEGY };
	private static int[] useConfig = {0, 0, 0, 0, 1 };

	String _dataName = "";
	private double[] _results;
	private long[] _time;

	public Program()
	{
		_results = new double[1];
		_time = new long[_results.length];
	}
	
	public static void main(String[] args)
	{
		try
		{
			//if (args.length != 2)
			//    throw new Exception("Wrong number of arguments");
			//DATA_PATH = args[0];
			//CONFIG_PATH = args[1];
			Program self = new Program();
			self.run();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void printStatistics()
	{
		double avg = 0;
		double avgTime = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		double var = 0;
		double dev = 0;
		for (int i = 0; i < _results.length; i++)
		{
			avg += _results[i];
			avgTime += _time[i];
			if (_results[i] > max) max = _results[i];
			if (_results[i] < min) min = _results[i];
		}
		avg /= _results.length;
		avgTime /= _results.length;
		for (int i = 0; i < _results.length; i++)
		{
			var += Math.pow(_results[i] - avg, 2);
		}
		dev = var;
		dev = Math.sqrt(dev / (_results.length - 1));
		var /= _results.length;

		System.out.println("======================");
		System.out.println(_dataName);
		System.out.println("======================");
		System.out.println("Min:\t\t " + min);
		System.out.println("Average:\t " + avg);
		System.out.println("Variance:\t " + var);
		System.out.println("Deviation:\t " + dev);
		System.out.println("Max:\t\t " + max);
		System.out.println("Avg. time:\t " + (avgTime) / 1000.0);
	}

	private double getBestSolution()
	{
		DataStore ds = DataStore.getInstance();
		double[] res = (double[])ds.getDataTable("solutions").getRow(0).getRowProperty();
		System.out.println(res[0]);
		return res[0];
	}

	private void run() throws Exception
	{
		try
		{
			//String path = "D:\\__bordel\\_Richard\\jssp_bench.xml";
			//String path = "D:\\__bordel\\_Richard\\jssp_bench-ft10x10.xml";
			//String path = "D:\\__bordel\\_Richard\\jssp_data2.xml";			
			//String path = "D:\\_diplom\\AI3S\\JSSP\\data\\swv20.xml";

			//String path = "D:\\__bordel\\_Richard\\JSSP\\benchmark\\data\\ft10.xml";
			//String path = "D:\\__bordel\\_Richard\\JSSP\\benchmark\\data\\la22.xml";			
			//String path = "D:\\__bordel\\_Richard\\JSSP\\benchmark\\data\\yn1.xml
                        String path = "C:\\Documents and Settings\\Rick\\Desktop\\AI3S2\\JSSP\\data\\benchmark\\la22.xml";

			//String path = "D:\\__bordel\\_Richard\\JSSP\\benchmark\\data\\yn2.xml";
			//String path = DATA_PATH;
			_dataName = path.substring(path.lastIndexOf("\\") + 1);
			_dataName = _dataName.substring(0, _dataName.lastIndexOf('.'));
			//ZedGraphMaker.getInstance().setTitle("JSSP - " + _dataName);


			DataManager dm = new DataManager();


			SearchCoordinator sc = null;
			int ix = 1;
			for (int j = 0; j < _results.length; j++)
				for (int i = 0; i < 5; i++)
				{
					if (useConfig[i] == 0)
						continue;

					dm.initDataStore(path);
					ScheduleManager sm = new ScheduleManager();

					//if (j == 1)
					//    sc = new SearchCoordinator(configs[2]);
					//else if (j == 2)
					//    sc = new SearchCoordinator(configs[3]);
					//else
					//    sc = new SearchCoordinator(configs[i]);

					sc = new SearchCoordinator(configs[i]);
					//sc = new SearchCoordinator(configs[i] + ix + ".xml");
					//sc = new SearchCoordinator(CONFIG_PATH);

					JsspEvaluator evaluator = new JsspEvaluator(sm);
					JsspOperator operator = new JsspOperator(dm.getNumOper());
					JsspGeneticSearchAdapter jgsa = new JsspGeneticSearchAdapter(operator, evaluator, false, "");

					JsspMoveManager moveManager = new JsspMoveManager(sm);
					JsspObjectiveFunction objectiveFunction = new JsspObjectiveFunction(sm);
					JsspLongTermMemory longTermMemory = new JsspLongTermMemory();
					JsspTabuSearchAdapter jtsa = new JsspTabuSearchAdapter(moveManager, objectiveFunction, longTermMemory, false, "");

					//objectiveFunction.evaluate((JsspSolution)jtsa.loadSolution()[0],new JsspMove(0,6));
					
					sc.putAlgorithmAdapter("GeneticSearch", jgsa);
					sc.putAlgorithmAdapter("TabuSearch", jtsa);

					long t1 = System.currentTimeMillis();
					sc.search();
					long t2 = System.currentTimeMillis();
					_results[ix-1] = getBestSolution();
					_time[ix-1] = t2 - t1;
					ix++;
				}
			printStatistics();
			//sc.createGraph("");
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
}
