package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionAdapter;

public class TestSolution  extends SolutionAdapter 
{
    private static final long serialVersionUID = 5667060759187043483L;
    public Object[] solution;
    
    public TestSolution(Object[] sol)
    {
        solution = sol;
    }

    @Override
    public boolean equals(Solution s)
    {
        // TODO Auto-generated method stub
        return false;
    }

    

}
