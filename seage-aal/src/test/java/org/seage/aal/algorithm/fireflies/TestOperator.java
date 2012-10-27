package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;


public class TestOperator extends FireflyOperator
{

    @Override
    public double getDistance(Solution s1, Solution s2)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void attract(Solution s0, Solution s1, int iter)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Solution randomSolution()
    {
        // TODO Auto-generated method stub
        return new TestSolution(new Object[]{1,3,2,5,4});
    }

    @Override
    public void randomSolution(Solution solution)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void modifySolution(Solution solution)
    {
        // TODO Auto-generated method stub
        
    }

}
