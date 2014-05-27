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

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspMoveBasedEvaluator;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
	protected TspMoveBasedEvaluator _evaluator;
	
    public TspObjectiveFunction(City[] cities)
    {
    	_evaluator = new TspMoveBasedEvaluator(cities.clone());
    }   // end constructor

    
    public double[] evaluate(TspSolution solution, int[] move) throws Exception
    {
    	return _evaluator.evaluate(solution._tour, move, solution.getObjectiveValue());
//        try
//        {
//            Integer[] tour = solution._tour;
//            int len = tour.length;
//
//            // If move is null, calculate distance from scratch
//            if (move == null)
//            {
//                double dist = 0;
//                for (int i = 0; i < len; i++)
//                {
//                    dist += getCityDistance(tour[i]-1,i + 1 >= len ? tour[0]-1 : tour[i + 1]-1);                    
//                }
//
//                return new double[]{ dist };
//            } // end if: move == null
//            // Else calculate incrementally
//            else
//            {
//                //int mv = (TspSwapMove) move;
//                int pos1 = move[0];
//                int pos2 = move[1];
//                
//                // Prior objective value
//                double dist = 0;//solution.getObjectiveValue();
//
//                int pos1L = (pos1-1+len) % len;
//            	int pos1R = (pos1+1+len) % len;
//            	int pos2L = (pos2-1+len) % len;
//            	int pos2R = (pos2+1+len) % len;
//            	
//            	int delta = Math.abs(pos1-pos2);
//                // Treat a pair swap move differently
//                if (delta==1)
//                {
//                    //     | |
//                    // A-B-C-D-E: swap C and D, say (works for symmetric matrix only)
//                    dist -= getCityDistance(tour[pos1L]-1, tour[pos1]-1);           // -BC
//                    dist -= getCityDistance(tour[pos2]-1, tour[pos2R]-1);   // -DE
//                    dist += getCityDistance(tour[pos1L]-1, tour[pos2]-1);           // +BD
//                    dist += getCityDistance(tour[pos1]-1, tour[pos2R]-1);   // +CE
//                    return new double[] { solution.getObjectiveValue() + dist };
//                } // end if: pair swap
//                else if (delta==(len-1))
//                {
//                	// |       |
//                    // A-B-C-D-E: swap A and E, say (works for symmetric matrix only)
//                    dist -= getCityDistance(tour[pos1]-1, tour[pos1R]-1);   // -AB
//                    dist -= getCityDistance(tour[pos2L]-1, tour[pos2]-1);   // -DE
//                    dist += getCityDistance(tour[pos1R]-1, tour[pos2]-1);   // +EB
//                    dist += getCityDistance(tour[pos1]-1, tour[pos2L]-1);   // +DA
//                    return new double[] { solution.getObjectiveValue() +dist };
//                }
//                // Else the swap is separated by at least one customer
//                else
//                {                	
//                    //   |     |
//                    // A-B-C-D-E-F: swap B and E, say
//                    dist -= getCityDistance(tour[pos1L]-1,tour[pos1]-1);           // -AB
//                    dist -= getCityDistance(tour[pos1]-1, tour[pos1R]-1);         // -BC
//                    dist -= getCityDistance(tour[pos2L]-1, tour[pos2]-1);           // -DE
//                    dist -= getCityDistance(tour[pos2]-1, tour[pos2R]-1);   // -EF
//
//                    dist += getCityDistance(tour[pos1L]-1, tour[pos2]-1);           // +AE
//                    dist += getCityDistance(tour[pos2]-1, tour[pos1R]-1);         // +EC
//                    dist += getCityDistance(tour[pos2L]-1, tour[pos1]-1);           // +DB
//                    dist += getCityDistance(tour[pos1]-1, tour[pos2R]-1);   // +BF
//                    return new double[]{ solution.getObjectiveValue() + dist };
//                }   // end else: not a pair swap
//            }   // end else: calculate incremental
//        } catch (Exception ex)
//        {
//            throw ex;
//        }
    }   // end evaluate

//    private double getCityDistance(int i, int j)
//	{		
//		return getEuclidianDistance(_cities[i].X, _cities[i].Y, _cities[j].X, _cities[j].Y);
//	}


	/** Calculate distance between two points. */
//    private double getEuclidianDistance(double x1, double y1, double x2, double y2)
//    {
//        double xDiff = x2 - x1;
//        double yDiff = y2 - y1;
//        return Math.round(Math.sqrt(xDiff * xDiff + yDiff * yDiff));
//    }   // end norm
//
	@Override
	public double getObjectiveValue(Solution s) throws Exception {
		return evaluate((TspSolution)s, null)[0];
	}

}
