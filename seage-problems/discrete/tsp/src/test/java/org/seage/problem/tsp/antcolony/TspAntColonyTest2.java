package org.seage.problem.tsp.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;
import org.seage.problem.tsp.TspProblemProvider;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;

public class TspAntColonyTest2 {
  @Test
  public void testAntColonyWithOneAnt() throws Exception {
    
    String instanceID = "berlin52";
    TspProblemProvider provider = new TspProblemProvider();

    ProblemInstanceInfo pii = provider.getProblemInfo().getProblemInstanceInfo(instanceID);
    TspProblemInstance instance = provider.initProblemInstance(pii);
    City[] cities = instance.getCities();
    TspGraph graph = new TspGraph(cities);
    AntColony colony = new AntColony(graph);
    TspOptimalTourBerlin52 optimum = new TspOptimalTourBerlin52();
    ArrayList<Integer> optimalTour = new ArrayList<>(Arrays.asList(optimum.OptimalTour));
    optimalTour.add(1);
    Ant ant = new TspAnt(graph, optimalTour, cities);
    colony.setParameters(1, 1, 1, 1, 1, 0.8);
    colony.startExploring(graph.getNodes().get(1), new Ant[] {ant});

    assertEquals(optimalTour, Graph.edgeListToNodeIds(colony.getBestPath()));
    assertEquals(optimum.OptimalLength, colony.getGlobalBest());
  }
}
