package org.seage.launcher;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.seage.launcher.commands.Command;
import org.seage.launcher.commands.ExperimentMultiRandomCommand;
import org.seage.launcher.commands.ExperimentSingleEvolutionCommand;
import org.seage.launcher.commands.ExperimentSingleFeedbackCommand;
import org.seage.launcher.commands.ExperimentSingleRandomCommand;
import org.seage.launcher.commands.ListCommand;
import org.seage.launcher.commands.ReportCommand;
import org.seage.logging.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


public class Launcher 
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
			commands.put("experiment-multi-random", new ExperimentMultiRandomCommand());
			
			Launcher launcher = new Launcher();
			
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
	    _logger.info("SEAGE running ...");
	    LogHelper.configure(Launcher.class.getClassLoader().getResourceAsStream("logback.xml"));
		cmd.performCommad();
		_logger.info("SEAGE finished ...");
	}
}



