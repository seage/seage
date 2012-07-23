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
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.sat.grasp;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatMove implements IMove {

    private int _literalIx;

    public SatMove(int literalIx) {
        literalIx = _literalIx;
    }

    public int getLiteralIx() {
        return _literalIx;
    }
 
    public Solution apply(Solution s) {
        // TODO: A - tady se musi 's' klonovat - (hluboka kopie)
        SatSolution newSol = (SatSolution)s;
        boolean[] litValues = newSol._litValues.clone();
        if(litValues[_literalIx]){
            litValues[_literalIx] = false;
        } else {
            litValues[_literalIx] = true;
        }
        newSol = new SatSolution();
        newSol.setLiteralValues(litValues);
        return (Solution)newSol;
    }
}
