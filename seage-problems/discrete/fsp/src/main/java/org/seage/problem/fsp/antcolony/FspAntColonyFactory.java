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
 *     David Omrai
 *     - Fsp implementation
 */

package org.seage.problem.fsp.antcolony;

import java.util.ArrayList;
import java.util.List;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.antcolony.JspAntColonyFactory;
/**
 * .
 * @author Richard Malek
 * @author Edited by David Omrai
 */

@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
@Annotations.NotReady
public class FspAntColonyFactory extends JspAntColonyFactory {
  
  @Override
  public IAlgorithmAdapter<JspPhenotype, Ant> createAlgorithm(
      ProblemInstance instance, IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator
  ) throws Exception {
    JspJobsDefinition jobs = (JspJobsDefinition) instance;
    FspGraph fspGraph = new FspGraph(jobs, (JspPhenotypeEvaluator) phenotypeEvaluator);
    return new AntColonyAdapter<JspPhenotype, Ant>(fspGraph, phenotypeEvaluator) {
      
      @Override
      public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception {
        ants = new Ant[source.length];
        for (int i = 0; i < ants.length; i++) {
          
          ArrayList<Integer> nodes = new ArrayList<>();
          for (int j = 0; j < source[i].getSolution().length; j++) {
            // Add next node
            nodes.add(source[i].getSolution()[j]);
          }
          // Add the starting node
          nodes.add(0, 0);
          ants[i] = new FspAnt(
              graph.nodesToNodePath(nodes), 
              jobs, (JspPhenotypeEvaluator) phenotypeEvaluator);
        }
      }

      @Override
      public JspPhenotype[] solutionsToPhenotype() throws Exception {
        JspPhenotype[] result = new JspPhenotype [ants.length];
        for (int i = 0; i < ants.length; i++) {
          result[i] = solutionToPhenotype(ants[i]);
        }
        return result;
      }

      @Override
      public JspPhenotype solutionToPhenotype(Ant ant) throws Exception {
        List<Integer> nodePath = ant.getNodeIDsAlongPath();
        // Remove the starting node
        nodePath.remove(0);

        Integer[] jobArray = nodePath.toArray(new Integer[0]);

        JspPhenotype result = new JspPhenotype(jobArray);
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };
  }
}
