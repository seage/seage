package org.seage.hh.experimenter2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

public class ExperimentScoreCardTest {
  @Test
  void testScoreCardToJson() {
    ExperimentScoreCard scoreCard = new ExperimentScoreCard(
        "HyperHeuristic1", new String[] {"TSP", "SAT"});
    
    scoreCard.setTotalScore(0.5);
    scoreCard.putProblemScore("SAT", 0.6);
    scoreCard.putProblemScore("TSP", 0.4);
    scoreCard.putInstanceScore("SAT", "sat1", 0.1);
    scoreCard.putInstanceScore("SAT", "sat2", 0.2);
    scoreCard.putInstanceScore("SAT", "sat3", 0.3);
    scoreCard.putInstanceScore("TSP", "sat1", 0.1);
    scoreCard.putInstanceScore("TSP", "sat2", 0.2);
    scoreCard.putInstanceScore("TSP", "sat3", 0.3);

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String json = gson.toJson(scoreCard);
    assertNotNull(json);

    ExperimentScoreCard scoreCard2 = gson.fromJson(json, ExperimentScoreCard.class);
    assertNotNull(scoreCard2);
    String json2 = gson.toJson(scoreCard2);

    assertEquals(json, json2);
  }
}
