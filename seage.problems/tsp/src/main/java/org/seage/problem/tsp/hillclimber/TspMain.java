/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;
import org.seage.metaheuristic.hillclimber.HillClimber;
import org.seage.problem.tsp.Visualizer;

/**
 *
 * @author Martin Zaloga
 * @deprecated Replaced by TspProblemSolver
 */
public class TspMain
{
    private City[] _cities;

    public static void main(String[] args)
    {
        try
        {
            new TspMain().run(args[0], "Random");
        }
        catch(Exception ex)
        {
            System.out.println( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    public void run(String path, String switcher) throws Exception
    {
        _cities = CityProvider.readCities( path );
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _cities.length);

        HillClimber hc = new HillClimber( new TspObjectiveFunction(_cities) , new TspMoveManager() );
        hc.setIterationCount( 10000 );

        if(switcher.equals("Random") || switcher.equals("random")){
            TspRandomSolution2 tspSolution = new TspRandomSolution2( _cities);
            tspSolution.switcher = "Random";
            hc.startSearching( tspSolution );
            Visualizer.instance().createGraph(_cities, tspSolution.getTour(), "tsphcgraph.png", 600, 400);
        }

        if(switcher.equals("Greedy") || switcher.equals("greedy")){
            TspGreedySolution2 tspSolution = new TspGreedySolution2( _cities);
            tspSolution.switcher = "Greedy";
            hc.startSearching( tspSolution );
            Visualizer.instance().createGraph(_cities, tspSolution.getTour(), "tsphcgraph.png", 600, 400);
        }

        System.out.println(hc.getBestSolution().getObjectiveValue());
    }


}
