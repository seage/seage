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
import java.util.Map.Entry;
import org.seage.aal.Annotations.AlgorithmId;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.heatmap.HeatmapGenerator;
import org.seage.hh.heatmap.HeatmapGenerator.ExperimentScoreCards;
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

    HashMap<String, ExperimentScoreCard> algExperiment = new HashMap<>();

    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

    for (ExperimentRecord experiment : experiments) {
      if (heatmap) {
        ExperimentScoreCard expScoreCard = gson.fromJson(experiment.getScoreCard(), objType);

        if (algExperiment.containsKey(experiment.getAlgorithmID())) {
          double bestOverScore = 0.0;
          ExperimentScoreCard curExpScoreCard = algExperiment.get(experiment.getAlgorithmID());

          for (String algorithID : expScoreCard.getProblems()) {
            if (expScoreCard.getProblemScore(algorithID) > curExpScoreCard.getProblemScore(algorithID)) {
              curExpScoreCard.putProblemScore(algorithID, expScoreCard.getProblemScore(algorithID));
              
              bestOverScore += expScoreCard.getProblemScore(algorithID);
            } else {
              bestOverScore += curExpScoreCard.getProblemScore(algorithID);
            }
          }
          curExpScoreCard.setTotalScore(bestOverScore/(curExpScoreCard.getProblems().size()));
        } else {
          algExperiment.put(experiment.getAlgorithmID(), expScoreCard);
        }
      }

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

    if (heatmap) {
      ExperimentScoreCards scoreCards = new ExperimentScoreCards();

      for (Entry<String, ExperimentScoreCard> entry : algExperiment.entrySet()) {
        scoreCards.results.add(entry.getValue());
      }

      System.out.println(gson.toJson(scoreCards));
    }
  }
}
