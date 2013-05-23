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
package org.seage.metaheuristic.particles;

/**
 * @author Jan Zmatlik
 */
public interface IParticleSwarm
{
	

	void startSearching(Particle[] particles);

	void stopSearching();

	Particle getBestParticle();

	public Particle[] getParticles();

	public void setParticles(Particle[] particles);
	
	long getCurrentIteration();
	
	long getMaximalIterationCount();

	void setMaximalIterationCount(long maximalIterationCount);

	double getMaximalVelocity();

	void setMaximalVectorValue(double maximalVelocity);

	double getMinimalVelocity();

	void setMinimalVectorValue(double minimalVelocity);
}
