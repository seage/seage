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
 * @author Martin Zaloga
 */
public class TspMoveManager implements IMoveManager {

    /**
     * Function which generates the next steps algorithm
     * @param solution - Current solutions for which are generated further steps
     * @return - The next steps algorithm
     */
    public IMove[] getAllMoves(Solution solution) {
        TspSolution sol = (TspSolution) solution;
        TspMove[] moves = new TspMove[sol.getTour().length];
        Random rnd = new Random();

        /*random selection of sequence the steps*/
        for (int i = 0; i < sol.getTour().length; i++) {
            moves[i] = new TspMove(rnd.nextInt(sol.getTour().length), rnd.nextInt(sol.getTour().length));
        }
        
        return (IMove[]) moves;
    }
}
