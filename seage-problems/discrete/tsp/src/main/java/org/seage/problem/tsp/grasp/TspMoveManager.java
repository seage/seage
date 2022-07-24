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
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.grasp;

import java.util.Random;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IMoveManager;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class TspMoveManager implements IMoveManager {

  /**
   * Function which generates the next steps algorithm
   * 
   * @param solution - Current solutions for which are generated further steps
   * @return - The next steps algorithm
   */
  @Override
  public IMove[] getAllMoves(Solution solution) {
    TspSolution sol = (TspSolution) solution;
    TspMove[] moves = new TspMove[sol.getTour().length];
    Random rnd = new Random();

    /* random selection of sequence the steps */
    for (int i = 0; i < sol.getTour().length; i++) {
      moves[i] = new TspMove(rnd.nextInt(sol.getTour().length), rnd.nextInt(sol.getTour().length));
    }

    return moves;
  }
}
