/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *   Richard Malek
 *   - Initial implementation
 */

package org.seage.problem.jsp.antcolony;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.metaheuristic.antcolony.Ant;

import java.util.Random;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
public class JspAntColonyFactory implements IAlgorithmFactory<JspPhenotype, Ant>
{
  Random rnd = new Random();
  @Override
  public Class<?> getAlgorithmClass()
  {
    return AntColonyAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<JspPhenotype, Ant> createAlgorithm(
    ProblemInstance instance, IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator) throws Exception
  {
    JobsDefinition jobs = (JobsDefinition) instance;
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotypeEvaluator evaluator =
        new JspPhenotypeEvaluator(problemProvider.getProblemInfo(), (JobsDefinition) instance);
    JspAntColonySolution graph = new JspAntColonySolution(jobs, evaluator);
    JspAntBrain brain = new JspAntBrain(graph, jobs);

    return new AntColonyAdapter<JspAntColonySolution, Ant>(graph, phenotypeEvaluator)
    {
      @Override
      public void solutionsFromPhenotype(Integer[] source) throws Exception
      {
        _ants = new Ant[source.length];
        for (int i = 0; i < _ants.length; i++)
        {
          ArrayList<Integer> nodes = new ArrayList<Integer>();
          for (int j = 0; j < source[i].length; j++)
            nodes.add((Integer) source[i][j]);
          _ants[i] = new Ant(nodes);
       }
      }

      @Override
      public Object[][] solutionsToPhenotype() throws Exception
      {
        Object[][] result = new Object[_ants.length][];
        for (int i = 0; i < _ants.length; i++)
        {
          result[i] = new Integer[_ants[i].getNodeIDsAlongPath().size()];
          for (int j = 0; j < result[i].length; j++)
          {
            result[i][j] = _ants[i].getNodeIDsAlongPath().get(j);
          }
        }
        return result;
      }
    };
  }

  // @Override
  // public IAlgorithmAdapter createAlgorithm(ProblemInstance instance, IPhenotypeEvaluator phenotypeEvaluator)
  //     throws Exception {
  //   // TODO Auto-generated method stub
  //   return null;
  // }
}