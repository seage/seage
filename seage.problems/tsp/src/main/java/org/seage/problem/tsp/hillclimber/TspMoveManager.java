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
        if (solution.switcher.equals("Random") || solution.switcher.equals("random")) {
            TspRandomSolution2 sol = (TspRandomSolution2) solution;
            TspMove[] moves = new TspMove[sol.getTour().length];

            Random rnd = new Random();

            for (int i = 0; i < sol.getTour().length; i++) {
                moves[i] = new TspMove(rnd.nextInt(sol.getTour().length), rnd.nextInt(sol.getTour().length));
            }
            return (IMove[]) moves;
        }

        if (solution.switcher.equals("Greedy") || solution.switcher.equals("greedy")) {
            TspGreedySolution2 sol = (TspGreedySolution2) solution;
            TspMove[] moves = new TspMove[sol.getTour().length];

            Random rnd = new Random();

            for (int i = 0; i < sol.getTour().length; i++) {
                moves[i] = new TspMove(rnd.nextInt(sol.getTour().length), rnd.nextInt(sol.getTour().length));
            }
            return (IMove[]) moves;
        }

        return (IMove[]) null;
    }
}
