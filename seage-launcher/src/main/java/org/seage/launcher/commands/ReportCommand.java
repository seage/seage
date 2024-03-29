package org.seage.launcher.commands;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.seage.hh.experiment.ExperimentReporter;
import org.seage.hh.heatmap.HeatmapForTagCreator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Perform a basic reporting")
public class ReportCommand extends Command {
  private static final Logger log = LoggerFactory.getLogger(ReportCommand.class.getName());

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
    log.info(experiment.getScoreCard());
  }

  @Override
  public void performCommand() throws Exception {
    
    List<ExperimentRecord> experiments = (tag != null) 
        ? ExperimentReporter.getExperimentsByTag(tag)
        : ExperimentReporter.getExperiments();

    if (experiments.isEmpty()) {
      log.info("No experiments for tag: {}", tag);
      return;
    }

    // Create the heatmap
    if (heatmap) {
      HeatmapForTagCreator.createHeatmapForTag(experiments, tag);    
    }

    // Print last 50 experiments
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    experiments = experiments.size() > 50 ? experiments.subList(0, 50) : experiments;
    Collections.sort(experiments, (e1, e2) -> (int)(e1.getStartDate().getTime() - e2.getStartDate().getTime()));
    
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
      log.info(logLine);
    }    
  }
}
