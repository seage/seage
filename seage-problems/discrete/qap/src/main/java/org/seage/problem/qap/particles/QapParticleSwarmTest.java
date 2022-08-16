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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
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
            new QapParticleSwarmTest().run(_dataPath);//args[0] );
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            log.error(ex);
        }
    }

    public void run(String path) throws Exception
    {
        _facilityLocation = FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _facilityLocation[0][0].length);

        ParticleSwarm pso = new ParticleSwarm(new QapObjectiveFunction(_facilityLocation));

        pso.setMaximalIterationCount(1500);
        pso.setMaximalVectorValue(0.9);
        pso.setMinimalVectorValue(-0.9);
        pso.setAlpha(0.9);
        pso.setBeta(0.9);

        pso.addParticleSwarmOptimizationListener(this);
        pso.startSearching(null);
    }

    @Override
    public void newBestSolutionFound(ParticleSwarmEvent e)
    {
        // System.out.println("Best: " + e.getParticleSwarmOptimization().getBestSolution().getObjectiveValue());
    }

    @Override
    public void newIterationStarted(ParticleSwarmEvent e)
    {
    }

    @Override
    public void particleSwarmStarted(ParticleSwarmEvent e)
    {
        System.out.println("Started");
    }

    @Override
    public void particleSwarmStopped(ParticleSwarmEvent e)
    {
        System.out.println("Stopped");
    }

}
