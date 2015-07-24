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

import java.io.FileInputStream;

import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.Graph;
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
        String path = "data/sat/uf20-01.cnf";// args[0];
        Formula formula = new Formula(new ProblemInstanceInfo("",ProblemInstanceOrigin.FILE, path), FormulaReader.readClauses(new FileInputStream(path)));

        double quantumPheromone = 100, evaporation = 0.98, defaultPheromone = 0.1;
        double alpha = 1, beta = 3;
        int numAnts = 100, iterations = 50000;

        Graph graph = new SatGraph2(formula);
        SatAntBrain brain = new SatAntBrain(graph, formula);       
        AntColony colony = new AntColony(graph, brain);
        colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, evaporation);
        
        Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant();
		
        colony.startExploring(graph.getNodes().get(0), ants);

        System.out.println("Global best: "+colony.getGlobalBest());
        
        Boolean[] s = new Boolean[colony.getBestPath().size()];
        for(int i=0;i<s.length;i++)
        {
            s[i] = colony.getBestPath().get(i).getNode1().getID() > 0;
            int s2 = 0;
            if(s[i]) s2 =1;
            System.out.print(s2);
        }
        System.out.println();
        System.out.println("Global best: "+ FormulaEvaluator.evaluate(formula, s));
        //graph.printPheromone();
    }
}
