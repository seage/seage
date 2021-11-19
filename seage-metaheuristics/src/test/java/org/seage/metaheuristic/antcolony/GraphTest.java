package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {
  Graph graph = new Graph(Arrays.asList(1, 2, 3));

  @BeforeEach
  public void init() throws Exception {    
    graph.setDefaultPheromone(1);
  }
  
}
