package org.seage.aal.algorithm.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestObjectiveFunction implements IObjectiveFunction
{

    @Override
    public double getObjectiveValue(Solution s)
    {
    	TestSolution sol = (TestSolution)s;
    	double val = 0;
    	for(int i=0;i<sol.solution.length-1;i++)
    		val += Math.abs((Integer)sol.solution[i]-(Integer)sol.solution[i+1]);
        
    	return val;
    }

}
