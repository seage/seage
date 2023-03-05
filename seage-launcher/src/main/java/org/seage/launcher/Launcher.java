package org.seage.launcher;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import org.seage.aal.problem.ProblemProvider;
import org.seage.launcher.commands.Command;
import org.seage.launcher.commands.DetailsCommand;
import org.seage.launcher.commands.ExperimentApproachCommand;
import org.seage.launcher.commands.ExperimentMultiRandomCommand;
import org.seage.launcher.commands.ExperimentSingleDefaultCommand;
import org.seage.launcher.commands.ExperimentSingleEvolutionCommand;
import org.seage.launcher.commands.ExperimentSingleFeedbackCommand;
import org.seage.launcher.commands.ExperimentSingleGridCommand;
import org.seage.launcher.commands.ExperimentSingleRandomCommand;
import org.seage.launcher.commands.InfoCommand;
import org.seage.launcher.commands.MetadataGeneratorCommand;
import org.seage.launcher.commands.ReportCommand;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.problem.qap.QapProblemProvider;
import org.seage.problem.sat.SatProblemProvider;
import org.seage.problem.tsp.TspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Parameters(commandDescription = "SEAGE Launcher")
public class Launcher {
  @Parameter(names = "--help", help = true)
  private boolean help;

  private static final Logger logger = LoggerFactory.getLogger(Launcher.class.getName());
  private static final HashMap<String, Command> commands = new LinkedHashMap<>();

  static {
    ProblemProvider.registerProblemProviders(
        new Class<?>[] {TspProblemProvider.class, SatProblemProvider.class,
            JspProblemProvider.class, FspProblemProvider.class, QapProblemProvider.class});

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
    commands.put("report", new ReportCommand());
  }

  /**
   * .
   */
  public static void main(String[] args) {
    try {
      Launcher launcher = new Launcher();

      JCommander jc = new JCommander(launcher);
      // jc.addCommand("", launcher); // Hm?
      for (Entry<String, Command> e : commands.entrySet()) {
        jc.addCommand(e.getKey(), e.getValue());
      }

      Command command = processArgs(args, jc, launcher);

      if (command != null) {
        launcher.run(command);
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void run(Command cmd) throws Exception {
    logger.info("");
    logger.info("SEAGE {} - https://www.seage.org", SeageVersion.VERSION);
    logger.info("");
    logger.info("SEAGE running ...");
    cmd.performCommand();
    logger.info("SEAGE finished");
  }

  private static Command processArgs(String[] args, JCommander jc, Launcher launcher) {
    Command command = null;
    try {
      if (args.length == 0) {
        printDefaultHelp(jc);
        return null;
      }
      jc.parse(args);
      if (launcher.help) {
        
        jc.usage();
        return null;
      }
      String commandName = jc.getParsedCommand();
      JCommander commander = jc.getCommands().get(commandName);
      command = (Command) commander.getObjects().get(0);

      if (command.help) {   
        String commandDescription = jc.getUsageFormatter().getCommandDescription(commandName);     
        jc.getConsole().println(String.format("%n%s\t%s%n", commandName, commandDescription));
        commander.usage();
        return null;
      }
    } catch (ParameterException ex) {
      logger.error(ex.getMessage());
      logger.error("Try to use --help");
      return null;
    }
    return command;
  }

  private static void printDefaultHelp(JCommander jc) {
    jc.getConsole().println("");
    jc.getConsole().println("SEAGE - https://www.seage.org");
    jc.getConsole().println("");
    jc.getConsole().println("Usage: ./scripts/run.sh [command]");
    jc.getConsole().println("");
    jc.getConsole().println("  Environment variables:");
    jc.getConsole().println(String.format("    %-30s", "DB_USER=seage"));
    jc.getConsole().println(String.format("    %-30s", "DB_PASSWORD=seage"));
    jc.getConsole().println(String.format("    %-30s", "DB_URL=jdbc:postgresql://localhost:5432/seage"));
    jc.getConsole().println("");
    jc.getConsole().println("  Commands:");
    for (Entry<String, Command> e : commands.entrySet()) {
      String commandDescription = jc.getUsageFormatter().getCommandDescription(e.getKey());
      jc.getConsole().println(String.format("    %-30s - %s", e.getKey(), commandDescription));
    }
    jc.getConsole().println("");
    jc.getConsole().println("Use --help with each command");
    jc.getConsole().println("");
  }
}
