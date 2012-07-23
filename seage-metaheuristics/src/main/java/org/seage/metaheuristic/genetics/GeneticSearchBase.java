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
package org.seage.metaheuristic.genetics;

/**
 * @author Richard Malek (original)
 */
public abstract class GeneticSearchBase
{
	private GeneticSearchListener[] _geneticSearchListenerList = { };
	private GeneticSearchEvent _event;

	public GeneticSearchBase()
	{
            _event = new GeneticSearchEvent(this);
	}		

	public void addGeneticSearchListener(GeneticSearchListener listener)
	{
		GeneticSearchListener[] list = new GeneticSearchListener[
			_geneticSearchListenerList.length+1];

		for (int i = 0; i < list.length - 1; i++)
			list[i] = _geneticSearchListenerList[i];

		list[list.length - 1] = listener;
		_geneticSearchListenerList = list;
	}
	
	public void removeGeneticSearchListener(GeneticSearchListener listener)
	{
            int index = -1;
            int j = 0;
            while (index < 0 && j < _geneticSearchListenerList.length)
            {
                if (_geneticSearchListenerList[j] == listener)
                index = j;

                else j++;
            }   // end while: through list

                // If index is less than zero then it wasn't in the list
                if (index >= 0)
                {
                        GeneticSearchListener[] list = new GeneticSearchListener[
                                _geneticSearchListenerList.length-1];

                        for (int i = 0; i < list.length; i++)
                                list[i] = _geneticSearchListenerList[i < index ? i : i + 1];

                        _geneticSearchListenerList = list;
                }
	}
	
	protected void fireGeneticSearchStarted()
	{
		for (int i = 0; i < _geneticSearchListenerList.length; i++)
			_geneticSearchListenerList[i].geneticSearchStarted(_event);
	}
	protected void fireGeneticSearchStopped()
	{
		for (int i = 0; i < _geneticSearchListenerList.length; i++)
			_geneticSearchListenerList[i].geneticSearchStopped(_event);
	}
	protected void fireNewBestSolutionFound()
	{
		for (int i = 0; i < _geneticSearchListenerList.length; i++)
			_geneticSearchListenerList[i].newBestSolutionFound(_event);
	}
	protected void fireNoChangeInValueIterationMade()
	{
		for (int i = 0; i < _geneticSearchListenerList.length; i++)
			_geneticSearchListenerList[i].noChangeInValueIterationMade(_event);
	}

        public abstract Subject getBestSubject();
        public abstract int getCurrentIteration();
}
