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
package org.seage.problem.qap.particles;

import java.io.FileInputStream;
import org.seage.metaheuristic.particles.IParticleSwarmListener;
import org.seage.metaheuristic.particles.ParticleSwarm;
import org.seage.metaheuristic.particles.ParticleSwarmEvent;
import org.seage.problem.qap.FacilityLocationProvider;

/**
 * The purpose of this class is demonstration of PSO algorithm use.
 *
 * @author Karel Durkota
 */
public class QapParticleSwarmTest implements IParticleSwarmListener
{
    private Double[][][] _facilityLocation;
//    private static String _dataPath = "data/tai12a.dat";
    private static String _dataPath = "D:\\qap\\bur26a.dat";

    public static void main(String[] args)
    {
        try
        {
            new QapParticleSwarmTest().run( _dataPath );//args[0] );
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
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _facilityLocation[0][0].length);

        ParticleSwarm pso = new ParticleSwarm( new QapObjectiveFunction(_facilityLocation) );

        pso.setMaximalIterationCount( 1500 );
        pso.setMaximalVelocity(0.9);
        pso.setMinimalVelocity(-0.9);
        pso.setAlpha(0.9);
        pso.setBeta(0.9);

        pso.addParticleSwarmOptimizationListener( this );
        pso.startSearching( null );
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
