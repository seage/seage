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
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeatmapForTagCreator {
  private static final Logger logger = LoggerFactory.getLogger(HeatmapForTagCreator.class);

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

  protected static List<ScoreCard> mergeScoreCards(List<ScoreCard> scoreCards) {
    // Map of algorithm names and their score cards
    HashMap<String, ScoreCard> scoreCardsTable = new HashMap<>();

    for (ScoreCard scoreCard : scoreCards) {
      if (!scoreCardsTable.containsKey(scoreCard.getAlgorithmName())) {
        scoreCardsTable.put(scoreCard.getAlgorithmName(), scoreCard);
        continue;
      }
      // Merge logic here
      ScoreCard resultScoreCard = scoreCardsTable.get(scoreCard.getAlgorithmName());

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
   * It fetches experiment reports by experimentTag and creates a heatmap from them.
   * @param experimentTag Experiment experimentTag
   */
  public static void createHeatmapForTag(
      List<ExperimentRecord> experiments, String experimentTag) throws Exception {

    List<ScoreCard> scoreCardsForTag = new ArrayList<>();

    for (ExperimentRecord experiment : experiments) {
      ScoreCard scoreCard = parseScoreCardsJson(experiment.getScoreCard());

      if (scoreCard != null) {
        scoreCardsForTag.add(scoreCard);
      }
    }
    List<ScoreCard> table = mergeScoreCards(scoreCardsForTag);
    String heatmap = HeatmapGenerator.createHeatmap(table, experimentTag, algAuthors);
    
    String filePrexix = experimentTag == null 
        ? String.valueOf(System.currentTimeMillis()) 
        : experimentTag;
    String filePath = "./output" + "/" + filePrexix + "-heatmap.svg";
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      logger.info("Storing heatmap svg into file: {}", filePath);
      fileWriter.write(heatmap);
    }
  }

  protected static ScoreCard parseScoreCardsJson(String scoreCardJson) {
    Gson gson = new Gson();
    Type objType = new TypeToken<ScoreCard>() {}.getType();
    return gson.fromJson(scoreCardJson, objType);
  }
}
