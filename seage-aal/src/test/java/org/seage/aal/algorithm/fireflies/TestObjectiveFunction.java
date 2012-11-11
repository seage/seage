package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;

public class TestObjectiveFunction implements ObjectiveFunction
{
    @Override
    public double[] evaluate(Solution soln) throws Exception
    {
        // TODO Auto-generated method stub
        return new double[]{(Integer) ((TestSolution)soln).solution[0]};
    }

    @Override
    public int getCounter()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void incrementCounter()
    {
        // TODO Auto-generated method stub
        
    }

}
