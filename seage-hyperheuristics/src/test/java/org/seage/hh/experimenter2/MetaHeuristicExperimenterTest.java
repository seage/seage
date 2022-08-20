package org.seage.hh.experimenter2;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.seage.hh.knowledgebase.db.DbManager;

class MetaHeuristicExperimenterTest {
  
  @Test
  void testMetaHeuristicExperiment() throws Exception {
    //DbManager.initTest();

    HashMap<String, List<String>> instances = new HashMap<>();
    instances.put("TSP", Arrays.asList(new String[] {"berlin52"}));

    ExperimenterRunner ae = new ExperimenterRunner(
          "AntColony", instances, 3, 60);

    // Test the experiment runner
    assertNotNull(ae);

    // Try to run it
    ae.runExperiment();
  }

}
