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
 * .
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.problem.tsp.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;


/**
 * .
 *
 * @author Jan Zmatlik
 */
public class TspMoveManager implements IMoveManager {
  Random rnd = new Random();

  @Override
  public Solution getModifiedSolution(Solution solution, double ct) {
   
    return getSwappedSolution(solution);
    // return getInversedSolution(solution);
    // return getInsertedSolution(solution);
  }

  private TspSolution getSwappedSolution(Solution solution) {
    TspSolution tspSolution = ((TspSolution) solution).clone();

    int tspSolutionLength = tspSolution.getTour().length;
    int a = rnd.nextInt(tspSolutionLength);
    int b = rnd.nextInt(tspSolutionLength);

    // Swap values if indices are different
    if (a != b) {
      int tmp = tspSolution.getTour()[a];
      tspSolution.getTour()[a] = tspSolution.getTour()[b];
      tspSolution.getTour()[b] = tmp;
    }

    return tspSolution;
  }

  private TspSolution getInversedSolution(Solution solution) {
    TspSolution tspSolution = ((TspSolution) solution).clone();
    
    int tspSolutionLength = tspSolution.getTour().length;
    int fromPos = rnd.nextInt(tspSolutionLength);
    int toPos = Math.min((fromPos + rnd.nextInt(5)), tspSolutionLength - 1);


    for (int i = fromPos; i < toPos; i++) {
      int tmp = tspSolution.getTour()[i];
      tspSolution.getTour()[i] = tspSolution.getTour()[toPos - (i - fromPos)];
      tspSolution.getTour()[toPos - (i - fromPos)] = tmp;
    }

    return tspSolution;
  }

  private TspSolution getInsertedSolution(Solution solution) {
    TspSolution tspSolution = ((TspSolution) solution).clone();

    int tspSolutionLength = tspSolution.getTour().length;

    int fromPos = rnd.nextInt(tspSolutionLength);
    int toPos = rnd.nextInt(tspSolutionLength);

    int fromCity = tspSolution.getTour()[fromPos];

    if (fromPos < toPos) {
      for (int i = fromPos; i < toPos; i++) {
        tspSolution.getTour()[i] = tspSolution.getTour()[i + 1];
      }
    } else {
      for (int i = fromPos; i > toPos; i--) {
        tspSolution.getTour()[i] = tspSolution.getTour()[i - 1];
      }
    }

    tspSolution.getTour()[toPos] = fromCity;

    return tspSolution;
  }
}
