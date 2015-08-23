package org.seage.problem.sat;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.Assert;

public class FormulaEvaluatorTest
{

    @Test
    public void test()
    {
        ArrayList<Clause> clauses = new ArrayList<Clause>();
        clauses.add(new Clause(new Literal[] { new Literal(0, false), new Literal(1, true), new Literal(2, true) }));
        clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, false), new Literal(2, true) }));
        clauses.add(new Clause(new Literal[] { new Literal(0, true), new Literal(1, true), new Literal(2, false) }));

        Formula f = new Formula(null, clauses);
        Assert.assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] { false, false, false }));
        Assert.assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] { true, true, true }));

        Assert.assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] { true, false, false }));
        Assert.assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] { false, true, false }));
        Assert.assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] { false, false, true }));

        Assert.assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] { true, true, false }));
        Assert.assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] { false, true, true }));
        Assert.assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] { true, false, true }));

        //
        FormulaEvaluator formulaEvaluator = new FormulaEvaluator(f);
        // Assert.assertEquals(2.0, formulaEvaluator.evaluate(f, -1));
        // Assert.assertEquals(0.5, formulaEvaluator.evaluate(f, 1));
        //
        // Assert.assertEquals(2.0, formulaEvaluator.evaluate(f, -2));
        // Assert.assertEquals(0.5, formulaEvaluator.evaluate(f, 2));
        //
        // Assert.assertEquals(2.0, formulaEvaluator.evaluate(f, -3));
        // Assert.assertEquals(0.5, formulaEvaluator.evaluate(f, 3));

        printObjectiveValues(formulaEvaluator, f, new Boolean[] { false, false, false });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { false, false, true });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { false, true, false });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { false, true, true });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { true, false, false });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { true, false, true });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { true, true, false });
        printObjectiveValues(formulaEvaluator, f, new Boolean[] { true, true, true });
    }

    private void printObjectiveValues(FormulaEvaluator formulaEvaluator, Formula f, Boolean[] booleans)
    {
//        double val = 0.0;
//        for (int i = 0; i < booleans.length; i++)
//            val += formulaEvaluator.evaluate(f, (i + 1) * (booleans[i] == true ? 1 : -1));
//        System.out.println(String.format("%.2f - %d", val, FormulaEvaluator.evaluate(f, booleans)));
    }

}
