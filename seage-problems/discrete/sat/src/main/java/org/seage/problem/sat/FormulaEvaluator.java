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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.sat;

/**
 *
 * @author rick
 */
public class FormulaEvaluator {

    public static int evaluate(Formula f, boolean[] s) {
        int numFalseClauses = 0;
        boolean clauseIsNegative = true;

        for (Clause c : f.getClauses()) {
            clauseIsNegative = true;
            for (Literal l : c.getLiterals()) {
                boolean x = s[l.getIndex()];
                if (l.isNeg()) {
                    x = !x;
                }

                if (x) {
                    clauseIsNegative = false;
                    break;
                }
            }
            if (clauseIsNegative) {
                numFalseClauses++;
            }
        }
        return numFalseClauses;
    }

    public static double evaluate(Formula f, int ix, boolean value)
    {
        int positive = 0;
        int negative = 0;

        for (Literal l : f.getLiterals().get(ix))
        {
            if(l.isNeg() == value)
                negative++;
            else
                positive++;
        }

        if(positive == 0 )
            return Double.MAX_VALUE;
        else
            return 1.0 * negative / positive;
    }
}
