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
package org.seage.problem.qap.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunction implements IObjectiveFunction
{
    public double getObjectiveValue(Solution solution)
    {
        Integer[] assign = ((QapSolution) solution)._assign;
        int len = assign.length;
        Double[][][] _matrix = QapSolution._facilityLocation;

        double price = 0;
        for(int i=0;i<len;i++){
            for(int j=0;j<len;j++){
                price+=_matrix[0][i][j]*_matrix[1][assign[i]][assign[j]];
            }
        }
        double addition=0;
        for(int i=0;i<_matrix[0][0].length;i++){
            addition+=_matrix[2][i][assign[i]];
        }
        return  price+addition ;
    }
}
