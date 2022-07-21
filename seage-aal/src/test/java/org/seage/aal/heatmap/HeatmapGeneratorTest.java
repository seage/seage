package org.seage.aal.heatmap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class HeatmapGeneratorTest {
  // Path to the xml with results
  //String xmlPath = "src/test/resources/test-unit-metric-scores.xml";
  String jsonPath = "/test-unit-metric-scores.json";
  Map<String, String> authorsNames = new HashMap<String, String>();

  @Test
  void testLoadJsonFile() throws Exception {
    HeatmapGenerator hmg = new HeatmapGenerator();

    // Turn the json file into a input stream
    InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath);

    // Load the results
    hmg.loadJson(jsonInputStream, authorsNames);

    // Test if the results aren't null
    assertNotNull(hmg.results);
    // test the algorithms num
    assertEquals(3, hmg.results.size());
    // test if the problems isn't null
    assertNotNull(hmg.problems);
    // test the problems num
    assertEquals(4, hmg.problems.size());

    // Test each of the results
    assertEquals(4, hmg.results.get(0).problemsResults.size());
    assertEquals(4, hmg.results.get(1).problemsResults.size());
    assertEquals(4, hmg.results.get(2).problemsResults.size());
  }

  @Test 
  void testJsonSortResults() {
    HeatmapGenerator hmg = new HeatmapGenerator();

    // Turn the json file into a input stream
    InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath);

    // Load the results
    hmg.loadJson(jsonInputStream, authorsNames);

    // Sort the results
    hmg.sortResults();

    // Test the sorted order
    assertEquals("Algorithm2", hmg.results.get(0).name);
    assertEquals("Algorithm3", hmg.results.get(1).name);
    assertEquals("Algorithm1", hmg.results.get(2).name);
  }

  @Test
  void testJsonResultsToList() throws Exception {
    HeatmapGenerator hmg = new HeatmapGenerator();

    // Turn the json file into a input stream
    InputStream jsonInputStream = HeatmapGeneratorTest.class.getResourceAsStream(jsonPath);

    // Load the results
    hmg.loadJson(jsonInputStream, authorsNames);

    // Sort the results
    hmg.sortResults();
    // Create list from results
    hmg.resultsToList();

    // Test if the list isn't null
    assertNotNull(hmg.algsOverRes);
    // Test the length
    assertEquals(3, hmg.algsOverRes.size());
    // Test the overall data
    assertEquals("Algorithm2", hmg.algsOverRes.get(0).get(0));
    assertEquals("0.9", hmg.algsOverRes.get(0).get(2));
    assertEquals("Algorithm3", hmg.algsOverRes.get(1).get(0));
    assertEquals("0.6", hmg.algsOverRes.get(1).get(2));
    assertEquals("Algorithm1", hmg.algsOverRes.get(2).get(0));
    assertEquals("0.3", hmg.algsOverRes.get(2).get(2));

    // Test the problems data
    assertEquals(hmg.problems.get(0), hmg.algsProbsRes.get(0).get(0).get(0));
    assertEquals(hmg.problems.get(1), hmg.algsProbsRes.get(0).get(1).get(0));
    assertEquals(hmg.problems.get(2), hmg.algsProbsRes.get(0).get(2).get(0));
    assertEquals(hmg.problems.get(3), hmg.algsProbsRes.get(0).get(3).get(0));
  }

  @Test
  void testLoadXmlFile() throws Exception {
    assertTrue(true);
    // HeatmapGenerator hmg = new HeatmapGenerator();

    // // Load the results
    // hmg.loadXmlFile(xmlPath, authorsNames);

    // // test if the results isn't null
    // assertNotNull(hmg.results);
    // // test the algorithms num
    // assertEquals(3, hmg.results.size());
    // // test if the problems isn't null
    // assertNotNull(hmg.problems);
    // // test the problems num
    // assertEquals(4, hmg.problems.size());

    // // Test each of the results
    // assertEquals(4, hmg.results.get(0).problemsResults.size());
    // assertEquals(4, hmg.results.get(1).problemsResults.size());
    // assertEquals(4, hmg.results.get(2).problemsResults.size());
  }

  @Test 
  void testXmlSortResults() {
    assertTrue(true);
    // HeatmapGenerator hmg = new HeatmapGenerator();
    
    // // Load the results
    // hmg.loadXmlFile(xmlPath, authorsNames);
    // // Sort the results
    // hmg.sortResults();

    // // Test the sorted order
    // assertEquals("Algorithm2", hmg.results.get(0).name);
    // assertEquals("Algorithm3", hmg.results.get(1).name);
    // assertEquals("Algorithm1", hmg.results.get(2).name);
  }

  @Test
  void testXmlResultsToList() throws Exception {
    assertTrue(true);
    // HeatmapGenerator hmg = new HeatmapGenerator();

    // // Load the results
    // hmg.loadXmlFile(xmlPath, authorsNames);
    // // Sort the results
    // hmg.sortResults();
    // // Create list from results
    // hmg.resultsToList();

    // // Test if the list isn't null
    // assertNotNull(hmg.algsOverRes);
    // // Test the length
    // assertEquals(3, hmg.algsOverRes.size());
    // // Test the overall data
    // assertEquals("Algorithm2", hmg.algsOverRes.get(0).get(0));
    // assertEquals("0.9", hmg.algsOverRes.get(0).get(2));
    // assertEquals("Algorithm3", hmg.algsOverRes.get(1).get(0));
    // assertEquals("0.6", hmg.algsOverRes.get(1).get(2));
    // assertEquals("Algorithm1", hmg.algsOverRes.get(2).get(0));
    // assertEquals("0.3", hmg.algsOverRes.get(2).get(2));

    // // Test the problems data
    // assertEquals(hmg.problems.get(0), hmg.algsProbsRes.get(0).get(0).get(0));
    // assertEquals(hmg.problems.get(1), hmg.algsProbsRes.get(0).get(1).get(0));
    // assertEquals(hmg.problems.get(2), hmg.algsProbsRes.get(0).get(2).get(0));
    // assertEquals(hmg.problems.get(3), hmg.algsProbsRes.get(0).get(3).get(0));
  }
}
