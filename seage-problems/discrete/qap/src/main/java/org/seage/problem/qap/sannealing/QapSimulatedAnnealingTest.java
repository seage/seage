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
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.qap.sannealing;

import java.io.FileInputStream;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.qap.FacilityLocationProvider;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Karel Durkota
 */
public class QapSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent>
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
        _facilityLocation = FacilityLocationProvider.readFacilityLocations( new FileInputStream(path)  );
        System.out.println("Loading Facilities & Locations from path: " + path);
        System.out.println("Number of facilities and locations: " + _facilityLocation.length);

        SimulatedAnnealing sa = new SimulatedAnnealing( new QapObjectiveFunction() , new QapMoveManager() );

        sa.setMaximalTemperature( 2000 );
        sa.setMinimalTemperature( 0.1 );
        sa.setAnnealingCoefficient( 0.99 );
        sa.setMaximalIterationCount(2500);
        sa.setMaximalSuccessIterationCount(100);

        sa.addSimulatedAnnealingListener( this );
        sa.startSearching( (Solution) new QapGreedySolution( _facilityLocation ) );

        System.out.println(((QapSolution)sa.getBestSolution()).toString());
        for(int i=0;i<((QapSolution)sa.getBestSolution())._assign.length;i++){
            System.out.print(((QapSolution)sa.getBestSolution())._assign[i]+", ");
        }
        System.out.println("\nEVAL: "+((QapSolution)sa.getBestSolution()).getObjectiveValue());
    }

	@Override
	public void algorithmStarted(SimulatedAnnealingEvent e)
	{
		System.out.println("Started");
	}

	@Override
	public void algorithmStopped(SimulatedAnnealingEvent e)
	{
		System.out.println("Stopped");
	}

	@Override
	public void newBestSolutionFound(SimulatedAnnealingEvent e)
	{
		System.out.println("Best: " + e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());
	}

	@Override
	public void iterationPerformed(SimulatedAnnealingEvent e)
	{
		
	}

	@Override
	public void noChangeInValueIterationMade(SimulatedAnnealingEvent e)
	{
		
	}

}
