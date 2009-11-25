/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.tsp.metaheuristic.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author rick
 */
public class TspMoveManager implements IMoveManager
{

    public Solution getModifiedSolution(Solution solution) {
        // TODO: A - implement Solution.clone()
        TspSolution sol = (TspSolution)solution;

        Random rnd = new Random();

        int a = rnd.nextInt(sol._tour.length);
        int b = rnd.nextInt(sol._tour.length);

        int t = sol._tour[a];
        sol._tour[a] = sol._tour[b];
        sol._tour[b] = sol._tour[a];

        return sol;
    }

}
