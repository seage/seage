/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.sat.antcolony;

import org.seage.metaheuristic.antcolony.Ant;
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

        SatGraph graph = new SatGraph(formula);
        SatAntBrain brain = new SatAntBrain(formula);       
        AntColony colony = new AntColony(brain, graph);
        colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, evaporation);
        
        Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant(brain, graph);
		
        colony.startExploring(graph.getNodeList().get(0), ants);

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
