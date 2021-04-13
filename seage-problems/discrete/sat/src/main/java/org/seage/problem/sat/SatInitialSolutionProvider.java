package org.seage.problem.sat;
/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation - Added problem
 * annotations
 */

import java.util.Arrays;
import java.util.Random;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;

public class SatInitialSolutionProvider {
  /**
   * .
   * @param problemInstance .
   * @param randomSeed .
   * @return .
   * @throws Exception .
   * @author David Omrai
   */
  public SatPhenotype generateGreedySolution(
      ProblemInstance problemInstance, long randomSeed)
      throws Exception {
    Random rnd = new Random(randomSeed);
    Formula f = (Formula) problemInstance;
    
    SatProblemProvider satProblemProvider = new SatProblemProvider();

    IPhenotypeEvaluator<SatPhenotype> evaluator = 
        satProblemProvider.initPhenotypeEvaluator(problemInstance);
    
    // Create new random solution
    Boolean[] bestSolution = new Boolean[f.getLiteralCount()];
    for (int j = 0; j < f.getLiteralCount(); j++) {
      bestSolution[j] = rnd.nextBoolean();
    }
    
    double bestScore = evaluator.evaluate(new SatPhenotype(bestSolution))[0];
        
    // Find better solution using greedy algorithm
    for (int i = 0; i < f.getLiteralCount(); i++) {

      Boolean[] newSolution = Arrays.copyOf(bestSolution, bestSolution.length);

      for (int k = i; k < f.getLiteralCount(); k++) {      
        newSolution[k] = !newSolution[k]; 

        double newScore = evaluator.evaluate(new SatPhenotype(newSolution))[0];

        if (newScore < bestScore) {
          // Save the changes
          bestSolution = Arrays.copyOf(newSolution, newSolution.length);
          // Save better score
          bestScore = newScore;
        }
        // Return the changed literal
        newSolution[k] = !newSolution[k];
      }
    }

    SatPhenotype result = new SatPhenotype(bestSolution);
    double[] objVals = evaluator.evaluate(result);
    result.setObjValue(objVals[0]);
    result.setScore(objVals[1]);

    return result;
  }


  /**
   * Method creates solutions using greedy algorithm.
   * @param problemInstance Problem isntance.
   * @param numSolutions Number of solutions.
   * @param randomSeed Random seed for random generator.
   * @return Solutions.
   * @author David Omrai.
   */
  public SatPhenotype[] generateGreedySolutions(
      ProblemInstance problemInstance, int numSolutions, long randomSeed)
      throws Exception {
    Random rnd = new Random(randomSeed);
    SatPhenotype[] result = new SatPhenotype[numSolutions];
    
    for (int i = 0; i < numSolutions; i++) {
      result[i] = 
        generateGreedySolution(problemInstance, rnd.nextLong());
    }

    return result;
  }
}
