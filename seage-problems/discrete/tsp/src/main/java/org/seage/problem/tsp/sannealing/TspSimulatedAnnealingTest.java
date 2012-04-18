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
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import java.io.FileInputStream;
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
        _cities = CityProvider.readCities( new FileInputStream(path) );
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
