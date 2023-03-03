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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 *
 * @author Richard Malek
 */

public class TspMoveManager implements MoveManager {

  // @Override
  public Move[] getAllMoves0(Solution solution) {
    Integer[] tour = ((TspSolution) solution)._tour;
    ArrayList<Move> buffer = new ArrayList<Move>();
    int span = Math.max(5, tour.length / 5);

    // Generate moves that move each customer
    // forward and back up to five spaces.
    for (int i = 0; i < tour.length; i++)
      for (int j = -span; j <= span; j++)
        if ((i + j > 0) && (i + j < tour.length) && (i != i + j))
          buffer.add(new TspMove(i, i + j));

    return buffer.toArray(new Move[] {});
  } // end getAllMoves

  Random rnd = new Random();

  // @Override
  public Move[] getAllMoves1(Solution solution) {
    TspSolution tspSoln = (TspSolution) solution;
    Integer[] tour = tspSoln._tour;
    List<Move> moves = new ArrayList<>();

    //int ix1 = rnd.nextInt(tour.length);

    // Generate moves that move each customer
    // forward and back up to five spaces.
    for (int i = 0; i < tour.length*2; i++) {
      int ix1 = rnd.nextInt(tour.length);
      int ix2 = rnd.nextInt(tour.length);
      if (ix1 == ix2)
          continue;
      moves.add(new TspMove(ix1, ix2));
    }

    return moves.toArray(new Move[] {});
  }
  Set<Integer> used = new HashSet<>();
  @Override
  public Move[] getAllMoves(Solution solution) {
    TspSolution tspSoln = (TspSolution) solution;
    Integer[] tour = tspSoln._tour;
    List<Move> moves = new ArrayList<>();
    
    for (int i = 0; i < tour.length; i++) {
      int ix1 = i;
      used.clear();
      for (int j = 0; j < 4; j++) {
        int ix2 = i + rnd.nextInt(tour.length - i);
        if (ix1 == ix2 || used.contains(ix2)) {
          continue;
        }
        moves.add(new TspMove(ix1, ix2));
        used.add(ix2);
      }
    }

    return moves.toArray(new Move[] {});
  }

} // end class MyMoveManager
