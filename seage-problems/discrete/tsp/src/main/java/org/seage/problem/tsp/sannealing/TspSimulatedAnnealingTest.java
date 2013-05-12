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
package org.seage.problem.tsp.sannealing;

import java.io.FileInputStream;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent>
{
    private City[] _cities;
    //private static String _dataPath = "D:\\eil51.tsp";

    public static void main(String[] args)
    {
        try
        {
            new TspSimulatedAnnealingTest().run( args[0] );
        }
        catch(Exception ex)
        {
            System.out.println( ex.getMessage() );
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        _cities = CityProvider.readCities( new FileInputStream(path) );
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _cities.length);

        SimulatedAnnealing sa = new SimulatedAnnealing( new TspObjectiveFunction() , new TspMoveManager() );

        sa.setMaximalTemperature( 1500000 );
        sa.setMinimalTemperature( 0.1 );
        sa.setAnnealingCoefficient( 0.99 );
        sa.setMaximalInnerIterationCount(500000);
        sa.setMaximalAcceptedSolutionsPerOneStepCount(50000);

        sa.addSimulatedAnnealingListener( this );
        TspGreedySolution s = new TspGreedySolution(_cities);
        System.out.println(s.getObjectiveValue());
        sa.startSearching( s );

        System.out.println(sa.getBestSolution().getObjectiveValue());
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
	public void iterationPerformed(SimulatedAnnealingEvent e)
	{

	}

	@Override
	public void noChangeInValueIterationMade(SimulatedAnnealingEvent e)
	{
		
	}

	@Override
	public void newBestSolutionFound(SimulatedAnnealingEvent e)
	{
		// TODO Auto-generated method stub
		System.out.println("Best: " + e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());
	}

}
