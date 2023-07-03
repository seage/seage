package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormulaEvaluator2Test {
  FormulaEvaluator formulaEvaluator;

  private final int a = 1;
  private final int b = 2;
  private final int c = 3;
  
  @BeforeEach
  void setup() {
    // ( !a | b | c ) & ( a | !b | !c ) & ( a | b | !c ) & ( !a | b | !c ) & ( !a | !b | !c ) & (!a | !b | c )
    ArrayList<Clause> clauses = new ArrayList<Clause>();
    clauses.add(new Clause(
        new Literal[] {new Literal(a, true), new Literal(b, false), new Literal(c, false)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(a, false), new Literal(b, true), new Literal(c, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(a, false), new Literal(b, false), new Literal(c, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(a, true), new Literal(b, false), new Literal(c, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(a, true), new Literal(b, true), new Literal(c, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(a, true), new Literal(b, true), new Literal(c, false)}));

    Formula f = new Formula(null, clauses);
    formulaEvaluator = new FormulaEvaluator(f);
  }

  @Test
  void testLiteralImpact() {  
    assertEquals(2, formulaEvaluator.getLiteralImpact(a));
    assertEquals(4, formulaEvaluator.getLiteralImpact(-a));
    assertEquals(3, formulaEvaluator.getLiteralImpact(b));
    assertEquals(3, formulaEvaluator.getLiteralImpact(-b));
    assertEquals(2, formulaEvaluator.getLiteralImpact(c));
    assertEquals(4, formulaEvaluator.getLiteralImpact(-c));
  }

  @Test
  void testLiteralPairImpact() {
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(-a, b));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(-a, -b));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(-a, c));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(-a, -c));
    assertEquals(1, formulaEvaluator.getLiteralPairImpact(a, b));
    assertEquals(1, formulaEvaluator.getLiteralPairImpact(a, -b));
    assertEquals(0, formulaEvaluator.getLiteralPairImpact(a, c));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(a, -c));
    assertEquals(1, formulaEvaluator.getLiteralPairImpact(-b, c));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(-b, -c));
    assertEquals(1, formulaEvaluator.getLiteralPairImpact(b, c));
    assertEquals(2, formulaEvaluator.getLiteralPairImpact(b, -c));
  }
}
