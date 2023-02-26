package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.heatmap.HeatmapGenerator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


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

    List<ExperimentScoreCard> experimentsScoreCards = new ArrayList<>();

    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

    for (ExperimentRecord experiment : experiments) {
      experimentsScoreCards.add(gson.fromJson(experiment.getScoreCard(), objType));

      // Print the experiment details
      String logLine =
          String.format("%10d | %25s | %20s | %12s | %7.5f | %15s |", 
              experiment.getStartDate().toInstant().toEpochMilli(),
              experiment.getExperimentType(), 
              experiment.getAlgorithmID(),
              experiment.getProblemID(),
              experiment.getScore(),
              experiment.getTag());
      logger.info(logLine);
    }
  }
}
