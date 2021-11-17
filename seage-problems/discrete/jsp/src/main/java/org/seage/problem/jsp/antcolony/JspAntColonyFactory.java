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
 *   David Omrai
 *   - Jsp implementation
 */

package org.seage.problem.jsp.antcolony;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.metaheuristic.antcolony.Ant;

import java.util.Random;
import java.util.ArrayList;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *
 * @author Richard Malek
 * Edited by David Omrai
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
    JspGraph jspGraph = new JspGraph(jobs, evaluator);
    JspAntBrain brain = new JspAntBrain(jspGraph, jobs);

    return new AntColonyAdapter<JspPhenotype, Ant>(jspGraph, phenotypeEvaluator)
    {
      @Override
      public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception
      {
        ants = new Ant[source.length];
        for (int i = 0; i < ants.length; i++)
        {
          // Current id of operation of specific job
          int[] jobsOper = new int[jobs.getJobsCount() + 1];
          for (int jobID = 0; jobID < jobsOper.length; jobID++) {
            jobsOper[jobID] = 1;
          }

          ArrayList<Integer> nodes = new ArrayList<Integer>();
          for (int j = 0; j < source[i].getSolution().length; j++) {
            // Id of job and machine, from value 0
            int jobID = source[i].getSolution()[j];
            int operID = jobsOper[jobID];
            // Add correct node id
            // node 0 = (jobID, operID) = (0,0)
            // node 1 = (0, 1), etc.
            nodes.add((jobID * jspGraph.getFactor()) + operID);

            jobsOper[jobID]++;
          }
          ants[i] = new Ant(brain, graph, nodes);
       }
      }

      @Override
      public JspPhenotype[] solutionsToPhenotype() throws Exception
      {
        JspPhenotype[] result = new JspPhenotype [ants.length];
        for (int i = 0; i < ants.length; i++)
          result[i] = solutionToPhenotype(ants[i]);
        return result;
      }

      @Override
      public JspPhenotype  solutionToPhenotype(Ant ant) throws Exception {
        Integer[] solution = new Integer[ant.getNodeIDsAlongPath().size()];
          for (int j = 0; j < ant.getNodeIDsAlongPath().size(); j++)
          {
            int nodeID = ant.getNodeIDsAlongPath().get(j);
            int jobID = nodeID / jspGraph.getFactor();
            solution[j] = jobID;
          }
        return new JspPhenotype(solution);
      }
    };
  }
}