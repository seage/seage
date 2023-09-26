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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * .
 * Contributors:
 *   Jan Zmatlik
 *   - Initial implementation
 */

package org.seage.problem.rosenbrock.fireflies;

import java.util.Arrays;
import java.util.List;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.Solution;

/**
 * .
 *
 * @author Jan Zmatlik
 */
public class RosenbrockFirefliesTest {
  /**
   * .
   *
   * @param args .
   * @throws Exception .
   */
  public static void main(String[] args) throws Exception {
    int dimension = 4;
    boolean withDecreasingRandomness = false;
    boolean withHillClimbingBestSolution = false;
    boolean bestSolutionNoMove = false;
    double initialIntensity = 1;
    double initialRandomness = 5;
    double finalRandomness = 2;
    double absorption = 0.003; //0.025;
    double timeStep = 0.7; //0.15;
    int populationSize = 100;
    boolean maximizing = false;
    int iterationsToGo = 1000;
    double minBound = -10;
    double maxBound = 10;

    FireflyOperator fo = new ContinuousFireflyOperator(
        initialIntensity, initialRandomness, finalRandomness, absorption, 
        timeStep, withDecreasingRandomness, dimension, minBound, maxBound);
    RosenbrockObjectiveFunction rof = new RosenbrockObjectiveFunction();
        
    FireflySearch fs = new FireflySearch(fo, rof);
    // fs.addFireflySearchListener(this);
    fs.setWithDecreasingRandomness(withDecreasingRandomness);
    fs.setWithHillClimbingBestSolution(withHillClimbingBestSolution);
    fs.setInitialIntensity(initialIntensity);
    fs.setInitialRandomness(initialRandomness);
    fs.setFinalRandomness(finalRandomness);
    fs.setBestSolutionNoMove(bestSolutionNoMove);
    fs.setAbsorption(absorption);
    fs.setTimeStep(timeStep);
    fs.setPopulationCount(populationSize);
    fs.setMaximizing(maximizing);
    fs.setIterationsToGo(iterationsToGo);
    // System.out.println("Length of solution"+(new QapRandomSolution(facilityLocations).assign.length));
    List<Solution> solutions = Arrays.asList(generateInitialSolutions(populationSize, fo));
    fs.startSolving(solutions);  
    System.out.println(fs.getBestSolution().getObjectiveValue()[0]);
    for (int i = 0; i < dimension; i++) {
      System.out.print(" " + ((ContinuousSolution) fs.getBestSolution()).getAssign()[i]);
    }
  }

  private static Solution[] generateInitialSolutions(
      int count, FireflyOperator fo) throws Exception {
    Solution[] result = new Solution[count];
    // result[0]=new QapGreedyStartSolution(fl);
    for (int i = 0; i < count; i++) {
      result[i] = fo.randomSolution();
    }
    return result;
  }
}
