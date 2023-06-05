package org.seage.launcher.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ProblemInstanceParamsParserTest {

  @Test
  void testParseOneProblemOneInstance() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ABC:inst34");

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals("ABC", result.keySet().stream().findFirst().get());
    assertEquals(1, result.keySet().size());
    assertEquals(1, result.get("ABC").size());
    assertEquals("inst34", result.get("ABC").get(0));
  }

  @Test
  void testParseOneProblemTwoInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ABC:inst12");
    instanceParams.add("ABC:inst34");

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals("ABC", result.keySet().stream().findFirst().get());
    assertEquals(1, result.keySet().size());
    assertEquals(2, result.get("ABC").size());
    assertEquals("inst12", result.get("ABC").get(0));
    assertEquals("inst34", result.get("ABC").get(1));
  }

  @Test
  void testParseTwoProblemsTwoInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ABC:inst12");
    instanceParams.add("CDE:inst34");

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals(2, result.keySet().size());
    assertTrue(result.containsKey("ABC"));
    assertTrue(result.containsKey("CDE"));    
    assertEquals(1, result.get("ABC").size());
    assertEquals("inst12", result.get("ABC").get(0));
    assertEquals(1, result.get("CDE").size());
    assertEquals("inst34", result.get("CDE").get(0));
  }

  @Test
  void testParseUnknownHyflexInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ABC:hyflex");

    assertThrows(IllegalArgumentException.class, 
        () -> ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams));
  }

  @Test
  void testParseTspHyflexInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("TSP:hyflex");    

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals(1, result.keySet().size());
    assertTrue(result.containsKey("TSP"));    
    assertEquals(5, result.get("TSP").size());
    assertEquals("pr299-hyflex-0", result.get("TSP").get(0));
  }

  @Test
  void testParseAllHyflexInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ALL:hyflex");    

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals(4, result.keySet().size());
    assertTrue(result.containsKey("SAT"));
    assertTrue(result.containsKey("TSP"));
    assertTrue(result.containsKey("JSP"));
    assertTrue(result.containsKey("FSP"));
    assertFalse(result.containsKey("QAP"));
    assertEquals(5, result.get("SAT").size());
    assertEquals("pg-525-2276-hyflex-3", result.get("SAT").get(0));
    assertEquals(5, result.get("TSP").size());
    assertEquals("pr299-hyflex-0", result.get("TSP").get(0));
    assertEquals(5, result.get("JSP").size());
    assertEquals("ft10", result.get("JSP").get(0));
    assertEquals(5, result.get("FSP").size());
    assertEquals("tai100_20_02", result.get("FSP").get(0));
    // assertEquals(5, result.get("QAP").size());
    // assertEquals("sko100a", result.get("QAP").get(0));
  }

  @Test
  void testParseAllSmallInstances() throws Exception {
    List<String> instanceParams = new ArrayList<>();
    instanceParams.add("ALL:small");    

    Map<String, List<String>> result = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceParams);

    assertNotNull(result);
    assertEquals(4, result.keySet().size());
    assertTrue(result.containsKey("SAT"));
    assertTrue(result.containsKey("TSP"));
    assertTrue(result.containsKey("JSP"));
    assertTrue(result.containsKey("FSP"));
    assertFalse(result.containsKey("QAP"));
    assertEquals(5, result.get("SAT").size());
    assertEquals(5, result.get("TSP").size());
    assertEquals(5, result.get("JSP").size());
    assertEquals(5, result.get("FSP").size());
    // assertEquals(5, result.get("QAP").size());
    // assertEquals("sko100a", result.get("QAP").get(0));
  }
}
