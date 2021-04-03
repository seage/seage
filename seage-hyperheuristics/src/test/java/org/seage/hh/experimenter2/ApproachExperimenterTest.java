package org.seage.hh.experimenter2;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ApproachExperimenterTest {
  @Test
  void testApproachExperimenterInit() {
    HashMap<String, List<String>> instances = new HashMap<>();
    instances.put("TSP", Arrays.asList(new String[] { "eil51", "berlin52" }));
    instances.put("SAT", Arrays.asList(new String[] { "u100", "u1000" }));
    
    ApproachExperimenter ae = new ApproachExperimenter("approachName", instances);
    assertNotNull(ae);
  }
}
