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
package org.seage.problem.tsp.particles;

import org.seage.metaheuristic.particles.IParticleSwarmOptimizationListener;
import org.seage.metaheuristic.particles.ParticleSwarmOptimization;
import org.seage.metaheuristic.particles.ParticleSwarmOptimizationEvent;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;

/**
 * The purpose of this class is demonstration of PSO algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmOptimizationTest implements IParticleSwarmOptimizationListener
{
    private City[] _cities;
    private static String _dataPath = "data/eil101.tsp";

    public static void main(String[] args)
    {
        try
        {
            new TspParticleSwarmOptimizationTest().run( args[0] );
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

        ParticleSwarmOptimization pso = new ParticleSwarmOptimization( new TspObjectiveFunction() , new TspVelocityManager() );

        pso.setMaximalIterationCount( 1500 );
        pso.setMaximalVelocity(150.0);

        pso.addParticleSwarmOptimizationListener( this );
        pso.startSearching( null );

        System.out.println(pso.getBestSolution());
    }

    public void newBestSolutionFound(ParticleSwarmOptimizationEvent e) {
        System.out.println("Best: " + e.getParticleSwarmOptimization().getBestSolution().getObjectiveValue());
    }

    public void newIterationStarted(ParticleSwarmOptimizationEvent e) {
    }

    public void particleSwarmOptimizationStarted(ParticleSwarmOptimizationEvent e) {
        System.out.println("Started");
    }

    public void particleSwarmOptimizationStopped(ParticleSwarmOptimizationEvent e) {
        System.out.println("Stopped");
    }

}
