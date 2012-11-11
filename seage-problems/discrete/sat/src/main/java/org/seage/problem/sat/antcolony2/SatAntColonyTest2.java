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
package org.seage.problem.sat.antcolony2;

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

        SatGraph2 graph = new SatGraph2(formula);

        System.out.println("formula literals: "+formula.getLiteralCount());
        System.out.println("graph nodes: " +graph.getNodes().size());
        System.out.println("graph edges: "+graph.getEdges().size());

//        for(int i = 0; i < graph.getNodeList().size(); i++){
//            System.out.println("node "+graph.getNodeList().get(i).getId());
//        }
        

//        SatAntBrain brain = new SatAntBrain(alpha, beta, formula);

//        SatAntCreator antCreator = new SatAntCreator(graph, brain, numAnts, qantumPheromone);
//        AntColony colony = new AntColony(antCreator, iterations);
//        colony.beginExploring();
//        System.out.println("Global best: "+(colony.getGlobalBest()-0.1));

    }
}
