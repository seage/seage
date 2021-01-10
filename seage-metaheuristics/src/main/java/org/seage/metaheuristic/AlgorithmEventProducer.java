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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Richard Malek (original)
 */
public class AlgorithmEventProducer<L extends IAlgorithmListener<E>, E> {
  private List<L> _listenerList;
  private E _event;

  public AlgorithmEventProducer(E event) {
    _event = event;
    _listenerList = new ArrayList<L>();
  }

  public void addAlgorithmListener(L listener) {
    _listenerList.add(listener);
  }

  public void removeGeneticSearchListener(L listener) {
    if (_listenerList.contains(listener))
      _listenerList.remove(listener);
  }

  public void fireAlgorithmStarted() {
    for (int i = 0; i < _listenerList.size(); i++)
      _listenerList.get(i).algorithmStarted(_event);
  }

  public void fireAlgorithmStopped() {
    for (int i = 0; i < _listenerList.size(); i++)
      _listenerList.get(i).algorithmStopped(_event);
  }

  public void fireNewBestSolutionFound() {
    for (int i = 0; i < _listenerList.size(); i++)
      _listenerList.get(i).newBestSolutionFound(_event);
  }

  public void fireNoChangeInValueIterationMade() {
    for (int i = 0; i < _listenerList.size(); i++)
      _listenerList.get(i).noChangeInValueIterationMade(_event);
  }

  public void fireIterationPerformed() {
    for (int i = 0; i < _listenerList.size(); i++)
      _listenerList.get(i).iterationPerformed(_event);
  }
}
