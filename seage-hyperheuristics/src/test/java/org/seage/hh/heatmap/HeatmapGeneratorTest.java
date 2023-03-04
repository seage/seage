package org.seage.hh.heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.seage.hh.experimenter.ExperimentScoreCard;
import org.seage.hh.heatmap.HeatmapGenerator.AlgorithmResult;

public class HeatmapGeneratorTest {
  // Path to the xml with results
  //String xmlPath = "src/test/resources/test-unit-metric-scores.xml";
  String jsonPath = "/test-unit-metric-scores.json";

  String scB23_1 = """
      {
        "algorithmName": "TabuSearch",
        "scorePerProblem": {
          "FSP": 0.7,
          "TSP": 0.3
        },
        "totalScore": 0.5
      }
      """;
  String scA1_1 = """
      {
        "algorithmName": "GeneticAlgorithm",
        "scorePerProblem": {
          "SAT": 0.4
        },
        "totalScore": 0.4
      }
      """;

  Map<String, String> authorsNames = new HashMap<String, String>();

  @Test
  void testLoadJsonFile() throws Exception {
    List<ExperimentScoreCard> scoreCards;
    
    try (InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath)) {
      scoreCards = HeatmapGenerator.loadJson(new String(jsonInputStream.readAllBytes(), StandardCharsets.UTF_8));
    }

    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(scoreCards, authorsNames);

    // Get problems
    List<String> problems = HeatmapGenerator.getProblemsNames(results);

    // Test if the results aren't null
    assertNotNull(results);
    // test the algorithms num
    assertEquals(3, results.size());
    // test if the problems isn't null
    assertNotNull(problems);
    // test the problems num
    assertEquals(4, problems.size());

    // Test each of the results
    assertEquals(4, results.get(0).problemsResults.size());
    assertEquals(4, results.get(1).problemsResults.size());
    assertEquals(4, results.get(2).problemsResults.size());
  }

  @Test 
  void testJsonSortResults() throws Exception {
    List<ExperimentScoreCard> scoreCards;
    
    try (InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath)) {
      scoreCards = HeatmapGenerator.loadJson(new String(jsonInputStream.readAllBytes(), StandardCharsets.UTF_8));
    }

    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(scoreCards, authorsNames);

    // Sort the results
    HeatmapGenerator.sortResults(results);

    // Test the sorted order
    assertEquals("Algorithm1", results.get(0).name);
    assertEquals("Algorithm2", results.get(1).name);
    assertEquals("Algorithm3", results.get(2).name);
  }

  @Test
  void testJsonResultsToList() throws Exception {
    List<ExperimentScoreCard> scoreCards;
    
    try (InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath)) {
      scoreCards = HeatmapGenerator.loadJson(new String(jsonInputStream.readAllBytes(), StandardCharsets.UTF_8));
    }

    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(scoreCards, authorsNames);


    List<String> problems = HeatmapGenerator.getProblemsNames(results);
    // Sort the results
    HeatmapGenerator.sortResults(results);
    // Create list from results
    List<List<String>> algsOverRes = new ArrayList<>();
    List<List<List<String>>> algsProbsRes = new ArrayList<>();
    HeatmapGenerator.resultsToList(results, problems, algsOverRes, algsProbsRes);

    // Test if the list isn't null
    assertNotNull(algsOverRes);
    // Test the length
    assertEquals(3, algsOverRes.size());
    // Test the overall data
    assertEquals("Algorithm1", algsOverRes.get(0).get(0));
    assertEquals("0.9", algsOverRes.get(0).get(2));
    assertEquals("Algorithm2", algsOverRes.get(1).get(0));
    assertEquals("0.6", algsOverRes.get(1).get(2));
    assertEquals("Algorithm3", algsOverRes.get(2).get(0));
    assertEquals("0.3", algsOverRes.get(2).get(2));

    // Test the problems data
    assertEquals(problems.get(0), algsProbsRes.get(0).get(0).get(0));
    assertEquals(problems.get(1), algsProbsRes.get(0).get(1).get(0));
    assertEquals(problems.get(2), algsProbsRes.get(0).get(2).get(0));
    assertEquals(problems.get(3), algsProbsRes.get(0).get(3).get(0));
  }

  @Test
  void testCreateHeatmap() throws Exception {
    try (InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath)) {
      // Get svg heatmap
      String heatmapSvg = HeatmapGenerator.createHeatmap(
          HeatmapGenerator.loadJson(new String(jsonInputStream.readAllBytes(), StandardCharsets.UTF_8)), "test", authorsNames);

      // Test the result
      assertNotNull(heatmapSvg);

      // Store the svg file
      String tmpDir = System.getProperty("java.io.tmpdir");

      try (FileWriter fileWriter = new FileWriter(tmpDir + "/heatmap.svg");) {
        fileWriter.write(heatmapSvg);
      }
    }
  }

  @Test
  void testResultsToList() throws Exception {
    ExperimentScoreCard scoreCard1 = HeatmapForTagCreator.parseScoreCardsJson(scB23_1);
    ExperimentScoreCard scoreCard2 = HeatmapForTagCreator.parseScoreCardsJson(scA1_1);
    List<ExperimentScoreCard> scoreCards = List.of(scoreCard1, scoreCard2);
    
    List<AlgorithmResult> results = HeatmapGenerator.loadExperimentScoreCards(scoreCards, new HashMap<>());

    List<String> problems = HeatmapGenerator.getProblemsNames(results);

    assertTrue(problems.contains("TSP"));
    assertTrue(problems.contains("FSP"));
    assertTrue(problems.contains("SAT"));

    List<List<String>> algsOverRes = new ArrayList<>();
    List<List<List<String>>> algsProbsRes = new ArrayList<>();
    HeatmapGenerator.resultsToList(results, problems, algsOverRes, algsProbsRes);

    System.out.println("");
  }
}
