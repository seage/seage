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
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.AntColonyEvent;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;

/**
 *
 * @author Zagy
 */
public class SatAntColonyTest implements IAlgorithmListener<AntColonyEvent>
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception 
    {    	
    	try
		{
    		//String path = "data/sat/uf20-01.cnf";
    		String path = "data/sat/uf100-01.cnf";
                      
    		long start = System.currentTimeMillis();
			new SatAntColonyTest().run(path);
			long end = System.currentTimeMillis();
			
			System.out.println((end - start) + " ms");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }
    public void run(String path) throws Exception
    {
        // args[0];
        Formula formula = new Formula(new ProblemInstanceInfo("",ProblemInstanceOrigin.FILE, path), FormulaReader.readClauses(new FileInputStream(path)));

        double quantumPheromone = 50, evaporation = 0.98, defaultPheromone = 0.1;
        double alpha = 1, beta = 2;
        int numAnts = 100, iterations = 15000;

        Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
        SatAntBrain brain = new SatAntBrain(graph, formula);       
        AntColony colony = new AntColony(graph, brain);
        colony.addAntColonyListener(this);
        colony.setParameters( iterations, alpha, beta, quantumPheromone, defaultPheromone, evaporation);
        
        Ant ants[] = new Ant[numAnts];
		for (int i = 0; i < numAnts; i++) 
			ants[i] = new Ant(null);
		
        colony.startExploring(graph.getNodes().get(0), ants);

        System.out.println("Global best: "+colony.getGlobalBest());
        
//        Boolean[] s = new Boolean[colony.getBestPath()];
//        for(int i=0;i<s.length;i++)
//        {
//        	if(colony.getBestPath().get(i).getNode1().getID()==0)
//        		continue;
//            s[i] = colony.getBestPath().get(i).getNode1().getID() > 0;
//            int s2 = 0;
//            if(s[i]) s2 =1;
//            System.out.print(s2);
//        }
//        System.out.println();
//        System.out.println("Global best: "+ FormulaEvaluator.evaluate(formula, s));
        //graph.printPheromone();
    }

	@Override
	public void algorithmStarted(AntColonyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void algorithmStopped(AntColonyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newBestSolutionFound(AntColonyEvent e) {
		System.out.println(String.format("%d - %f - %d/%d",
				e.getAntColony().getCurrentIteration(),
				e.getAntColony().getGlobalBest(),
				e.getAntColony().getGraph().getEdges().size(),
				e.getAntColony().getGraph().getNodes().size()*e.getAntColony().getGraph().getNodes().size()/2));
		
	}

	@Override
	public void iterationPerformed(AntColonyEvent e) {
		//System.out.println(e.getAntColony().getCurrentIteration());
		
	}

	@Override
	public void noChangeInValueIterationMade(AntColonyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
