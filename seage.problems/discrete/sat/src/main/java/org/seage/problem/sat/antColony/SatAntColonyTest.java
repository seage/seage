/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antColony;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path = "data/uf20/my.cnf";
        Formula formula = FormulaReader.readFormula(path);
        SatGraph g = new SatGraph(formula, 0.05, 0.001);
        g.printPheromone();
        System.out.println(" "+g.getEdgeList().size());
    }
}
