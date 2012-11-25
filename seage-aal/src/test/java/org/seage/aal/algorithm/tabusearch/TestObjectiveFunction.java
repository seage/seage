package org.seage.aal.algorithm.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.Solution;

public class TestObjectiveFunction implements ObjectiveFunction
{
	@Override
    public double[] evaluate(Solution soln, Move move) throws Exception
    {
        return new double[]{(Integer) ((TestSolution)soln).solution[0]};
    }

}
