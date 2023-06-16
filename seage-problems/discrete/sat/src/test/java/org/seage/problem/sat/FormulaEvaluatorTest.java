package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class FormulaEvaluatorTest {

  @Test
  void test() {
    ArrayList<Clause> clauses = new ArrayList<Clause>();
    clauses.add(new Clause(
        new Literal[] {new Literal(1, false), new Literal(2, true), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, false), new Literal(3, true)}));
    clauses.add(new Clause(
        new Literal[] {new Literal(1, true), new Literal(2, true), new Literal(3, false)}));

    Formula f = new Formula(null, clauses); // (a | !b | !c) & (!a | b | !c) & (!a | !b | c)
    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {false, false, false}));
    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {true, true, true}));

    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {true, false, false}));
    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {false, true, false}));
    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {false, false, true}));

    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true, true, false}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {false, true, true}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true, false, true}));


    assertEquals(3, FormulaEvaluator.evaluate(f, new Boolean[] {null, null, null}));
    assertEquals(2, FormulaEvaluator.evaluate(f, new Boolean[] {true, null, null}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {false, null, null}));    
    assertEquals(2, FormulaEvaluator.evaluate(f, new Boolean[] {null, true, null}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {null, false, null}));
    assertEquals(2, FormulaEvaluator.evaluate(f, new Boolean[] {null, null, true}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {null, null, false}));

    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {false, false, null}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {false, true, null}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true, false, null}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true, true, null}));

    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {false, null, false}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {false, null, true }));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true,  null, false}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {true,  null, true }));

    assertEquals(0, FormulaEvaluator.evaluate(f, new Boolean[] {null, false, false}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {null, false, true }));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {null, true,  false}));
    assertEquals(1, FormulaEvaluator.evaluate(f, new Boolean[] {null, true, true }));

    //
    FormulaEvaluator formulaEvaluator = new FormulaEvaluator(f);
    // assertEquals(2.0, formulaEvaluator.evaluate(f, -1));
    // assertEquals(0.5, formulaEvaluator.evaluate(f, 1));
    //
    // assertEquals(2.0, formulaEvaluator.evaluate(f, -2));
    // assertEquals(0.5, formulaEvaluator.evaluate(f, 2));
    //
    // assertEquals(2.0, formulaEvaluator.evaluate(f, -3));
    // assertEquals(0.5, formulaEvaluator.evaluate(f, 3));

    printObjectiveValues(formulaEvaluator, f, new Boolean[] {false, false, false});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {false, false, true});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {false, true, false});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {false, true, true});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {true, false, false});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {true, false, true});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {true, true, false});
    printObjectiveValues(formulaEvaluator, f, new Boolean[] {true, true, true});
  }

  private void printObjectiveValues(FormulaEvaluator formulaEvaluator, Formula f,
      Boolean[] booleans) {
    // double val = 0.0;
    // for (int i = 0; i < booleans.length; i++)
    // val += formulaEvaluator.evaluate(f, (i + 1) * (booleans[i] == true ? 1 : -1));
    // System.out.println(String.format("%.2f - %d", val, FormulaEvaluator.evaluate(f, booleans)));
  }

}
