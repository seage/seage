package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormulaEvaluator2Test {
  FormulaEvaluator formulaEvaluator;

  @BeforeEach
  void setup() {
    ArrayList<Clause> clauses = new ArrayList<Clause>();
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, false), new Literal(3, false)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, false), new Literal(2, true), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, false), new Literal(2, false), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, false), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, true), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, true), new Literal(3, false)}));

    // ( !a | b | c ) & ( a | !b | !c ) & ( a | b | !c ) & ( !a | b | !c ) & ( !a | !b | !c ) & (!a | !b | c )
    Formula f = new Formula(null, clauses);
    formulaEvaluator = new FormulaEvaluator(f);
  }

  @Test
  void testSingleImpact() {  
    assertEquals(2, formulaEvaluator.getSingleImpact(1));
    assertEquals(4, formulaEvaluator.getSingleImpact(-1));
    assertEquals(3, formulaEvaluator.getSingleImpact(2));
    assertEquals(3, formulaEvaluator.getSingleImpact(-2));
    assertEquals(2, formulaEvaluator.getSingleImpact(3));
    assertEquals(4, formulaEvaluator.getSingleImpact(-3));
  }

  @Test
  void getPairImpact() {
    assertEquals(2, formulaEvaluator.getPairImpact(-1, 2));
    assertEquals(2, formulaEvaluator.getPairImpact(-1, -2));
    assertEquals(2, formulaEvaluator.getPairImpact(-1, 3));
    assertEquals(2, formulaEvaluator.getPairImpact(-1, -3));
    assertEquals(1, formulaEvaluator.getPairImpact(1, 2));
    assertEquals(1, formulaEvaluator.getPairImpact(1, -2));
    assertEquals(0, formulaEvaluator.getPairImpact(1, 3));
    assertEquals(2, formulaEvaluator.getPairImpact(1, -3));
    assertEquals(1, formulaEvaluator.getPairImpact(-2, 3));
    assertEquals(2, formulaEvaluator.getPairImpact(-2, -3));
    assertEquals(1, formulaEvaluator.getPairImpact(2, 3));
    assertEquals(2, formulaEvaluator.getPairImpact(2, -3));
  }
}
