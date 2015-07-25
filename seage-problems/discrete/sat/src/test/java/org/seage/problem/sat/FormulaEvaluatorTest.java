package org.seage.problem.sat;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.Assert;

public class FormulaEvaluatorTest {

	@Test
	public void test() 
	{
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		clauses.add( new Clause(new Literal[]{new Literal(0, false),new Literal(1, true),new Literal(2, true)}));
		clauses.add( new Clause(new Literal[]{new Literal(0, true),new Literal(1, false),new Literal(2, true)}));
		clauses.add( new Clause(new Literal[]{new Literal(0, true),new Literal(1, true),new Literal(2, false)}));
		
		Formula f = new Formula(null, clauses);
		Assert.assertEquals(0 ,FormulaEvaluator.evaluate(f, new Boolean[]{false, false, false}));
		Assert.assertEquals(0 ,FormulaEvaluator.evaluate(f, new Boolean[]{true, true, true}));
		
		Assert.assertEquals(0 ,FormulaEvaluator.evaluate(f, new Boolean[]{true, false, false}));
		Assert.assertEquals(0 ,FormulaEvaluator.evaluate(f, new Boolean[]{false, true, false}));
		Assert.assertEquals(0 ,FormulaEvaluator.evaluate(f, new Boolean[]{false, false, true}));
		
		Assert.assertEquals(1 ,FormulaEvaluator.evaluate(f, new Boolean[]{true, true, false}));
		Assert.assertEquals(1 ,FormulaEvaluator.evaluate(f, new Boolean[]{false, true, true}));
		Assert.assertEquals(1 ,FormulaEvaluator.evaluate(f, new Boolean[]{true, false, true}));
		
		//
		Assert.assertEquals(2.0 ,FormulaEvaluator.evaluate(f, 0, true));
		Assert.assertEquals(0.5 ,FormulaEvaluator.evaluate(f, 0, false));
		
		Assert.assertEquals(2.0 ,FormulaEvaluator.evaluate(f, 1, true));
		Assert.assertEquals(0.5 ,FormulaEvaluator.evaluate(f, 1, false));
		
		Assert.assertEquals(2.0 ,FormulaEvaluator.evaluate(f, 2, true));
		Assert.assertEquals(0.5 ,FormulaEvaluator.evaluate(f, 2, false));
	}

}
