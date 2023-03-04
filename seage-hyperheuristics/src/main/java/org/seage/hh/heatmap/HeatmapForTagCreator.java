package org.seage.hh.heatmap;

import org.seage.aal.score.ScoreCalculator;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// ---------------------------------------------------

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HeatmapForTagCreator {
  private HeatmapForTagCreator() {
    // Empty constructor
  }

  protected static List<ExperimentScoreCard> mergeScoreCards(
      List<ExperimentScoreCard> experiments) {
    // Map of algorithm names and their score cards
    HashMap<String, ExperimentScoreCard> scoreCardsTable = new HashMap<>();

    for (ExperimentScoreCard scoreCard : experiments) {
      if(!scoreCardsTable.containsKey(scoreCard.getAlgorithmName())){
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
          resultScoreCard.setAlgorithmScore(ScoreCalculator.calculateExperimentScore(
              new ArrayList<>(resultScoreCard.getProblemsScores())));
        }
      }            
    }
    return new ArrayList<>(scoreCardsTable.values());
  }

  public static String createHeatmapForTag(String tag) throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();

    List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);
    List<ExperimentScoreCard> scoreCardsForTag = new ArrayList<>();

    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

    for (ExperimentRecord experiment : experiments) {
      ExperimentScoreCard scoreCard = gson.fromJson(experiment.getScoreCard(), objType);

      if (scoreCard != null) {
        scoreCardsForTag.add(scoreCard);
      }
    }
    List<ExperimentScoreCard> table = mergeScoreCards(scoreCardsForTag);
    return HeatmapGenerator.createHeatmap(table, tag, new HashMap<>());
  }
}
