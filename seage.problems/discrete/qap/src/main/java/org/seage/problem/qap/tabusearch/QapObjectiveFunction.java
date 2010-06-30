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
package org.seage.problem.qap.tabusearch;

import org.seage.metaheuristic.tabusearch.*;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunction implements ObjectiveFunction
{

    public double[][] _matrix;

    public QapObjectiveFunction(Double[][] facilityLocation)
    {
        int numFacilities = facilityLocation.length;
        double[][] customers = new double[numFacilities][numFacilities];

        for (int i = 0; i < numFacilities; i++)
        {
            for(int j=0;j<numFacilities;j++){
                customers[i][j] = facilityLocation[i][j];
            }
        }

        _matrix = customers;//createMatrix(customers);
    }   // end constructor

    @Override
    public double[] evaluate(Solution solution, Move move) throws Exception
    {
        try
        {
            int[] assign = ((QapSolution) solution)._assign;
            int len = assign.length;

            // If move is null, calculate distance from scratch
            if (move == null)
            {
                double price = 0;
                for (int i = 0; i < len; i++)
                {
                    //dist += _matrix[assign[i]][i + 1 >= len ? assign[0] : assign[i + 1]];
                    price += _matrix[i][assign[i]];
                }

                return new double[]{ price };
            } // end if: move == null
            // Else calculate incrementally
            else
            {
                QapSwapMove mv = (QapSwapMove) move;
                int pos1 = -1;
                int pos2 = -1;

                // Find positions
                for (int i = 0; i < assign.length; i++)
                {
                    if (assign[i] == mv.customer)
                    {
                        pos1 = i;
                        break;
                    }
                }
                pos2 = pos1 + mv.movement;

                // Prior objective value
                double price = solution.getObjectiveValue()[0];

                // Treat a pair swap move differently
                price -= _matrix[pos1][assign[pos1]];
                price -= _matrix[pos2][assign[pos2]];
                price += _matrix[pos1][assign[pos2]];
                price += _matrix[pos2][assign[pos1]];
                return new double[] { price };
            }   // end else: calculate incremental
        } catch (Exception ex)
        {
            throw ex;
        }
    }   // end evaluate
}   // end class MyObjectiveFunction

