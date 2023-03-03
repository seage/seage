package org.seage.hh.heatmap;

import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.knowledgebase.db.dbo.ExperimentRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

// ---------------------------------------------------

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HeatmapForTagCreator {
  private HeatmapForTagCreator() {
    // Empty constructor
  }

  protected static Collection<ExperimentScoreCard> getMergedScoreCards (List<ExperimentRecord> experiments) throws Exception {
    HashMap<String, ExperimentScoreCard> algExperiment = new HashMap<>();

    Gson gson = new Gson();
    Type objType = new TypeToken<ExperimentScoreCard>() {}.getType();

    for (ExperimentRecord experiment : experiments) {
      ExperimentScoreCard expScoreCard = gson.fromJson(experiment.getScoreCard(), objType);

      if (expScoreCard == null) {
        continue;
      }

      if (algExperiment.containsKey(experiment.getAlgorithmID())) {
        ExperimentScoreCard bestExpScoreCard = algExperiment.get(experiment.getAlgorithmID());

        for (String problemID : expScoreCard.getProblems()) {
          if ( (!bestExpScoreCard.getProblems().contains(problemID)) || 
              expScoreCard.getProblemScore(problemID) > bestExpScoreCard.getProblemScore(problemID)) {    
                bestExpScoreCard.putProblemScore(problemID, expScoreCard.getProblemScore(problemID));
          }
        }
      } else {
        algExperiment.put(experiment.getAlgorithmID(), expScoreCard);
      }
    }

    // Update the algorithm score
    for (ExperimentScoreCard expScoreCard : algExperiment.values()) {
      double algorithmScore = 0.0;
      for (String problemID : expScoreCard.getProblems()) {
        algorithmScore += expScoreCard.getProblemScore(problemID);
      }
      expScoreCard.setAlgorithmScore(algorithmScore / (expScoreCard.getProblems().size()));
    }

    return algExperiment.values();
  }

  public static void createHeatmapForTag(String tag) throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();
  
    List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);

    // Generate the svg heatmap file
    HeatmapGenerator.createSvgFile(tag, 
      HeatmapGenerator.loadExperimentScoreCards(new ArrayList<>(getMergedScoreCards(experiments)), 
      new HashMap<>()), "./output" + "/" + tag + "-heatmap.svg");
    }
}