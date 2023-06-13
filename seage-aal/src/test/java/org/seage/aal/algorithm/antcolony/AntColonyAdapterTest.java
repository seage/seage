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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm.antcolony;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.TestPhenotypeEvaluator;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Richard Malek
 */
public class AntColonyAdapterTest extends AlgorithmAdapterTestBase<Ant> {

  public AntColonyAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    Graph graph = new TestGraph();
    for (int i = 0; i < SOLUTION_LENGTH; i++) {
      Node n1 = new Node(i + 1);
      graph.getNodes().put(i + 1, n1);

    }

    this.algAdapter = new TestAntColonyAdapter(graph, new TestPhenotypeEvaluator());
    this.algParams = new AlgorithmParams();

    this.algParams.putValue("iterationCount", 3);
    this.algParams.putValue("alpha", 100);
    this.algParams.putValue("beta", 1);
    this.algParams.putValue("quantumOfPheromone", 1);
    this.algParams.putValue("localEvaporation", 1);
  }

  @Test
  @Override
  public void testAlgorithm() throws Exception {
    super.testAlgorithm();
  }

  @Test
  @Override
  public void testAlgorithmWithParamsAtZero() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    // params.putValue("numAnts", 0);
    params.putValue("iterationCount", 0);
    params.putValue("alpha", 0);
    params.putValue("beta", 0);
    params.putValue("quantumOfPheromone", 0);
    params.putValue("localEvaporation", 0);
    super.setAlgParameters(params);
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    // params.putValue("numAnts", 0.1);
    params.putValue("iterationCount", 1000000);
    params.putValue("alpha", 100);
    params.putValue("beta", 1);
    params.putValue("quantumOfPheromone", 1);
    params.putValue("localEvaporation", 1);

    super.setAlgParameters(params);
    super.testAsyncRunning();
  }

  @Test
  @Override
  public void testReport() throws Exception {
    super.testReport();
  }

  @Test
  @Override
  public void testAlgorithmWithParamsNull() throws Exception {
    super.testAlgorithmWithParamsNull();
  }
}
