/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.hillclimber;

import org.seage.metaheuristic.hillclimber.HillClimber;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatHillClimberTest {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path = "data/uf20/uf20-01.cnf";
        Formula formula = FormulaReader.readFormula(path);


        HillClimber _hc = new HillClimber(new SatObjectiveFunction(formula), new SatMoveManager(), new SatSolutionGenerator("greedy", formula), 10);
        _hc.startRestartedSearching("my", 100);
        SatSolution bestSol = (SatSolution)_hc.getBestSolution();
        formula.printFormula();
        System.out.println("false clauses: " + bestSol.getObjectiveValue());

//        SatSolution sol = new SatGreedySolution(formula);
//        SatMoveManager mm = new SatMoveManager();
//        mm.getAllMoves(sol);
//        mm.printMoves();
//        sol.printLiterals();
//
//        formula.substituteLiteralsInFormula(sol);
//        formula.printReadedFormula();
//        formula.printSubstitutedFormula();
//        System.out.println(""+formula.numberFalseClausesAfterSubstitute());
//        System.out.println(""+formula.numberFalseClausesInReadedFormula());
    }
}
