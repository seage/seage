/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import org.seage.metaheuristic.antcolony.AntColony;
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
        String path = "data/uf75/uf75-01.cnf";
//        String path = "data/uf20/my.cnf";
        Formula formula = FormulaReader.readFormula(path);
        double qantumPheromone = 0.01, evaporation = 0.05, defaultPheromone = 1;
        double alpha = 1, beta = 2;
        int numAnts = 100, iterations = 500;

        SatGraph graph = new SatGraph(formula, evaporation, defaultPheromone);
        SatAntBrain brain = new SatAntBrain(alpha, beta, formula);

        SatAntCreator antCreator = new SatAntCreator(graph, brain, numAnts, qantumPheromone);
        AntColony colony = new AntColony(antCreator, iterations);
        colony.beginExploring();
        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));
        //graph.printPheromone();
    }
}
