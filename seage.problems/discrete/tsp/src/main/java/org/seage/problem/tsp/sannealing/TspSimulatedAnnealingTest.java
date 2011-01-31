/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspSimulatedAnnealingTest implements ISimulatedAnnealingListener
{
    private City[] _cities;
    private static String _dataPath = "D:\\eil51.tsp";

    public static void main(String[] args)
    {
        try
        {
            new TspSimulatedAnnealingTest().run( _dataPath );
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

        SimulatedAnnealing sa = new SimulatedAnnealing( new TspObjectiveFunction() , new TspMoveManager() );

        sa.setMaximalTemperature( 200 );
        sa.setMinimalTemperature( 0.1 );
        sa.setAnnealingCoefficient( 0.99 );
        sa.setMaximalIterationCount(1500);
        sa.setMaximalSuccessIterationCount(100);

        sa.addSimulatedAnnealingListener( this );
        sa.startSearching( (Solution) new TspGreedySolution( _cities ) );

        System.out.println(sa.getBestSolution());
    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        System.out.println("Started");
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
        System.out.println("Stopped");
    }

    public void newBestSolutionFound(SimulatedAnnealingEvent e) {
        System.out.println("Best: " + e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());
    }

    public void newCurrentSolutionFound(SimulatedAnnealingEvent e) {
    }

    public void newIterationStarted(SimulatedAnnealingEvent e) {
    }

}
