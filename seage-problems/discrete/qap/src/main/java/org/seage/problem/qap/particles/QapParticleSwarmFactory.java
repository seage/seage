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
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.qap.particles;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.particles.ParticleSwarmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemInstance;

/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("ParticleSwarm")
@Annotations.AlgorithmName("Particle Swarm")
public class QapParticleSwarmFactory implements IAlgorithmFactory<QapPhenotype, QapParticle>
{
    //private Double[][][] _facilityLocation;

    private int _numParticles;

    private QapObjectiveFunction _objectiveFunction;

    @Override
    public Class<?> getAlgorithmClass()
    {
        return ParticleSwarmAdapter.class;
    }

    // @Override
    // public IAlgorithmAdapter<QapParticle> createAlgorithm(ProblemInstance instance) throws Exception
    // {
    //     final Double[][][] facilityLocation = ((QapProblemInstance) instance).getFacilityLocation();

    //     _objectiveFunction = new QapObjectiveFunction(facilityLocation);

    //     IAlgorithmAdapter<QapParticle> algorithm = new ParticleSwarmAdapter<> (
    //             generateInitialSolutions(facilityLocation.length),
    //             _objectiveFunction,
    //             false, "")
    //     {
    //         @Override
    //         public void solutionsFromPhenotype(Object[][] source) throws Exception
    //         {
    //             _numParticles = source.length;
    //             _initialParticles = generateInitialSolutions(facilityLocation.length);
    //             for (int i = 0; i < source.length; i++)
    //             {
    //                 Integer[] tour = ((QapParticle) _initialParticles[i]).getAssign();
    //                 for (int j = 0; j < source[i].length; j++)
    //                 {
    //                     tour[j] = (Integer) source[i][j];
    //                 }
    //             }
    //         }

    //         @Override
    //         public Object[][] solutionsToPhenotype() throws Exception
    //         {
    //             int numOfParticles = _particleSwarm.getParticles().length;
    //             Object[][] source = new Object[numOfParticles][facilityLocation.length];

    //             for (int i = 0; i < source.length; i++)
    //             {
    //                 source[i] = new Integer[facilityLocation.length];
    //                 Integer[] tour = ((QapParticle) _particleSwarm.getParticles()[i]).getAssign();
    //                 for (int j = 0; j < source[i].length; j++)
    //                 {
    //                     source[i][j] = tour[j];
    //                 }
    //             }

    //             // TODO: A - need to sort source by fitness function of each tour

    //             return source;
    //         }

	// 		@Override
	// 		public Object[] solutionToPhenotype(QapParticle solution) throws Exception {
	// 			// TODO Auto-generated method stub
	// 			return null;
	// 		}
    //     };

    //     return algorithm;
    // }

    private Particle[] generateInitialSolutions(int length) throws Exception
    {
        Particle[] particles = generateQapRandomParticles(_numParticles, length);

        for (Particle particle : particles)
        {
            // Initial coords
            for (int i = 0; i < length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for (int i = 0; i < length; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            _objectiveFunction.setObjectiveValue(particle);
        }

        return particles;
    }

    //    void printArray(Integer[] array)
    //    {
    //        for(int i = 0; i< array.length; i++)
    //            System.out.print(" " + array[i]);
    //    }

    private Particle[] generateQapRandomParticles(int count, int length)
    {
        QapRandomParticle[] particles = new QapRandomParticle[count];
        for (int i = 0; i < count; i++)
            particles[i] = new QapRandomParticle(length);

        return particles;
    }

    @Override
    public IAlgorithmAdapter<QapPhenotype, QapParticle> createAlgorithm(ProblemInstance instance,
            IPhenotypeEvaluator<QapPhenotype> phenotypeEvaluator) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
