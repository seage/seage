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
package org.seage.problem.tsp.particles;

import java.io.FileInputStream;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;

/**
 * The purpose of this class is demonstration of PSO algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmTest implements IParticleSwarmListener
{
    private City[] _cities;
    private static String _dataPath = "data/eil101.tsp";

    public static void main(String[] args)
    {
        try
        {
            if(args.length != 0)
                _dataPath = args[0];
            new TspParticleSwarmTest().run( _dataPath );
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

        ParticleSwarm pso = new ParticleSwarm( new TspObjectiveFunction(_cities) );

        pso.setMaximalIterationCount( 1500 );
        pso.setMaximalVelocity(0.9);
        pso.setMinimalVelocity(-0.9);
        pso.setAlpha(0.9);
        pso.setBeta(0.9);

        pso.addParticleSwarmOptimizationListener( this );
        pso.startSearching( new Particle[] {new TspSortedParticle(_cities.length), new TspGreedyParticle(_cities.length)} );
    }

    public void newBestSolutionFound(ParticleSwarmEvent e) {
       // System.out.println("Best: " + e.getParticleSwarmOptimization().getBestSolution().getObjectiveValue());
    }

    public void newIterationStarted(ParticleSwarmEvent e) {
    }

    public void particleSwarmStarted(ParticleSwarmEvent e) {
        System.out.println("Started");
    }

    public void particleSwarmStopped(ParticleSwarmEvent e) {
        System.out.println("Stopped");
    }

}
