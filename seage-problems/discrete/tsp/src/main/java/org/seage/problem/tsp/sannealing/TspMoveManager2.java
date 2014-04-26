/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import java.util.Random;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class TspMoveManager2 implements IMoveManager
{
	Random rnd = new Random(1);
	private TspObjectiveFunction2 _objFunc;
	
	public TspMoveManager2(TspObjectiveFunction2 objFunc)
	{
		_objFunc = objFunc;
	}
	
    public Solution getModifiedSolution0(Solution solution) throws Exception
    {
        TspSolution tspSolution = ((TspSolution)solution).clone();        
        
        int tspSolutionLength = tspSolution.getTour().length;
        int a = Math.min(Math.max(1, rnd.nextInt( tspSolutionLength )), tspSolutionLength-1);
        int[] move = new int[2];
        int[] bestMove = null;
        move[0] = a;
        move[1] = 0;
        double bestVal = _objFunc.evaluate(tspSolution, move)[0];
        
        for(int i=1;i<tspSolutionLength;i++)
        {
        	if(i == a)
        	    continue;
        	move[1] = i;
        	double val = _objFunc.evaluate(tspSolution, move)[0];
        	if(val < bestVal)
        	{
        		bestVal = val;
        		bestMove = move;
        	}
        }

        // Swap values if indices are different
        if(bestMove != null)
        {  
        	int tmp = tspSolution.getTour()[bestMove[0]];
            tspSolution.getTour()[bestMove[0]] = tspSolution.getTour()[bestMove[1]];
            tspSolution.getTour()[bestMove[1]] = tmp;            
        }

        return (Solution)tspSolution;
    }
    
    public Solution getModifiedSolution(Solution solution, double currentTemperature) throws Exception
    {
    	TspSolution tspSolution = ((TspSolution)solution).clone();  
    	Integer[] next = neighbour(tspSolution.getTour(), currentTemperature);
    	for(int i=0;i<tspSolution.getTour().length;i++)
    	{
    		tspSolution._tour[i] = next[i];
    	}
    	
    	return tspSolution;
    }
    
    Integer[] neighbour(Integer[] state, double curTemp) {
        int n = state.length;
        int i = rnd.nextInt(n);
        int w = (int) Math.min(n * curTemp / 2, (Integer.MAX_VALUE/2 -1));
        int j = Math.abs(i + rnd.nextInt(2 * w + 1) - w + n) % n;
        Integer[] newState = state.clone();
        int sign = i - j;
        // reverse order from i to j
        while (sign * (i - j) > 0) {
          int t = newState[i];
          newState[i] = newState[j];
          newState[j] = t;
          i = (i + 1) % n;
          j = (j - 1 + n) % n;
        }
        return newState;
      }

}
