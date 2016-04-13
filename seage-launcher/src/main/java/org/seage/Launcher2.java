package org.seage;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.experimenter.reporting.h2.ExperimentDataH2Importer;
import org.seage.experimenter.singlealgorithm.evolution.SingleAlgorithmEvolutionExperimenter;
import org.seage.experimenter.singlealgorithm.feedback.SingleAlgorithmFeedbackExperimenter;
import org.seage.experimenter.singlealgorithm.random.SingleAlgorithmRandomExperimenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;


abstract class Command
{
	public abstract void performCommad() throws Exception ;
}

public class Launcher2 
{
	private static final Logger _logger = LoggerFactory.getLogger(Launcher.class.getName());

	@Parameter(names = "--help", help = true)
	private boolean help;

	public static void main(String[] args) 
	{
		try
		{
			HashMap<String, Command> commands = new LinkedHashMap<>();
			commands.put("list", new ListCommand());
			commands.put("report", new ReportCommand());
			commands.put("experiment-single-random", new ExperimentSingleRandomCommand());
			commands.put("experiment-single-feedback", new ExperimentSingleFeedbackCommand());
			commands.put("experiment-single-evolution", new ExperimentSingleEvolutionCommand());
			
			Launcher2 launcher = new Launcher2();
			
			JCommander jc = new JCommander(launcher);
			for(Entry<String, Command> e : commands.entrySet())
				jc.addCommand(e.getKey(), e.getValue());
			jc.parse(args);
			if(args.length == 0 || launcher.help)
			{
				jc.usage();
				return;
			}		
			launcher.run(commands.get(jc.getParsedCommand()));
		}
		catch(Exception ex)
		{
			_logger.error(ex.getMessage(), ex);
		}
	}

	private void run(Command cmd) throws Exception
	{			
		cmd.performCommad();
	}
}

@Parameters(commandDescription = "List implemented problems and algorithms")
class ListCommand extends Command 
{
    private static final Logger _logger = LoggerFactory.getLogger(ListCommand.class.getName());
	@Override
	public void performCommad() throws Exception 
	{
	    _logger.info("List of implemented problems and algorithms:");
        _logger.info("--------------------------------------------");

        DataNode problems = new DataNode("Problems");
        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for (String problemId : providers.keySet())
        {
            try
            {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                problems.putDataNode(pi);

                String name = pi.getValueStr("name");
                _logger.info(problemId + " - " + name);

                _logger.info("\talgorithms:");
                for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
                {
                    _logger.info("\t\t" + alg.getValueStr("id")/*
                                                               * +" ("+alg.getValueStr("id")+")"
                                                               */);

                    //_logger.info("\t\t\tparameters:");
                    for (DataNode param : alg.getDataNodes("Parameter"))
                    {
                        _logger.info("\t\t\t"
                                + param.getValueStr("name") + "  ("
                                + param.getValueStr("min") + ", "
                                + param.getValueStr("max") + ", "
                                + param.getValueStr("init") + ")");
                    }
                }
                _logger.info("\tinstances:");
                for (DataNode inst : pi.getDataNode("Instances").getDataNodes())
                {
                    _logger.info("\t\t" + inst.getValueStr("type") + "=" + inst.getValueStr("path")/*
                                                                                                   * +" ("+alg.getValueStr("id")+")"
                                                                                                   */);
                }

                _logger.info("");
            }
            catch (Exception ex)
            {
                _logger.error( problemId + ": " + ex.getMessage(), ex);
            }
            //XmlHelper.writeXml(problems, "problems.xml");
        }	}
}

@Parameters(commandDescription = "Process experiment output data and add records to db")
class ReportCommand extends Command 
{
	@Parameter(names = "--clean", description = "Drop all db data, create all tables and fill them by new data")
	private boolean clean;
	
	@Override
	public void performCommad() throws Exception 
	{
		new ExperimentDataH2Importer("output/experiment-logs", "database/seage", clean).processLogs();
	}
}

@Parameters( commandDescription = "Perform single random experiment")
class ExperimentSingleRandomCommand extends Command
{
    @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
    int numOfConfigs;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;
    @Parameter(names = "-p", required = true, description = "ProblemID")
    String problemID;
    @Parameter(names = "-i", required = true, description = "Problem instances", variableArity = true)
    List<String> instances;
    @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
    List<String> algorithms;
	@Override
	public void performCommad() throws Exception 
	{
	    new SingleAlgorithmRandomExperimenter(numOfConfigs, algorithmTimeoutS)
	    .runExperiment(problemID, instances.toArray(new String[]{}), algorithms.toArray(new String[]{}));		
	}	
}

@Parameters( commandDescription = "Perform single feedback experiment")
class ExperimentSingleFeedbackCommand extends Command
{
    @Parameter(names = "-n", required = true, description = "Number of configs per each experiment")
    int numOfConfigs;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;
    @Parameter(names = "-p", required = true, description = "ProblemID")
    String problemID;
    @Parameter(names = "-i", required = true, description = "Problem instances", variableArity = true)
    List<String> instances;
    @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
    List<String> algorithms;
    @Override
    public void performCommad() throws Exception 
    {
        new SingleAlgorithmFeedbackExperimenter(numOfConfigs, algorithmTimeoutS)
        .runExperiment(problemID, instances.toArray(new String[]{}), algorithms.toArray(new String[]{}));       
    }   
}

@Parameters( commandDescription = "Perform single evolution experiment")
class ExperimentSingleEvolutionCommand extends Command
{
    @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
    int numOfSubjects;
    @Parameter(names = "-g", required = true, description = "Number of iterations")
    int numOfIterations;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;
    @Parameter(names = "-p", required = true, description = "ProblemID")
    String problemID;
    @Parameter(names = "-i", required = true, description = "Problem instances", variableArity = true)
    List<String> instances;
    @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
    List<String> algorithms;
    @Override
    public void performCommad() throws Exception 
    {
        new SingleAlgorithmEvolutionExperimenter(numOfSubjects, numOfIterations, algorithmTimeoutS )
        .runExperiment(problemID, instances.toArray(new String[]{}), algorithms.toArray(new String[]{}));       
    }   
}
