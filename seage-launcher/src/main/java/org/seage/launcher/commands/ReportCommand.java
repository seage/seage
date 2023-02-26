package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.seage.hh.score.heatmap.HeatmapGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
   * Method generates the experiment heatmap
   * @param experiment .
   */
  void generateHeatmap(ExperimentRecord experiment) throws IOException {
    logger.info(experiment.getScoreCard());
  }

  @Override
  public void performCommand() throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();
    
    List<ExperimentRecord> experiments = (tag != null) ? reporter.getExperimentsByTag(tag) : reporter.getExperiments();

    for (ExperimentRecord experiment : experiments) {
      String logLine =
          String.format("%10d | %25s | %20s | %12s | %7.5f | %15s |", 
              experiment.getStartDate().toInstant().toEpochMilli(),
              experiment.getExperimentType(), 
              experiment.getAlgorithmID(),
              experiment.getProblemID(),
              experiment.getScore(),
              experiment.getTag());
      logger.info(logLine);

      if (heatmap && tag != null) {
        for (String algorithm : algorithms) {
          // todo - find each best result with tag and combine them
        }
      }
    }
  }
}
