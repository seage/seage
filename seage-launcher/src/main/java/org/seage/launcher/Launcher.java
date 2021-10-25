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
import org.seage.launcher.commands.ListCommand;
// import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jssp.JsspProblemProvider;
import org.seage.problem.sat.SatProblemProvider;
import org.seage.problem.tsp.TspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
  static {
    ProblemProvider.registerProblemProviders(
        new Class<?>[] {
            TspProblemProvider.class,
            SatProblemProvider.class,
            JsspProblemProvider.class
        });
  }
  
  private static final Logger _logger = LoggerFactory.getLogger(Launcher.class.getName());

  @Parameter(names = "--help", help = true)
  private boolean help;

  public static void main(String[] args) {
    try {
      HashMap<String, Command> commands = new LinkedHashMap<>();
      commands.put("list", new ListCommand());
      commands.put("experiment-single-default", new ExperimentSingleDefaultCommand());
      commands.put("experiment-single-random", new ExperimentSingleRandomCommand());
      commands.put("experiment-single-interval", new ExperimentSingleGridCommand());
      commands.put("experiment-single-feedback", new ExperimentSingleFeedbackCommand());
      commands.put("experiment-single-evolution", new ExperimentSingleEvolutionCommand());
      commands.put("experiment-multi-random", new ExperimentMultiRandomCommand());
      commands.put("experiment-approach", new ExperimentApproachCommand());

      Launcher launcher = new Launcher();

      JCommander jc = new JCommander(launcher);
      for (Entry<String, Command> e : commands.entrySet())
        jc.addCommand(e.getKey(), e.getValue());

      jc.parse(args);

      if (args.length == 0 || launcher.help) {
        jc.usage();
        return;
      }
      launcher.run(commands.get(jc.getParsedCommand()));
    } catch (ParameterException ex) {
      _logger.error(ex.getMessage());
      _logger.error("Try to use --help");
    } catch (Exception ex) {
      _logger.error(ex.getMessage(), ex);
    }
  }

  private void run(Command cmd) throws Exception {
    _logger.info("SEAGE running ...");
    // LogHelper.configure(Launcher.class.getClassLoader().getResourceAsStream("logback.xml"));
    cmd.performCommad();
    _logger.info("SEAGE finished ...");
  }
}
