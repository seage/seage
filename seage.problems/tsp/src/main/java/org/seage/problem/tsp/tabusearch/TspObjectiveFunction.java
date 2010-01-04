/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import org.seage.metaheuristic.tabusearch.*;
import org.seage.problem.tsp.City;

/**
 *
 * @author Richard Malek
 */
public class TspObjectiveFunction implements ObjectiveFunction
{

    public double[][] _matrix;

    public TspObjectiveFunction(City[] cities)
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

    @Override
    public double[] evaluate(Solution solution, Move move) throws Exception
    {
        try
        {
            int[] tour = ((TspSolution) solution)._tour;
            int len = tour.length;

            // If move is null, calculate distance from scratch
            if (move == null)
            {
                double dist = 0;
                for (int i = 0; i < len; i++)
                {
                    dist += _matrix[tour[i]][i + 1 >= len ? tour[0] : tour[i + 1]];
                }

                return new double[]{ dist };
            } // end if: move == null
            // Else calculate incrementally
            else
            {
                TspSwapMove mv = (TspSwapMove) move;
                int pos1 = -1;
                int pos2 = -1;

                // Find positions
                for (int i = 0; i < tour.length; i++)
                {
                    if (tour[i] == mv.customer)
                    {
                        pos1 = i;
                        break;
                    }
                }
                pos2 = pos1 + mv.movement;

                // Logic below requires pos1 < pos2
                if (pos1 > pos2)
                {
                    int temp = pos2;
                    pos2 = pos1;
                    pos1 = temp;
                }   // end if

                // Prior objective value
                double dist = solution.getObjectiveValue()[0];

                // Treat a pair swap move differently
                if (pos1 + 1 == pos2)
                {
                    //     | |
                    // A-B-C-D-E: swap C and D, say (works for symmetric matrix only)
                    dist -= _matrix[tour[pos1 - 1]][tour[pos1]];           // -BC
                    dist -= _matrix[tour[pos2]][tour[(pos2 + 1) % len]];   // -DE
                    dist += _matrix[tour[pos1 - 1]][tour[pos2]];           // +BD
                    dist += _matrix[tour[pos1]][tour[(pos2 + 1) % len]];   // +CE
                    return new double[] { dist };
                } // end if: pair swap
                // Else the swap is separated by at least one customer
                else
                {
                    //   |     |
                    // A-B-C-D-E-F: swap B and E, say
                    dist -= _matrix[tour[pos1 - 1]][tour[pos1]];           // -AB
                    dist -= _matrix[tour[pos1]][tour[pos1 + 1]];         // -BC
                    dist -= _matrix[tour[pos2 - 1]][tour[pos2]];           // -DE
                    dist -= _matrix[tour[pos2]][tour[(pos2 + 1) % len]];   // -EF

                    dist += _matrix[tour[pos1 - 1]][tour[pos2]];           // +AE
                    dist += _matrix[tour[pos2]][tour[pos1 + 1]];         // +EC
                    dist += _matrix[tour[pos2 - 1]][tour[pos1]];           // +DB
                    dist += _matrix[tour[pos1]][tour[(pos2 + 1) % len]];   // +BF
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
            for (int j = i + 1; j < len; j++)
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
}   // end class MyObjectiveFunction

