/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
}
