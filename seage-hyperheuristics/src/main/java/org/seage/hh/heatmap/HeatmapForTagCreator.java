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

  protected static List<ExperimentScoreCard> getMergedScoreCards (List<ExperimentScoreCard> experiments) {
    HashMap<String, ExperimentScoreCard> algExperiment = new HashMap<>();

    for (ExperimentScoreCard expScoreCard : experiments) {
      if (algExperiment.containsKey(expScoreCard.getName())) {
        ExperimentScoreCard bestExpScoreCard = algExperiment.get(expScoreCard.getName());

        for (String problemID : expScoreCard.getProblems()) {
          if ( (!bestExpScoreCard.getProblems().contains(problemID)) || 
              expScoreCard.getProblemScore(problemID) > bestExpScoreCard.getProblemScore(problemID)) {    
                bestExpScoreCard.putProblemScore(problemID, expScoreCard.getProblemScore(problemID));
          }
        }
        bestExpScoreCard.setAlgorithmScore(ScoreCalculator.calculateExperimentScore(new ArrayList<>(bestExpScoreCard.getProblemsScores())));
      } else {
        algExperiment.put(expScoreCard.getName(), expScoreCard);
      }
    }

    return new ArrayList<>(algExperiment.values());
  }

  public static String createHeatmapForTag(String tag) throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();
  
    List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);
    List<ExperimentScoreCard> expScoreCards = new ArrayList<>();

    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

    for (ExperimentRecord experiment : experiments) {
      ExperimentScoreCard expScoreCard = gson.fromJson(experiment.getScoreCard(), objType);

      if (expScoreCard != null) {
        expScoreCards.add(expScoreCard);
      }
    }

    return HeatmapGenerator.createHeatmap(getMergedScoreCards(expScoreCards), tag, new HashMap<>());
  }
}