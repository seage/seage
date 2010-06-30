/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony2;

import org.seage.problem.sat.antcolony.*;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
//        String path = "data/uf75/uf75-01.cnf";
        String path = "data/uf20/my.cnf";
        Formula formula = FormulaReader.readFormula(path);
        double qantumPheromone = 0.01, evaporation = 0.05, defaultPheromone = 1;
//        double alpha = 1, beta = 2;
//        int numAnts = 100, iterations = 500;

        SatGraph2 graph = new SatGraph2(formula, evaporation, defaultPheromone);

        System.out.println("formula literals: "+formula.getLiteralCount());
        System.out.println("graph nodes: " +graph.getNodeList().size());
        System.out.println("graph edges: "+graph.getEdgeList().size());

//        for(int i = 0; i < graph.getNodeList().size(); i++){
//            System.out.println("node "+graph.getNodeList().get(i).getId());
//        }
        

//        SatAntBrain brain = new SatAntBrain(alpha, beta, formula);

//        SatAntCreator antCreator = new SatAntCreator(graph, brain, numAnts, qantumPheromone);
//        AntColony colony = new AntColony(antCreator, iterations);
//        colony.beginExploring();
//        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));
        graph.printPheromone();
    }
}
