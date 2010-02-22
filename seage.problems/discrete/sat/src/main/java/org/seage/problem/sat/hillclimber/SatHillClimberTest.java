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
        SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
        SatSolutionGenerator solGen = new SatSolutionGenerator("greedy", formula);
        int numRestarts = 10;
        int numIterations = 10;

        HillClimber _hc = new HillClimber(objFce, new SatMoveManager(), solGen, numIterations);
        _hc.startRestartedSearching("my", numRestarts);
        SatSolution bestSol = (SatSolution)_hc.getBestSolution();
        formula.printFormula();
        System.out.println("false clauses: " + bestSol.getObjectiveValue());
    }
}
