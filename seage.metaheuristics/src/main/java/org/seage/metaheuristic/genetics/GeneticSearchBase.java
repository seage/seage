/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
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
