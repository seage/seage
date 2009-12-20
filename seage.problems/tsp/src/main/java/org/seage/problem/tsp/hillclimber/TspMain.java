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
import org.seage.metaheuristic.hillclimber.Solution;
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
            new TspMain().run(args[0]);
        }
        catch(Exception ex)
        {
            System.out.println( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        _cities = CityProvider.readCities( path );
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _cities.length);

        HillClimber hc = new HillClimber( new TspObjectiveFunction(_cities) , new TspMoveManager() );
        hc.setIterationCount( 1000 );
        TspSolution tspSolution = new TspSolution( _cities );
        hc.startSearching( tspSolution );

        Visualizer.instance().createGraph(_cities, tspSolution.getTour(), "../tspsagraph.png", 600, 400);
    }


}
