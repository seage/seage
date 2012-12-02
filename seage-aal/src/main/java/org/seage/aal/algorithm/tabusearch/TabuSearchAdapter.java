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
package org.seage.aal.algorithm.tabusearch;

import java.util.*;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.*;

/**
 * TabuSearchAdapter interface.
 */
@AlgorithmParameters({ 
	@Parameter(name = "numIterDivers", min = 1, max = 1, init = 1), 
	@Parameter(name = "numIteration", min = 1, max = 1000000, init = 1000),
	@Parameter(name = "numSolutions", min = 1, max = 1, init = 1), 
	@Parameter(name = "tabuListLength", min = 1, max = 1000, init = 30) })
public abstract class TabuSearchAdapter extends AlgorithmAdapterImpl
{

	private TabuSearch _tabuSearch;
	private ObjectiveFunction _objectiveFunction;
	private TabuSearchObserver _observer;
	private int _iterationToGo;
	private int _tabuListLength;
	private boolean _maximizing;
	protected Solution[] _solutions;
	protected int _solutionsToExplore;
	protected Solution _bestEverSolution; // best of all solution
	private SolutionComparator _solutionComparator;
	private AlgorithmParams _params;

	private double _statInitObjVal;
	private double _statEndObjVal;
	private int _statNumIter;
	private int _statNumNewSol;
	private int _statLastIterNewSol;
	private String _searchID;
	private AlgorithmReporter _reporter;

	public TabuSearchAdapter(MoveManager moveManager, ObjectiveFunction objectiveFunction, String searchID)
	{
		_observer = new TabuSearchObserver();
		_tabuSearch = new TabuSearch(moveManager, objectiveFunction, false);
		_tabuSearch.addTabuSearchListener(_observer);
		_objectiveFunction = objectiveFunction;

		_tabuSearch.setAspirationCriteria(new BestEverAspirationCriteria());
		_iterationToGo = 0;
		_tabuListLength = 0;
		_solutionComparator = new SolutionComparator(false);
		_searchID = searchID;
	}

	@Override
	public void startSearching() throws Exception
	{
		_reporter = new AlgorithmReporter(_searchID);
		_reporter.putParameters(_params);

		boolean[] mask = new boolean[_solutions.length];

		if (_solutionsToExplore >= _solutions.length)
		{
			_solutionsToExplore = _solutions.length;
			for (int i = 0; i < _solutionsToExplore; i++)
			{
				mask[i] = true;
			}
		}
		else
		{
			// Random rnd = new Random();
			mask[0] = true;
			for (int i = 1; i < _solutionsToExplore; i++)
			{
				mask[i] = true;
			}
		}

		_statNumNewSol = 0;
		Solution currentSolution = null;
		setBestEverSolution(); // z nactenych reseni, ulozi nejlepsi

		for (int i = 0; i < _solutions.length; i++)
		{
			if (mask[i] == false)
			{
				continue;
			}

			currentSolution = _solutions[i];
			_tabuSearch.setBestSolution(currentSolution);
			_tabuSearch.setCurrentSolution(currentSolution);

			_tabuSearch.setTabuList(new SimpleTabuList(_tabuListLength));

			_tabuSearch.setIterationsToGo(_iterationToGo);

			_tabuSearch.startSolving();

			_solutions[i] = _tabuSearch.getBestSolution();

		}
		Arrays.sort(_solutions, _solutionComparator);
	}

	public void stopSearching() throws Exception
	{
		_tabuSearch.stopSolving();

		while (isRunning())
		{
			Thread.sleep(50);
		}

	}

	public boolean isRunning()
	{
		return _tabuSearch.isSolving();
	}

	public void setParameters(AlgorithmParams params) throws Exception
	{
		_params = params;
		_params.putValue("id", "TabuSearch");
		DataNode p = params.getDataNode("Parameters");
		_iterationToGo = _statNumIter = p.getValueInt("numIteration");

		_tabuListLength = p.getValueInt("tabuListLength");
		p.getValueInt("numIterDivers");
		_solutionsToExplore = p.getValueInt("numSolutions");

	}

	public AlgorithmReport getReport() throws Exception
	{
		int num = _solutions.length;
		double avg = 0;
		for (int i = 0; i < num; i++)
		{
			avg += _solutions[i].getObjectiveValue()[0];
		}

		avg /= num;

		// DataNode stats = new DataNode("statistics");
		// stats.putValue("NumberOfIter", new Integer(_statNumIter));
		// stats.putValue("NumberOfNewSolutions", new Integer(_statNumNewSol));
		// stats.putValue("LastIterNumberNewSol", new
		// Integer(_statLastIterNewSol));
		// stats.putValue("ObjValDelta", new Double(Math.abs(_statInitObjVal -
		// _statEndObjVal)));
		// stats.putValue("MinObjVal", new Double(_statEndObjVal));
		// stats.putValue("AvgObjVal", new Double(avg));

		_reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

		return _reporter.getReport();
	}

	@SuppressWarnings("deprecation")
	private void setBestEverSolution() throws Exception
	{
		try
		{
			if (_solutions[0].getObjectiveValue() == null)
			{
				_solutions[0].setObjectiveValue(_objectiveFunction.evaluate(_solutions[0], null));
			}
			_bestEverSolution = (Solution) _solutions[0].clone();

			for (int i = 1; i < _solutions.length; i++)
			{
				if (_solutions[i].getObjectiveValue() == null)
				{
					_solutions[i].setObjectiveValue(_objectiveFunction.evaluate(_solutions[i], null));
				}
				if (TabuSearch.firstIsBetterThanSecond(_solutions[i].getObjectiveValue(), _bestEverSolution.getObjectiveValue(), _maximizing))
				{
					_bestEverSolution = (Solution) _solutions[i].clone();
				}
			}
			_statInitObjVal = _bestEverSolution.getObjectiveValue()[0];
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	private class TabuSearchObserver implements TabuSearchListener
	{
		// params

		public TabuSearchObserver()
		{
		}

		public void tabuSearchStarted(TabuSearchEvent e)
		{
			_algorithmStarted = true;
			_statLastIterNewSol = 0;
		}

		public void tabuSearchStopped(TabuSearchEvent e)
		{
			_algorithmStopped = true;
			_statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
		}

		@SuppressWarnings("deprecation")
		public void newBestSolutionFound(TabuSearchEvent e)
		{
			try
			{
				Solution newBest = e.getTabuSearch().getBestSolution();

				if (_bestEverSolution.getObjectiveValue() == null)
				{
					_bestEverSolution = (Solution) newBest.clone();
					// System.out.println(_searchID + "  " +
					// newBest.getObjectiveValue()[0]);

					// addSolutionToGraph(e.getTabuSearch().getIterationsCompleted(),
					// _bestEverSolution);
					_statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
					_statNumNewSol++;

					if (_statLastIterNewSol < e.getTabuSearch().getIterationsCompleted())
					{
						_statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();
					}
				}
				else if (TabuSearch.firstIsBetterThanSecond(newBest.getObjectiveValue(), _bestEverSolution.getObjectiveValue(), _maximizing))
				{
					_bestEverSolution = (Solution) newBest.clone();
					// System.out.println(_searchID + "  " +
					// newBest.getObjectiveValue()[0]);
					// addSolutionToGraph(e.getTabuSearch().getIterationsCompleted(),
					// _bestEverSolution);
					_statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
					_statNumNewSol++;

					if (_statLastIterNewSol < e.getTabuSearch().getIterationsCompleted())
					{
						_statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();
					}
				}

				// System.out.println(_bestEverSolution);
				// DataNode log = new DataNode("newSolution");
				// log.putValue("time", System.currentTimeMillis());
				// log.putValue("numIter",
				// e.getTabuSearch().getIterationsCompleted());
				// log.putValue("objVal", newBest.getObjectiveValue()[0]);
				// log.putValue("solution", newBest.toString());
				_reporter.putNewSolution(System.currentTimeMillis(), e.getTabuSearch().getIterationsCompleted(), newBest.getObjectiveValue()[0], newBest.toString());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

		}

		public void newCurrentSolutionFound(TabuSearchEvent e)
		{
		}

		public void unimprovingMoveMade(TabuSearchEvent e)
		{
		}

		public void improvingMoveMade(TabuSearchEvent e)
		{
		}

		public void noChangeInValueMoveMade(TabuSearchEvent e)
		{
		}
	}
}
