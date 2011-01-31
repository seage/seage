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
 *     Karel Durkota
 */
package org.seage.problem.qap.fireflies;

import org.seage.metaheuristic.fireflies.*;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunction implements ObjectiveFunction
{

    public double[][][] _matrix;

    public QapObjectiveFunction(Double[][][] facilityLocation)
    {
        int numFacilities = facilityLocation[0][0].length;
        double[][][] customers = new double[3][numFacilities][numFacilities];

        for(int n=0;n<3;n++)
            for (int i = 0; i < numFacilities; i++)
            {
                for(int j=0;j<numFacilities;j++){
                    customers[n][i][j] = facilityLocation[n][i][j];
                }
            }

        _matrix = customers;//createMatrix(customers);
    }   // end constructor

    @Override
    public double[] evaluate(Solution solution, Move move) throws Exception
    {
        try
        {
            Integer[] assign = ((QapSolution) solution)._assign;
            int len = assign.length;

            // If move is null, calculate distance from scratch
//            if (move == null)
//            {
                double price = 0;
                for(int i=0;i<len;i++){
                    for(int j=0;j<len;j++){
                        double a = _matrix[0][i][j];
                        price+=_matrix[0][i][j]*_matrix[1][assign[i]][assign[j]];
                    }
                }
                double addition=0;
                for(int i=0;i<_matrix[0][0].length;i++){
                    addition+=_matrix[2][i][assign[i]];
                }

                return new double[]{ price+addition };
//            } // end if: move == null
            // Else calculate incrementally
//            else
//            {
//                QapSwapMove mv = (QapSwapMove) move;
//                int pos1 = -1;
//                int pos2 = -1;
//
//                // Find positions
//                for (int i = 0; i < assign.length; i++)
//                {
//                    if (assign[i] == mv.customer)
//                    {
//                        pos1 = i;
//                        break;
//                    }
//                }
//                pos2 = pos1 + mv.movement;
//
//
//                // Prior objective value
//                double price = solution.getObjectiveValue()[0];
//
//                // Treat a pair swap move differently
//                // COUNT DELTA according to http://iridia0.ulb.ac.be/~stuetzle/publications/AIDA-99-03.pdf page 5
//                // b = _matrix[0][i][j]
//                // a = _matrix[1][i][j]
//                double delta = 0;
//                delta += _matrix[0][pos1][pos1]*(_matrix[1][assign[pos2]][assign[pos2]]-_matrix[1][assign[pos1]][assign[pos1]]);
//                delta += _matrix[0][pos1][pos2]*(_matrix[1][assign[pos2]][assign[pos1]]-_matrix[1][assign[pos1]][assign[pos2]]);
//                delta += _matrix[0][pos2][pos1]*(_matrix[1][assign[pos1]][assign[pos2]]-_matrix[1][assign[pos2]][assign[pos1]]);
//                delta += _matrix[0][pos2][pos2]*(_matrix[1][assign[pos1]][assign[pos1]]-_matrix[1][assign[pos2]][assign[pos2]]);
//                double temp=0;
//                for(int i=0;i<_matrix[0][0].length;i++){
//                    if(i==pos1 || i==pos2)
//                        continue;
//                temp += _matrix[0][i][pos1]*(_matrix[1][assign[i]][assign[pos2]]-_matrix[1][assign[i]][assign[pos1]]);
//                temp += _matrix[0][i][pos2]*(_matrix[1][assign[i]][assign[pos1]]-_matrix[1][assign[i]][assign[pos2]]);
//                temp += _matrix[0][pos1][i]*(_matrix[1][assign[pos2]][assign[i]]-_matrix[1][assign[pos1]][assign[i]]);
//                temp += _matrix[0][pos2][i]*(_matrix[1][assign[pos1]][assign[i]]-_matrix[1][assign[pos2]][assign[i]]);
//                }
//                delta += temp;
//
//
//
//                return new double[] { price+delta };
//            }   // end else: calculate incremental
        } catch (Exception ex)
        {
            throw ex;
        }
    }   // end evaluate

    public void ObjectiveFunction(Solution soln) {
        throw new UnsupportedOperationException("Not supported yet. - ObjectiveFunction in QapObjectiveFuntion");
    }

    public double[] evaluate(Solution soln) throws Exception {
        return evaluate(soln,null);
    }

}   // end class MyObjectiveFunction

