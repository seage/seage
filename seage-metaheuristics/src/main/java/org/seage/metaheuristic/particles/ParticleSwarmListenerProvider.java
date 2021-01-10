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
 *
 * @author Jan Zmatlik
 */
public class ParticleSwarmListenerProvider {

  private IParticleSwarmListener[] _particleSwarmOptimizationListenerList = {};

  private final ParticleSwarmEvent _particleSwarmOptimizationEvent;

  public ParticleSwarmListenerProvider(IParticleSwarm particleSwarmOptimization) {
    _particleSwarmOptimizationEvent = new ParticleSwarmEvent(particleSwarmOptimization);
  }

  public final void addParticleSwarmListener(IParticleSwarmListener listener) {
    IParticleSwarmListener[] list = new IParticleSwarmListener[_particleSwarmOptimizationListenerList.length + 1];

    int length = list.length;
    for (int i = 0; i < length - 1; i++)
      list[i] = _particleSwarmOptimizationListenerList[i];

    list[list.length - 1] = listener;
    _particleSwarmOptimizationListenerList = list;
  }

  protected final void fireParticleSwarmStarted() {
    int length = _particleSwarmOptimizationListenerList.length;
    for (int i = 0; i < length; i++)
      _particleSwarmOptimizationListenerList[i].particleSwarmStarted(_particleSwarmOptimizationEvent);
  }

  protected final void fireParticleSwarmStopped() {
    int length = _particleSwarmOptimizationListenerList.length;
    for (int i = 0; i < length; i++)
      _particleSwarmOptimizationListenerList[i].particleSwarmStopped(_particleSwarmOptimizationEvent);
  }

  protected final void fireNewBestSolutionFound() {
    int length = _particleSwarmOptimizationListenerList.length;
    for (int i = 0; i < length; i++)
      _particleSwarmOptimizationListenerList[i].newBestSolutionFound(_particleSwarmOptimizationEvent);
  }

  protected final void fireNewIterationStarted() {
    int length = _particleSwarmOptimizationListenerList.length;
    for (int i = 0; i < length; i++)
      _particleSwarmOptimizationListenerList[i].newIterationStarted(_particleSwarmOptimizationEvent);
  }
}
