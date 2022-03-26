package org.seage.launcher;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import org.seage.aal.problem.ProblemProvider;
import org.seage.launcher.commands.Command;
import org.seage.launcher.commands.ExperimentApproachCommand;
import org.seage.launcher.commands.ExperimentMultiRandomCommand;
import org.seage.launcher.commands.ExperimentSingleDefaultCommand;
import org.seage.launcher.commands.ExperimentSingleEvolutionCommand;
import org.seage.launcher.commands.ExperimentSingleFeedbackCommand;
import org.seage.launcher.commands.ExperimentSingleGridCommand;
import org.seage.launcher.commands.ExperimentSingleRandomCommand;
import org.seage.launcher.commands.InfoCommand;
import org.seage.launcher.commands.DetailsCommand;
import org.seage.launcher.commands.MetadataGeneratorCommand;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.problem.sat.SatProblemProvider;
import org.seage.problem.tsp.TspProblemProvider;
import org.seage.problem.qap.QapProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
  static {
    ProblemProvider.registerProblemProviders(
        new Class<?>[] {
            TspProblemProvider.class,
            SatProblemProvider.class,
            JspProblemProvider.class,
            FspProblemProvider.class,
            QapProblemProvider.class
        });
  }
  
  private static final Logger _logger = LoggerFactory.getLogger(Launcher.class.getName());

  @Parameter(names = "--help", help = true)
  private boolean help;

  public static void main(String[] args) {
    try {
      HashMap<String, Command> commands = new LinkedHashMap<>();
      commands.put("info", new InfoCommand());
      commands.put("details", new DetailsCommand());
      commands.put("experiment-single-default", new ExperimentSingleDefaultCommand());
      commands.put("experiment-single-random", new ExperimentSingleRandomCommand());
      commands.put("experiment-single-interval", new ExperimentSingleGridCommand());
      commands.put("experiment-single-feedback", new ExperimentSingleFeedbackCommand());
      commands.put("experiment-single-evolution", new ExperimentSingleEvolutionCommand());
      commands.put("experiment-multi-random", new ExperimentMultiRandomCommand());
      commands.put("experiment-approach", new ExperimentApproachCommand());
      commands.put("metadata", new MetadataGeneratorCommand());

      Launcher launcher = new Launcher();

      JCommander jc = new JCommander(launcher);
      for (Entry<String, Command> e : commands.entrySet())
        jc.addCommand(e.getKey(), e.getValue());

      if(args.length == 0) {
        printDefaultHelp(commands);
        return;
      }
      String commandName = getParsedCommand(args, jc, launcher);
      if(commandName == null) {
        if (launcher.help) {
          jc.usage();
        }
        return;
      }      
      launcher.run(commands.get(commandName));
    } catch (Exception ex) {
      _logger.error(ex.getMessage(), ex);
    }
  }

  

  private void run(Command cmd) throws Exception {
    _logger.info("SEAGE running ...");
    // LogHelper.configure(Launcher.class.getClassLoader().getResourceAsStream("logback.xml"));
    cmd.performCommand();
    _logger.info("SEAGE finished ...");
  }

  private static void printDefaultHelp(HashMap<String, Command> commands) {
    System.out.println("Usage: <main class> [command]");
    System.out.println();
    System.out.println("  Commands:");
    for (Entry<String, Command> e : commands.entrySet()) {
      System.out.println(String.format("    %-30s - %s", 
          e.getKey(), 
          e.getValue().getClass().getAnnotationsByType(com.beust.jcommander.Parameters.class)[0].commandDescription()
      ));
    }
    System.out.println();
    System.out.println("Use --help with each command");
    System.out.println();
  }

  private static String getParsedCommand(String[] args, JCommander jc, Launcher launcher) {
    String commandName = null;
    try {
      jc.parse(args);
      commandName = jc.getParsedCommand();
    } catch (ParameterException ex) {
      commandName = args[0];
      JCommander command = jc.getCommands().get(commandName);
      if (command != null && args.length > 1 && args[1].equals("--help")) {            
        command.usage();
        return null;      
      }
      _logger.error(ex.getMessage());
      _logger.error("Try to use --help"); 
      return null; 
    }
    return commandName;
  }
}
