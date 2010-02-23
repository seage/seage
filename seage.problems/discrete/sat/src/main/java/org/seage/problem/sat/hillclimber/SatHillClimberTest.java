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
        String path = "data/uf75/uf75-01.cnf";
        Formula formula = FormulaReader.readFormula(path);
        System.out.println(" Formula:");
        formula.printFormula();
        SatObjectiveFunction objFce = new SatObjectiveFunction(formula);
        SatSolutionGenerator solGen = new SatSolutionGenerator("greedy", formula);
        int numRestarts = 100;
        int numIterations = 10;

        HillClimber _hc = new HillClimber(objFce, new SatMoveManager(), solGen, numIterations);
        _hc.startRestartedSearching("my", numRestarts);
        SatSolution bestSol = (SatSolution)_hc.getBestSolution();
        SatSolution satHC = (SatSolution)_hc.getBestSolution();
        System.out.println(" Solution:");
        satHC.printLiterals();
        System.out.println(" false clauses: " + bestSol.getObjectiveValue());
    }
}
