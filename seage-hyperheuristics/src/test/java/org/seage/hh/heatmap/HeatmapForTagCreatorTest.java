package org.seage.hh.heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.ExperimentScoreCard;

public class HeatmapForTagCreatorTest {
  // Suppose we denote algorithms by uppercase letters {A, B, C} and problems by numbers {1, 2, 3}.
  // A - GeneticAlgorithm
  // B - TabuSearch
  // C - SimulatedAnnealing
  // D - AntColony
  // 1 - SAT
  // 2 - TSP
  // 3 - FSP
  // 4 - JSP 
  String scB3_1 = """
      {
        "algorithmName": "TabuSearch",
        "scorePerProblem": {
          "FSP": 0.7
        },
        "totalScore": 0.7
      }
      """;
  String scB3_2 = """
      {
        "algorithmName": "TabuSearch",
        "scorePerProblem": {
          "FSP": 0.4
        },
        "totalScore": 0.4
      }
      """;
  String scB2_1 = """
      {
        "algorithmName": "TabuSearch",
        "scorePerProblem": {
          "TSP": 0.3
        },
        "totalScore": 0.3
      }
      """;

  String scB1_1 = """
      {
        "algorithmName": "TabuSearch",
        "scorePerProblem": {
          "SAT": 0.5
        },
        "totalScore": 0.5
      }
      """;
  String scA12 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "scorePerProblem": {
          "SAT": 0.4,
          "TSP": 0.6          
        },
        "totalScore": 0.5
      }
      """;
  String scA2 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "scorePerProblem": {
          "TSP": 0.4
        },
        "totalScore": 0.4
      }
      """;
  String scA1 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "scorePerProblem": {
          "SAT": 0.6
        },
        "totalScore": 0.6
      }
      """;

  @Test
  /**
   * Merging two experiments results of one algorithm and on the same problem domain
   */
  void testSC1() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(scB3_1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(scB3_2);
    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.7, table.get(0).getAlgorithmScore());
    assertEquals(1, table.get(0).getProblems().size());
  }

  @Test
  /**
   * Merging three experiments results of one algorithm on two problem domains
   */
  void testSC2() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(scB3_1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(scB3_2);
    ExperimentScoreCard scoreCard3 = HeatmapForTagCreator.parseScoreCardsJson(scB2_1);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2, scoreCard3);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.5, table.get(0).getAlgorithmScore());
    assertEquals(2, table.get(0).getProblems().size());
  }

  @Test
  /**
   * Merging four experiments results of one algorithm on three problem domains
   */
  void testSC3() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(scB3_1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(scB3_2);
    ExperimentScoreCard scoreCard3 = HeatmapForTagCreator.parseScoreCardsJson(scB2_1);
    ExperimentScoreCard scoreCard4 = HeatmapForTagCreator.parseScoreCardsJson(scB1_1);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2, scoreCard3, scoreCard4);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
    assertEquals("TabuSearch", table.get(0).getAlgorithmName());
    assertEquals(0.5, table.get(0).getAlgorithmScore());
    assertEquals(3, table.get(0).getProblems().size());
  }

  @Test
  /**
   * Merging two and two different experiments, two algorithms, where one has one problem domain 
   * and the other one has two problem domains
   */
  void testSC4() {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(scB3_1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(scB3_2);
    ExperimentScoreCard scoreCard3 = HeatmapForTagCreator.parseScoreCardsJson(scA1);
    ExperimentScoreCard scoreCard4 = HeatmapForTagCreator.parseScoreCardsJson(scA2);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2, scoreCard3, scoreCard4);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(2, table.size());

    switch (table.get(0).getAlgorithmName()) {
      case "TabuSearch":
        assertEquals("TabuSearch", table.get(0).getAlgorithmName());
        assertEquals(0.7, table.get(0).getAlgorithmScore());
        assertEquals(1, table.get(0).getProblems().size());

        assertEquals("GeneticAlgorithm", table.get(1).getAlgorithmName());
        assertEquals(0.5, table.get(1).getAlgorithmScore());
        assertEquals(2, table.get(1).getProblems().size());
        break;
      case "GeneticAlgorithm":
        assertEquals("GeneticAlgorithm", table.get(0).getAlgorithmName());
        assertEquals(0.5, table.get(0).getAlgorithmScore());
        assertEquals(2, table.get(0).getProblems().size());

        assertEquals("GeneticAlgorithm", table.get(1).getAlgorithmName());
        assertEquals(0.7, table.get(1).getAlgorithmScore());
        assertEquals(1, table.get(1).getProblems().size());
        break;
      default:
        assertTrue(false);
    }
  }

  @Test
  /**
   * Testing passing an empty scoreCard
   */
  void testSC5() {
    ExperimentScoreCard scoreCard1 = new ExperimentScoreCard("empty", new String[0]);

    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1);
    List<ExperimentScoreCard> table = HeatmapForTagCreator.mergeScoreCards(scoreCards);

    assertEquals(1, table.size());
  }
}
