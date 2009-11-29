
package org.seage.tsp.metaheuristic.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmátlík
 */
public class TspMoveManager implements IMoveManager
{

    public Solution getModifiedSolution(Solution solution) {
        
        TspSolution tspSolution = ((TspSolution)solution).clone();
        System.out.println("OldHash: " + solution.hashCode());
        System.out.println("NewHash: " + tspSolution.hashCode());

        Random rnd = new Random();
        int tspSolutionLength = tspSolution.getTour().length;
        int a = rnd.nextInt( tspSolutionLength );
        int b = rnd.nextInt( tspSolutionLength );

        int t = tspSolution.getTour()[a];
        tspSolution.getTour()[a] = tspSolution.getTour()[b];
        tspSolution.getTour()[b] = tspSolution.getTour()[t];

        return (Solution)tspSolution;
    }

}
