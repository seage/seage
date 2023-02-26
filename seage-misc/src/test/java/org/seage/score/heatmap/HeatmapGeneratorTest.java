package org.seage.score.heatmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

public class HeatmapGeneratorTest {
  // Path to the xml with results
  //String xmlPath = "src/test/resources/test-unit-metric-scores.xml";
  String jsonPath = "/test-unit-metric-scores.json";
  Map<String, String> authorsNames = new HashMap<String, String>();

  @Test
  void testLoadJsonFile() throws Exception {
    HeatmapGenerator hmg = new HeatmapGenerator();
    List<HeatmapGenerator.AlgorithmResult> results;
    // Turn the json file into a input stream
    try (Scanner scanner = new Scanner( new File("bin/test/" + jsonPath), "UTF-8" )) {
      results = hmg.loadJson(scanner.useDelimiter("\\A").next(), authorsNames);
    }
    // Get problems
    List<String> problems = hmg.getProblemsNames(results);

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
    HeatmapGenerator hmg = new HeatmapGenerator();
    List<HeatmapGenerator.AlgorithmResult> results;
    
    try (Scanner scanner = new Scanner( new File("bin/test/" + jsonPath), "UTF-8" )) {
      results = hmg.loadJson(scanner.useDelimiter("\\A").next(), authorsNames);
    }

    // Sort the results
    hmg.sortResults(results);

    // Test the sorted order
    assertEquals("Algorithm2", results.get(0).name);
    assertEquals("Algorithm3", results.get(1).name);
    assertEquals("Algorithm1", results.get(2).name);
  }

  @Test
  void testJsonResultsToList() throws Exception {
    HeatmapGenerator hmg = new HeatmapGenerator();
    List<HeatmapGenerator.AlgorithmResult> results;

    // Turn the json file into a input stream
    try (Scanner scanner = new Scanner( new File("bin/test/" + jsonPath), "UTF-8" )) {
      results = hmg.loadJson(scanner.useDelimiter("\\A").next(), authorsNames);
    }

    List<String> problems = hmg.getProblemsNames(results);
    // Sort the results
    hmg.sortResults(results);
    // Create list from results
    List<List<String>> algsOverRes = new ArrayList<>();
    List<List<List<String>>> algsProbsRes = new ArrayList<>();
    hmg.resultsToList(results, problems, algsOverRes, algsProbsRes);

    // Test if the list isn't null
    assertNotNull(algsOverRes);
    // Test the length
    assertEquals(3, algsOverRes.size());
    // Test the overall data
    assertEquals("Algorithm2", algsOverRes.get(0).get(0));
    assertEquals("0.9", algsOverRes.get(0).get(2));
    assertEquals("Algorithm3", algsOverRes.get(1).get(0));
    assertEquals("0.6", algsOverRes.get(1).get(2));
    assertEquals("Algorithm1", algsOverRes.get(2).get(0));
    assertEquals("0.3", algsOverRes.get(2).get(2));

    // Test the problems data
    assertEquals(problems.get(0), algsProbsRes.get(0).get(0).get(0));
    assertEquals(problems.get(1), algsProbsRes.get(0).get(1).get(0));
    assertEquals(problems.get(2), algsProbsRes.get(0).get(2).get(0));
    assertEquals(problems.get(3), algsProbsRes.get(0).get(3).get(0));
  }

  @Test
  void testCreateHeatmap() throws Exception {
    try (Scanner scanner = new Scanner( new File("bin/test/" + jsonPath), "UTF-8" )) {
      // Get svg heatmap
      String heatmapSvg = HeatmapGenerator.createHeatmap(scanner.useDelimiter("\\A").next(), "test", authorsNames);

      // Test the result
      assertNotNull(heatmapSvg);

      // Store the svg file
      String tmpDir = System.getProperty("java.io.tmpdir");

      try (FileWriter fileWriter = new FileWriter(tmpDir + "/heatmap.svg");) {
        fileWriter.write(heatmapSvg);
      }
    }
  }
}
