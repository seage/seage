package org.seage.problem.sat;

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
}
