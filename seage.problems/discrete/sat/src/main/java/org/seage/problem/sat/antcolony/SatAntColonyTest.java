/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.sat.antcolony;

import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path = "data/uf75/uf75-050.cnf";// args[0];
        Formula formula = FormulaReader.readFormula(path);

        double quantumPheromone = 10, evaporation = 0.95, defaultPheromone = 0.1;
        double alpha = 1, beta = 5;
        int numAnts = 100, iterations = 5000;

        SatGraph graph = new SatGraph(formula, evaporation, defaultPheromone);
        SatAntBrain brain = new SatAntBrain(formula);
        
        AntColony colony = new AntColony(brain, graph);
        colony.setParameters(numAnts, iterations, alpha, beta, quantumPheromone);
        colony.beginExploring(graph.getNodeList().get(0));

        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));
        
        boolean[] s = new boolean[colony.getBestPath().size()];
        for(int i=0;i<s.length;i++)
        {
            s[i] = colony.getBestPath().get(i).getNode1().getId() > 0;
            int s2 = 0;
            if(s[i]) s2 =1;
            System.out.print(s2);
        }
        System.out.println();
        System.out.println("Global best: "+ FormulaEvaluator.evaluate(formula, s));
        //graph.printPheromone();
    }
}
