package org.seage.launcher.commands;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.heatmap.HeatmapForTagCreator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Perform a basic reporting")
public class ReportCommand extends Command {
  private static final Logger logger = LoggerFactory.getLogger(ReportCommand.class.getName());

  @Parameter(names = "-a", required = false, description = "Algorithms", variableArity = true)
  List<String> algorithms;

  @Parameter(names = {"-T", "--tag"}, required = false, description = "Tag the experiment is marked with")
  String tag;


  @Parameter(names = {"-H", "--heatmap"}, required = false, description = "Generate the experiment heatmap")
  boolean heatmap;

  /**
   * Method generates the experiment heatmap.
   * @param experiment .
   */
  void generateHeatmap(ExperimentRecord experiment) throws IOException {
    logger.info(experiment.getScoreCard());
  }

  @Override
  public void performCommand() throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();
    
    List<ExperimentRecord> experiments = (tag != null) 
        ? reporter.getExperimentsByTag(tag)
        : reporter.getExperiments();

    if (experiments.isEmpty()) {
      logger.info("No experiments for tag: {}", tag);
      return;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
    for (ExperimentRecord experiment : experiments) {
      // Print the experiment details
      String logLine =
          String.format("%s | %-25s | %-20s | %-20s | %7.5f | %-35s |", 
              dateFormat.format(experiment.getStartDate()),
              experiment.getExperimentType(), 
              experiment.getAlgorithmID(),
              experiment.getProblemID(),
              experiment.getScore(),
              experiment.getTag());
      logger.info(logLine);
    }
    // Create the heatmap
    if (heatmap) {
      HeatmapForTagCreator.createHeatmapForTag(experiments, tag);    
    }
  }
}
