package org.seage.classutil;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class ClassPathAnalyzerTest {

  @Test
  void test() {
    ClassPathAnalyzer cpa = new ClassPathAnalyzer("seage.problem");

    List<String> list = cpa.analyzeClassPath();
    assertNotNull(list);
    // TODO: A - Figure out how to test this 
    // assertTrue(list.size() > 0);
  }

}
