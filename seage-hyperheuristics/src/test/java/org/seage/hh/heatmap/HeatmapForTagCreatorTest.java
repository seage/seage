package org.seage.hh.heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.ExperimentScoreCard;

public class HeatmapForTagCreatorTest {
  String sc1 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.7,
        "scorePerInstance": {
          "FSP": {
            "tai500_20_01": 0.7
          }
        },
        "scorePerProblem": {
          "FSP": 0.7
        }
      }
      """;
  String sc2 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.4,
        "scorePerInstance": {
          "FSP": {
            "tai500_20_01": 0.4
          }
        },
        "scorePerProblem": {
          "FSP": 0.4
        }
      }
      """;
  String sc3 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.3,
        "scorePerInstance": {
          "TSP": {
            "berlin52": 0.3

          }
        },
        "scorePerProblem": {
          "TSP": 0.3
        }
      }
      """;

    String sc4 = """
      {
        "algorithmName": "TabuSearch",
        "totalScore": 0.4,
        "scorePerInstance": {
          "SAT": {
            "uf100-01": 0.4
          }
        },
        "scorePerProblem": {
          "SAT": 0.5
        }
      }
      """;
    String sc5 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "totalScore": 0.5,
        "scorePerInstance": {
          "TSP": {
            "berlin52": 0.3,
            "brd14051": 0.5
          },
          "SAT": {
            "uf100-01": 0.6,
            "uf100-011": 0.2
          },
        },
        "scorePerProblem": {
          "TSP": 0.6,
          "SAT": 0.4
        }
      }
      """;
      String sc6 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "totalScore": 0.4,
        "scorePerInstance": {
          "TSP": {
            "berlin52": 0.6,
            "brd14051": 0.2
          },
        },
        "scorePerProblem": {
          "TSP": 0.4,
        }
      }
      """;
      String sc7 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "totalScore": 0.6,
        "scorePerInstance": {
          "SAT": {
            "uf100-01": 0.7,
            "uf100-011": 0.5
          },
        },
        "scorePerProblem": {
          "SAT": 0.6
        }
      }
      """;

  @Test
  void testSC1() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(sc1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(sc2);
    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.7, table.get(0).getAlgorithmScore());
    assertEquals(1, table.get(0).getProblems().size());
  }

  @Test
  void testSC2() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(sc1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(sc2);
    ExperimentScoreCard scoreCard3 = HeatmapForTagCreator.parseScoreCardsJson(sc3);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2, scoreCard3);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.5, table.get(0).getAlgorithmScore());
    assertEquals(2, table.get(0).getProblems().size());
  }

  @Test
  void testSC3() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(sc1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(sc2);
    ExperimentScoreCard scoreCard3 = HeatmapForTagCreator.parseScoreCardsJson(sc3);
    ExperimentScoreCard scoreCard4 = HeatmapForTagCreator.parseScoreCardsJson(sc4);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2, scoreCard3, scoreCard4);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.5, table.get(0).getAlgorithmScore());
    assertEquals(3, table.get(0).getProblems().size());
  }
}
