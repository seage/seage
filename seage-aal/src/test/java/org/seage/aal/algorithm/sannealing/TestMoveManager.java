package org.seage.aal.algorithm.sannealing;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

public class TestMoveManager implements IMoveManager
{

    @Override
    public Solution getModifiedSolution(Solution solution)
    {
        return solution;
    }

}
