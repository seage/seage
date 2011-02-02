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
package org.seage.problem.qap.sannealing;

import org.seage.problem.qap.FacilityLocationProvider;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealingListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Karel Durkota
 */
public class QapSimulatedAnnealingTest implements ISimulatedAnnealingListener
{
    private Double[][][] _facilityLocation;
    private static String _dataPath = "data/tai12a.dat";

    public static void main(String[] args)
    {
        try
        {
            new QapSimulatedAnnealingTest().run( _dataPath );
        }
        catch(Exception ex)
        {
            System.out.println( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        _facilityLocation = FacilityLocationProvider.readFacilityLocations( path );
        System.out.println("Loading Facilities & Locations from path: " + path);
        System.out.println("Number of facilities and locations: " + _facilityLocation.length);

        SimulatedAnnealing sa = new SimulatedAnnealing( new QapObjectiveFunction() , new QapMoveManager() );

        sa.setMaximalTemperature( 200 );
        sa.setMinimalTemperature( 0.1 );
        sa.setAnnealingCoefficient( 0.99 );
        sa.setMaximalIterationCount(1500);
        sa.setMaximalSuccessIterationCount(100);

        sa.addSimulatedAnnealingListener( this );
        sa.startSearching( (Solution) new QapGreedySolution( _facilityLocation ) );

        System.out.println(((QapSolution)sa.getBestSolution()).toString());
        for(int i=0;i<((QapSolution)sa.getBestSolution())._assign.length;i++){
            System.out.print(((QapSolution)sa.getBestSolution())._assign[i]+", ");
        }
        System.out.println("\nEVAL: "+((QapSolution)sa.getBestSolution()).getObjectiveValue());
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
