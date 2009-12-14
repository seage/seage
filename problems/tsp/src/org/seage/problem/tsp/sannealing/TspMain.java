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

import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.tsp.data.*;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class TspMain implements ISimulatedAnnealingListener
{
    private City[] _cities;

    public static void main(String[] args)
    {
        try
        {
            new TspMain().run(args[0]);//args[0]eil101
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
        sa.setMinimalTemperature( 1E-15 );
        sa.setAnnealingCoefficient( 0.99 );
        sa.setMaximalIterationCount(1500);
        sa.setMaximalSuccessIterationCount(800);

        sa.addSimulatedAnnealingListener( this );
        sa.startSearching( (Solution) new TspSolution( _cities ) );
    }

    public void simulatedAnnealingStarted(SimulatedAnnealingEvent e) {
        System.out.println("Started");
    }

    public void simulatedAnnealingStopped(SimulatedAnnealingEvent e) {
        System.out.println("Stop");
        TspSolution tspSolution = (TspSolution) e.getSimulatedAnnealing().getBestSolution();
        Integer[] tour = tspSolution.getTour();
        System.out.println("Tour length: " + tspSolution.getObjectiveValue());
        for(int i = 0; i < tour.length; i++) System.out.print(tour[i]+" ");
        System.out.println();

        Visualizer.instance().createGraph(_cities, tour, "../tspsagraph.png", 600, 400);
    }

    public void newBestSolutionFound(SimulatedAnnealingEvent e) {
    }

    public void newCurrentSolutionFound(SimulatedAnnealingEvent e) {
    }
}
