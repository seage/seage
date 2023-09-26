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

package org.seage.problem.rosenbrock.genetics;

import java.util.ArrayList;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.rosenbrock.RosenbrockFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rosenbrock genetic algorithm test.
 */
public class RosenbrockGeneticAlgorithmTest {
  private static final Logger logger = 
      LoggerFactory.getLogger(RosenbrockGeneticAlgorithmTest.class.getName());
  
  /**
   * .
   */
  public static void main(String[] args) throws Exception {
    try {
      // Dimension of the Rosenbrock function
      int dim = 8;

      double[] test = new double[dim];
      for (int i = 0; i < dim; i++) {
        test[i] = 1;
      }
      System.out.println(RosenbrockFunction.f(test));

      ContinuousGeneticOperator.Limit[] limits = new ContinuousGeneticOperator.Limit[dim];
      ContinuousGeneticOperator<Subject<Double>> operator =
          new ContinuousGeneticOperator<>(limits);

      for (int i = 0; i < dim; i++) {
        limits[i] = new ContinuousGeneticOperator.Limit(-10, 10);
      }

      GeneticAlgorithm<Subject<Double>> gs =
          new GeneticAlgorithm<>(operator, new RosenbrockEvaluator());
      gs.addGeneticSearchListener(new IAlgorithmListener<GeneticAlgorithmEvent<Subject<Double>>>() {

        @Override
        public void noChangeInValueIterationMade(GeneticAlgorithmEvent<Subject<Double>> e) {
          // TODO Auto-generated method stub
        }

        @Override
        public void newBestSolutionFound(GeneticAlgorithmEvent<Subject<Double>> e) {
          System.out.println(e.getGeneticSearch().getBestSubject().getObjectiveValue()[0]);

        }

        @Override
        public void iterationPerformed(GeneticAlgorithmEvent<Subject<Double>> e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void algorithmStopped(GeneticAlgorithmEvent<Subject<Double>> e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void algorithmStarted(GeneticAlgorithmEvent<Subject<Double>> e) {
          // TODO Auto-generated method stub
        }
      });
      gs.setCrossLengthPct(40);
      gs.setEliteSubjectsPct(10);
      gs.setIterationToGo(10000);
      gs.setMutateChromosomeLengthPct(20);
      gs.setMutatePopulationPct(10);
      gs.setPopulationCount(1000);
      gs.setRandomSubjectsPct(1);

      Double[] dimArray = new Double[dim];
      for (int i = 0; i < dim; i++) {
        dimArray[i] = 0.0;
      }

      ArrayList<Subject<Double>> subjects = new ArrayList<>();
      for (int i = 0; i < gs.getPopulationCount(); i++) {
        subjects.add(new Subject<>(dimArray));
        operator.randomize(subjects.get(i));
      }

      gs.startSearching(subjects);

      System.out.println(gs.getBestSubject().getObjectiveValue()[0]);
      for (int i = 0; i < dim; i++) {
        System.out.print(gs.getBestSubject().getChromosome().getGene(i) + " ");
      }
      System.out.println();
    } catch (Exception ex) {
      // TODO Auto-generated catch block
      logger.error("{}", ex.getMessage(), ex);
    }
  }
}
