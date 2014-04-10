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

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;

/**
 * TabuSearchAdapter interface.
 */
@AlgorithmParameters({ 
	@Parameter(name = "iterationCount", min = 1, max = 1000000, init = 1000),
	@Parameter(name = "numSolutions", min = 1, max = 1, init = 1), 
	@Parameter(name = "tabuListLength", min = 1, max = 1000, init = 30) })
public abstract class TabuSearchAdapter extends AlgorithmAdapterImpl
{

	private TabuSearch _tabuSearch;
	private TabuSearchObserver _observer;
	private int _iterationToGo;
	private int _tabuListLength;
	protected Solution[] _solutions;
	protected int _solutionsToExplore;
	protected Solution _bestEverSolution; // best of all solution
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

		_tabuSearch.setAspirationCriteria(new BestEverAspirationCriteria());
		_iterationToGo = 0;
		_tabuListLength = 0;
		_searchID = searchID;
	}

	@Override
	public void startSearching(AlgorithmParams params) throws Exception
	{
		if(params==null)
    		throw new Exception("Parameters not set");
    	setParameters(params);
    	
		_reporter = new AlgorithmReporter(_searchID);
		_reporter.putParameters(_params);


		if ( _solutions.length > 1)
		{
			_logger.warning("More than one solutions to solve, used just the first one.");
		}		

		_statNumNewSol = 0;

		_tabuSearch.setCurrentSolution(_solutions[0]);
		_tabuSearch.setTabuList(new SimpleTabuList(_tabuListLength));
		_tabuSearch.setIterationsToGo(_iterationToGo);

		_tabuSearch.startSolving();

		_solutions[0] = _tabuSearch.getBestSolution();


	}

	@Override
	public void stopSearching() throws Exception
	{
		_tabuSearch.stopSolving();

		while (isRunning())
		{
			Thread.sleep(50);
		}
	}

	@Override
	public boolean isRunning()
	{
		return _tabuSearch.isSolving();
	}

	public void setParameters(AlgorithmParams params) throws Exception
	{
		_params = params;		
		_iterationToGo = _statNumIter = _params.getValueInt("iterationCount");

		_tabuListLength = _params.getValueInt("tabuListLength");
		_solutionsToExplore = _params.getValueInt("numSolutions");

	}

	@Override
	public AlgorithmReport getReport() throws Exception
	{
		_reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, (_statInitObjVal+_statEndObjVal)/2, _statEndObjVal);

		return _reporter.getReport();
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

		public void newBestSolutionFound(TabuSearchEvent e)
		{
			try
			{
				Solution newBest = e.getTabuSearch().getBestSolution();
				_bestEverSolution = newBest;		
				
				_statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();				
				
				_statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
				_statNumNewSol++;
				
				if(_statNumNewSol==1)
					_statInitObjVal = _statEndObjVal;

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
