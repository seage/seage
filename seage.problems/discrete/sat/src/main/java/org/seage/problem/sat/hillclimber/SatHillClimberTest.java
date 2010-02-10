/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.hillclimber;

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
        FormulaReader reader = new FormulaReader();
        int countLiterals = reader.loadNumberLiterals("data/uf20/uf20-01.cnf");
        SatRandomSolution sol = new SatRandomSolution(countLiterals);
    }
}
