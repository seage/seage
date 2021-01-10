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
 * Contributors: Martin Zaloga - Initial implementation
 */
package org.seage.problem.sat.grasp;

import java.util.Random;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IMoveManager;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatMoveManager implements IMoveManager {

  private Random _rnd = new Random();
  private SatMove[] _moves;

  /**
   * Function which generates the next steps algorithm
   * 
   * @param solution - Current solutions for which are generated further steps
   * @return - The next steps algorithm
   */
  @Override
  public IMove[] getAllMoves(Solution solution) {
    SatSolution sol = (SatSolution) solution;
    SatMove[] moves = new SatMove[sol.getLiteralCount()];

    /* random selection of sequence the steps */
    for (int i = 0; i < sol.getLiteralCount(); i++) {
      moves[i] = new SatMove(_rnd.nextInt(sol.getLiteralCount()));
    }
    _moves = moves;
    return moves;
  }

  public void printMoves() {
    for (int i = 0; i < _moves.length; i++) {
      System.out.println("move[" + i + "] " + _moves[i].toString());
    }
  }
}
