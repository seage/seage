package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;

public class TestObjectiveFunction implements ObjectiveFunction
{
    @Override
    public double[] evaluate(Solution soln) throws Exception
    {
        return new double[] { (Integer) ((TestSolution) soln).solution[0] };
    }

    @Override
    public int getCounter()
    {
        return 0;
    }

    @Override
    public void incrementCounter()
    {

    }

}
