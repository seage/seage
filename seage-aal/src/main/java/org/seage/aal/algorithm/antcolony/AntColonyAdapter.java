/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm.antcolony;

import java.util.List;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AntColony adapter base implementation.
 * 
 * @author Richard Malek
 *
 */
@AlgorithmParameters({ @Parameter(name = "numSolutions", min = 10, max = 1000, init = 100),
    @Parameter(name = "iterationCount", min = 10, max = 1000000, init = 1000000),
    @Parameter(name = "alpha", min = 1, max = 10, init = 1), 
    @Parameter(name = "beta", min = 1, max = 10, init = 3),
    @Parameter(name = "defaultPheromone", min = 0.00001, max = 1.0, init = 0.00001),
    @Parameter(name = "quantumOfPheromone", min = 1, max = 1000, init = 10),
    @Parameter(name = "localEvaporation", min = 0.5, max = 0.98, init = 0.95) })
public abstract class AntColonyAdapter<P extends Phenotype<?>, S extends Ant> 
    extends AlgorithmAdapterImpl<P, S> {

  private static final Logger logger = 
      LoggerFactory.getLogger(AntColonyAdapter.class.getName());
      
  protected AntColony antColony;
  protected Graph graph;
  private AlgorithmParams algParams;
  protected Ant[] ants;

  private long statNumIterationsDone;
  private long statNumNewBestSolutions;
  private long statLastImprovingIteration;
  private double initialSolutionValue;
  private double bestSolutionValue;
  public double averageSolutionValue;

  /**
   * AntColonyAdapter base implementation.
   * @param graph .
   * @param phenotypeEvaluator .
   */
  public AntColonyAdapter(Graph graph, IPhenotypeEvaluator<P> phenotypeEvaluator) {
    super(phenotypeEvaluator);
    this.algParams = null;
    this.graph = graph;
    this.phenotypeEvaluator = phenotypeEvaluator;
    this.antColony = new AntColony(graph);
    this.antColony.addAntColonyListener(new AntColonyListener());
  }

  /**
   * Sets the new algorithm parameters.
   * @param params .
   * @throws Exception .
   */
  public void setParameters(AlgorithmParams params) throws Exception {
    this.algParams = params;

    int iterationCount = algParams.getValueInt("iterationCount");
    double alpha = algParams.getValueDouble("alpha");
    double beta = algParams.getValueDouble("beta");
    double defaultPheromone = algParams.getValueDouble("defaultPheromone");
    double quantumOfPheromone = algParams.getValueDouble("quantumOfPheromone");
    double localEvaporation = algParams.getValueDouble("localEvaporation");
    antColony.setParameters(
        iterationCount, alpha, beta, quantumOfPheromone, defaultPheromone, localEvaporation);
  }

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

    reporter = new AlgorithmReporter<>(phenotypeEvaluator);
    reporter.putParameters(algParams);

    statLastImprovingIteration = 0;
    statNumIterationsDone = 0;
    statNumNewBestSolutions = 0;
    averageSolutionValue = 0;
    initialSolutionValue = bestSolutionValue = Double.MAX_VALUE;

    antColony.startExploring(graph.getNodes().values().iterator().next(), ants);

  }

  @Override
  public void stopSearching() throws Exception {
    antColony.stopExploring();

    while (isRunning()) {
      Thread.sleep(100);
    }
  }

  @Override
  public boolean isRunning() {
    return antColony.isRunning();
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    reporter.putStatistics(statNumIterationsDone, statNumNewBestSolutions,
        statLastImprovingIteration, initialSolutionValue, averageSolutionValue, bestSolutionValue);
    return reporter.getReport();
  }

  private class AntColonyListener implements IAlgorithmListener<AntColonyEvent> {
    @Override
    public void algorithmStarted(AntColonyEvent e) {
      algorithmStarted = true;

    }

    @Override
    public void algorithmStopped(AntColonyEvent e) {
      algorithmStopped = true;
      statNumIterationsDone = e.getAntColony().getCurrentIteration();
    }

    @Override
    public void newBestSolutionFound(AntColonyEvent e) {
      AntColony alg = e.getAntColony();
      averageSolutionValue = bestSolutionValue = alg.getGlobalBest();
      statLastImprovingIteration = alg.getCurrentIteration();
      averageSolutionValue = bestSolutionValue = alg.getGlobalBest();

      if (statNumNewBestSolutions == 0) {
        initialSolutionValue = bestSolutionValue;
      }

      statNumNewBestSolutions++;

      try {
        // TODO: A - Remove casting
        P solution = solutionToPhenotype((S) alg.getBestAnt());
        reporter.putNewSolution(System.currentTimeMillis(), alg.getCurrentIteration(), solution);
      } catch (Exception ex) {
        logger.warn(ex.getMessage(), ex);
      }
    }

    @Override
    public void noChangeInValueIterationMade(AntColonyEvent e) {
    }

    @Override
    public void iterationPerformed(AntColonyEvent e) {
      if (e.getAntColony().getCurrentIteration() == 1) {
        initialSolutionValue = e.getAntColony().getGlobalBest();
      }
    }

    private String createNodeListString(List<Edge> bestPath) {
      Integer[] nodeIDs = new Integer[bestPath.size() + 1];
      String result = "";
      nodeIDs[0] = bestPath.get(0).getNode1().getID();
      if (nodeIDs[0] != bestPath.get(1).getNode1().getID()
          || nodeIDs[0] != bestPath.get(1).getNode2().getID()) {
        nodeIDs[0] = bestPath.get(0).getNode2().getID();
      }
      for (int i = 1; i < nodeIDs.length - 1; i++) {
        nodeIDs[i] = bestPath.get(i).getNode1().getID();
        if (i > 0 && nodeIDs[i - 1].equals(nodeIDs[i])) {
          nodeIDs[i] = bestPath.get(i).getNode2().getID();
        }
        result += nodeIDs[i] + " ";
      }
      nodeIDs[nodeIDs.length - 1] = nodeIDs[0];

      return result;
    }

  }

}
