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

import java.util.Arrays;
import java.util.Random;
import org.seage.aal.algorithm.IPhenotypeEvaluator;

public class SatInitialSolutionProvider {
  /**
   * Method creates solution using greedy algorithmgit.
   * @param formula SAT formula.
   * @param evaluator Evaluator.
   * @param seed Seed for random generator.
   * @return Problem solution.
   * @author David Omrai
   */
  public static SatPhenotype generateGreedySolution(
      Formula formula, 
      IPhenotypeEvaluator<SatPhenotype> evaluator, long seed)
      throws Exception {
    Random rnd = new Random(seed);
    
    // Create new random solution
    Boolean[] bestSolution = new Boolean[formula.getLiteralCount()];
    for (int j = 0; j < formula.getLiteralCount(); j++) {
      bestSolution[j] = rnd.nextBoolean();
    }
    
    double bestScore = evaluator.evaluate(new SatPhenotype(bestSolution))[0];
        
    // Find better solution using greedy algorithm
    for (int i = 0; i < formula.getLiteralCount(); i++) {

      Boolean[] newSolution = Arrays.copyOf(bestSolution, bestSolution.length);

      for (int k = i; k < formula.getLiteralCount(); k++) {      
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
   * @param formula SAT formula.
   * @param numSolutions Number of solutions.
   * @param randomSeed Random seed for random generator.
   * @return Solutions.
   * @author David Omrai.
   */
  public static SatPhenotype[] generateGreedySolutions(
      Formula formula, 
      IPhenotypeEvaluator<SatPhenotype> evaluator, int numSolutions, long randomSeed)
      throws Exception {
    Random rnd = new Random(randomSeed);
    SatPhenotype[] result = new SatPhenotype[numSolutions];
    
    for (int i = 0; i < numSolutions; i++) {
      result[i] = 
        generateGreedySolution(formula, evaluator, rnd.nextLong());
    }

    return result;
  }
}
