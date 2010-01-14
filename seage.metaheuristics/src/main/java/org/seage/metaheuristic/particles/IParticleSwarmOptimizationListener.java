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
package org.seage.metaheuristic.particles;

/**
 *
 * @author Jan Zmatlik
 */
public interface IParticleSwarmOptimizationListener {

    public void particleSwarmOptimizationStarted( ParticleSwarmOptimizationEvent e );

    public void particleSwarmOptimizationStopped( ParticleSwarmOptimizationEvent e );

    public void newBestSolutionFound( ParticleSwarmOptimizationEvent e );

    public void newIterationStarted( ParticleSwarmOptimizationEvent e );

}
