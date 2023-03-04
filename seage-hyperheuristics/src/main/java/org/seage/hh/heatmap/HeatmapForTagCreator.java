package org.seage.hh.heatmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seage.aal.score.ScoreCalculator;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;

public class HeatmapForTagCreator {
  private HeatmapForTagCreator() {
    // Empty constructor
  }

  private static final String SEAGE = "SEAGE";
  private static final Map<String, String> algAuthors = Map.of(
      "GeneticAlgorithm", SEAGE,
      "TabuSearch", SEAGE, 
      "SimulatedAnnealing", SEAGE, 
      "AntColony", SEAGE
  );

  protected static List<ExperimentScoreCard> mergeScoreCards(List<ExperimentScoreCard> scoreCards) {
    // Map of algorithm names and their score cards
    HashMap<String, ExperimentScoreCard> scoreCardsTable = new HashMap<>();

    for (ExperimentScoreCard scoreCard : scoreCards) {
      if (!scoreCardsTable.containsKey(scoreCard.getAlgorithmName())) {
        scoreCardsTable.put(scoreCard.getAlgorithmName(), scoreCard);
        continue;
      }
      // Merge logic here
      ExperimentScoreCard resultScoreCard = scoreCardsTable.get(scoreCard.getAlgorithmName());

      for (String problemID : scoreCard.getProblems()) {
        Double score = scoreCard.getProblemScore(problemID);
        Double bestScore = resultScoreCard.getProblemScore(problemID);
        if (bestScore == null || score > bestScore) {
          resultScoreCard.putProblemScore(problemID, score);
          resultScoreCard.setAlgorithmScore(ScoreCalculator
              .calculateExperimentScore(new ArrayList<>(resultScoreCard.getProblemsScores())));
        }
      }
    }
    return new ArrayList<>(scoreCardsTable.values());
  }
  /**
   * It fetches experiment reports by tag and creates a heatmap from them.
   * @param tag Experiment tag
   */
  public static void createHeatmapForTag(String tag) throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();

    List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);
    List<ExperimentScoreCard> scoreCardsForTag = new ArrayList<>();


    for (ExperimentRecord experiment : experiments) {
      ExperimentScoreCard scoreCard = parseScoreCardsJson(experiment.getScoreCard());

      if (scoreCard != null) {
        scoreCardsForTag.add(scoreCard);
      }
    }
    List<ExperimentScoreCard> table = mergeScoreCards(scoreCardsForTag);
    String heatmap = HeatmapGenerator.createHeatmap(table, tag, algAuthors);
    
    String filePrexix = tag == null ? String.valueOf(System.currentTimeMillis()) : tag;    
    try (FileWriter fileWriter = new FileWriter("./output" + "/" + filePrexix + "-heatmap.svg")) {
      fileWriter.write(heatmap);
    }
  }

  protected static ExperimentScoreCard parseScoreCardsJson(String scoreCardJson) {
    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();
    return gson.fromJson(scoreCardJson, objType);
  }
}
