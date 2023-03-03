package org.seage.hh.heatmap;

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

  public static void createHeatmapForTag(String tag) throws Exception {
    ExperimentReporter reporter = new ExperimentReporter();
  
    List<ExperimentRecord> experiments = reporter.getExperimentsByTag(tag);

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
        double bestAlgScore = bestExpScoreCard.getAlgorithmScore();

        for (String problemID : expScoreCard.getProblems()) {
          System.out.println(problemID);
          if ( (!bestExpScoreCard.getProblems().contains(problemID)) || 
              expScoreCard.getProblemScore(problemID) > bestExpScoreCard.getProblemScore(problemID)) {
            System.out.println("Hey, I'm here");
            // there is the problem, it's not storing the problem id
            bestExpScoreCard.putProblemScore(problemID, expScoreCard.getProblemScore(problemID));              
          }
          bestAlgScore += bestExpScoreCard.getProblemScore(problemID);
          System.out.println(bestAlgScore);
        }
        bestExpScoreCard.setAlgorithmScore(bestAlgScore/(bestExpScoreCard.getProblems().size()));
        System.out.println(bestExpScoreCard.getProblems().size());
      } else {
        algExperiment.put(experiment.getAlgorithmID(), expScoreCard);
      }
    }

    // Generate the svg heatmap file
    HeatmapGenerator.createSvgFile(tag, 
      HeatmapGenerator.loadExperimentScoreCards(new ArrayList<>(algExperiment.values()), 
      new HashMap<>()), "./output" + "/" + tag + "-heatmap.svg");
    }
}