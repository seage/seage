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
import org.seage.metaheuristic.tabusearch.Move;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction2 implements IObjectiveFunction
{
	public double[][] _matrix;

    public TspObjectiveFunction2(City[] cities)
    {
        int numCities = cities.length;
        double[][] customers = new double[numCities][];

        for (int i = 0; i < numCities; i++)
        {
            customers[i] = new double[2];
            customers[i][0] = (Double) cities[i].X;
            customers[i][1] = (Double) cities[i].Y;
        }

        _matrix = createMatrix(customers);
    }   // end constructor

    public double[] evaluate(TspSolution solution, int[] move) throws Exception
    {
        try
        {
            Integer[] tour = solution._tour;
            int len = tour.length;

            // If move is null, calculate distance from scratch
            if (move == null)
            {
                double dist = 0;
                for (int i = 0; i < len; i++)
                {
                    dist += _matrix[tour[i]][i + 1 >= len ? tour[0] : tour[i + 1]];                    
                }
                /*for (int i = 0; i < len-1; i++)
                {
                    dist += _matrix[tour[i]][tour[i + 1]];                    
                }*/

                return new double[]{ dist };
            } // end if: move == null
            // Else calculate incrementally
            else
            {
                //int mv = (TspSwapMove) move;
                int pos1 = move[0];
                int pos2 = move[1];

                // Find positions
                /*for (int i = 0; i < tour.length; i++)
                {
                    if (tour[i] == move[0])
                    {
                        pos1 = i;
                        break;
                    }
                }*/
                //pos2 =  move[1];

                // Logic below requires pos1 < pos2
                /*if (pos1 > pos2)
                {
                    int temp = pos2;
                    pos2 = pos1;
                    pos1 = temp;
                }*/   // end if

                // Prior objective value
                double dist = 0;//solution.getObjectiveValue();

                int pos1L = (pos1-1+len) % len;
            	int pos1R = (pos1+1+len) % len;
            	int pos2L = (pos2-1+len) % len;
            	int pos2R = (pos2+1+len) % len;
            	
            	int delta = Math.abs(pos1-pos2);
                // Treat a pair swap move differently
                if (delta==1)
                {
                    //     | |
                    // A-B-C-D-E: swap C and D, say (works for symmetric matrix only)
                    dist -= _matrix[tour[pos1L]][tour[pos1]];           // -BC
                    dist -= _matrix[tour[pos2]][tour[pos2R]];   // -DE
                    dist += _matrix[tour[pos1L]][tour[pos2]];           // +BD
                    dist += _matrix[tour[pos1]][tour[pos2R]];   // +CE
                    return new double[] { dist };
                } // end if: pair swap
                else if (delta==(len-1))
                {
                	// |       |
                    // A-B-C-D-E: swap A and E, say (works for symmetric matrix only)
                    dist -= _matrix[tour[pos1]][tour[pos1R]];   // -AB
                    dist -= _matrix[tour[pos2L]][tour[pos2]];   // -DE
                    dist += _matrix[tour[pos1R]][tour[pos2]];   // +EB
                    dist += _matrix[tour[pos1]][tour[pos2L]];   // +DA
                    return new double[] { dist };
                }
                // Else the swap is separated by at least one customer
                else
                {                	
                    //   |     |
                    // A-B-C-D-E-F: swap B and E, say
                    dist -= _matrix[tour[pos1L]][tour[pos1]];           // -AB
                    dist -= _matrix[tour[pos1]][tour[pos1R]];         // -BC
                    dist -= _matrix[tour[pos2L]][tour[pos2]];           // -DE
                    dist -= _matrix[tour[pos2]][tour[pos2R]];   // -EF

                    dist += _matrix[tour[pos1L]][tour[pos2]];           // +AE
                    dist += _matrix[tour[pos2]][tour[pos1R]];         // +EC
                    dist += _matrix[tour[pos2L]][tour[pos1]];           // +DB
                    dist += _matrix[tour[pos1]][tour[pos2R]];   // +BF
                    return new double[]{ dist };
                }   // end else: not a pair swap
            }   // end else: calculate incremental
        } catch (Exception ex)
        {
            throw ex;
        }
    }   // end evaluate

    /** Create symmetric matrix. */
    private double[][] createMatrix(double[][] customers)
    {
        int len = customers.length;
        double[][] matrix = new double[len][len];

        for (int i = 0; i < len; i++)
        {
            for (int j = 0; j < len; j++)
            {
                matrix[i][j] = matrix[j][i] = norm(
                        customers[i][0], customers[i][1],
                        customers[j][0], customers[j][1]);
            }
        }
        return matrix;
    }   // end createMatrix

    /** Calculate distance between two points. */
    private double norm(double x1, double y1, double x2, double y2)
    {
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }   // end norm

	@Override
	public double getObjectiveValue(Solution s) throws Exception {
		return evaluate((TspSolution)s, null)[0];
	}

}
