/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import java.util.Random;
import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IMoveManager;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author rick
 */
public class TspMoveManager implements IMoveManager {

    public IMove[] getAllMoves(Solution solution) {
        TspSolution sol = (TspSolution) solution;
        TspMove[] moves = new TspMove[sol._tour.length];

        Random rnd = new Random();

        for (int i = 0; i < sol._tour.length; i++) {
            moves[i] = new TspMove(rnd.nextInt(sol._tour.length), rnd.nextInt(sol._tour.length));
        }

        return (IMove[]) moves;
    }
}
