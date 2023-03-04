package org.seage.hh.heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.ExperimentScoreCard;

public class HeatmapForTagCreatorTest {
  // todo
  String sc1 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.7323786081897516,
        "scorePerInstance": {
          "FSP": {
            "tai500_20_01": 0.7323786081897516
          }
        },
        "scorePerProblem": {
          "FSP": 0.7323786081897516
        }
      }
      """;
  String sc2 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.7323786081897516,
        "scorePerInstance": {
          "FSP": {
            "tai500_20_01": 0.7323786081897516
          }
        },
        "scorePerProblem": {
          "FSP": 0.7323786081897516
        }
      }
      """;

  @Test
  void testSC1() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(sc1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(sc2);
    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    // TODO
    assertEquals(-1, table.size());
  }
}
