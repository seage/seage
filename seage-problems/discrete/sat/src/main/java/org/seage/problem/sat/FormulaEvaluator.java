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
