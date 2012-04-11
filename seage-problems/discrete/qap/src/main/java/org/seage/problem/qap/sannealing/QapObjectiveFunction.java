/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.qap.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunction implements IObjectiveFunction
{
    private QapSolution _currrentQapSolution;
    
    public void setObjectiveValue(Solution solution)
    {
        _currrentQapSolution = (QapSolution)solution;

        Integer[] assign = ((QapSolution) solution)._assign;
        int len = assign.length;
        Double[][][] _matrix = ((QapSolution)solution)._facilityLocation;

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
        solution.setObjectiveValue( price+addition );
    }
}
