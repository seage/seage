/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.hillclimber;

import org.seage.problem.sat.Clause;
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
//        FormulaReader reader = new FormulaReader();
//        Clause[] clauses = reader.readClauses("data/uf20/uf20-01.cnf");
//        System.out.println(""+clauses[0].getLiterals().length);
        SatRandomSolution sol = new SatRandomSolution("data/uf20/uf20-01.cnf");
    }
}
