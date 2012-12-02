package org.seage.aal.algorithm.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.Solution;

public class TestObjectiveFunction implements ObjectiveFunction
{
	@Override
    public double[] evaluate(Solution soln, Move move) throws Exception
    {		
		TestSolution sol = (TestSolution)soln.clone();
		if(move != null)
			move.operateOn(sol);
    	double val = 0;
    	for(int i=0;i<sol.solution.length-1;i++)
    		val += Math.abs((Integer)sol.solution[i]-(Integer)sol.solution[i+1]);
        
        return new double[]{val};
    }

}
