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
 *     Karel Durkota
 */
package org.seage.aal.algorithm.fireflies;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.FireflySearchEvent;
import org.seage.metaheuristic.fireflies.FireflySearchListener;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionComparator;

/**
 * FireflySearchAdapter class
 */

@AlgorithmParameters({ 
	@Parameter(name = "iterationCount", min = 500, max = 500, init = 10), 
	@Parameter(name = "numSolutions", min = 500, max = 500, init = 500),
    @Parameter(name = "timeStep", min = 0.1, max = 2, init = 0.15), 
    @Parameter(name = "withDecreasingRandomness", min = 1, max = 1, init = 1),
        // @Parameter(name="initialIntensity", min=0, max=100000, init=1),
        // @Parameter(name="initialRandomness", min=0, max=100000, init=1),
        // @Parameter(name="finalRandomness", min=0, max=100000, init=0.2),
        @Parameter(name = "absorption", min = 0, max = 1, init = 0.025)
// @Parameter(name="populationSize", min=0, max=10000, init=0.1)
})
public abstract class FireflyAlgorithmAdapter extends AlgorithmAdapterImpl
{
    protected Solution[]          _solutions;
    private FireflySearch         _fireflySearch;
    private ObjectiveFunction     _evaluator;
    private SolutionComparator    _comparator;
    private FireflySearchObserver _observer;
    private Solution              _bestEverSolution;
    private AlgorithmParams       _params;
    private String                _searchID;
    // private String _paramID;

    private double                _statInitObjVal;
    private double                _statEndObjVal;
    private int                   _statNumIter;
    private int                   _statNumNewSol;
    private int                   _statLastIterNewSol;

    private AlgorithmReporter     _reporter;

    // private DataNode _minutes;

    public FireflyAlgorithmAdapter(FireflyOperator operator, ObjectiveFunction evaluator, boolean maximizing, String searchID)
    {
        _evaluator = evaluator;
        _observer = new FireflySearchObserver();
        _comparator = new SolutionComparator();
        _fireflySearch = new FireflySearch(operator, evaluator);
        _fireflySearch.addFireflySearchListener(_observer);
        _searchID = searchID;
    }

    /**
     * <running> <parameters/> <minutes/> <statistics/> </running>
     * 
     * @param param
     * @throws java.lang.Exception
     */
    @Override
    public void startSearching(AlgorithmParams params) throws Exception
    {
    	if(params==null)
    		throw new Exception("Parameters not set");
    	setParameters(params);
    	
        _reporter = new AlgorithmReporter(_searchID);
        _reporter.putParameters(_params);        

        _fireflySearch.startSolving(_solutions);
        _solutions = _fireflySearch.getSolutions();
        if (_solutions == null)
            throw new Exception("Solutions null");
    }

    @Override
    public void stopSearching() throws Exception
    {
        _fireflySearch.stopSolving();

        while (isRunning())
            Thread.sleep(100);
    }

    @Override
    public boolean isRunning()
    {
        return _fireflySearch.isRunning();
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;
        _params.putValue("id", "FireflyAlgorithm");
        DataNode p = params.getDataNode("Parameters");
        _fireflySearch.setIterationsToGo(p.getValueInt("iterationCount"));
        _statNumIter = p.getValueInt("iterationCount");
        // _geneticSearch.setSolutionCount(param.getValueInt("numSolution"));
        _fireflySearch.setPopulationCount(p.getValueInt("numSolutions"));
        _fireflySearch.setAbsorption(p.getValueDouble("absorption"));
        // _EFASearch.setInitialIntensity(param.getValueDouble("initialIntensity"));
        // _EFASearch.setInitialRandomness(param.getValueDouble("initialRandomness"));
        // _EFASearch.setFinalRandomness(param.getValueDouble("finalRandomness"));
        // _EFASearch.setAbsorption(param.getValueDouble("absorption"));
        _fireflySearch.setTimeStep(p.getValueDouble("timeStep"));
        _fireflySearch.setWithDecreasingRandomness(((p.getValueDouble("withDecreasingRandomness") > 0) ? true : false));
        // EDD OWN PARAMETERS

        // _paramID = param.getValue("ID");
    }


    public AlgorithmReport getReport() throws Exception
    {
        int num = _solutions.length;// > 10 ? 10 : solutions.length;
        double avg = 0;
        for (int i = 0; i < num; i++)
            avg += _solutions[i].getObjectiveValue()[0];
        avg /= num;

        _reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

        return _reporter.getReport();
    }

    private class FireflySearchObserver implements FireflySearchListener
    {
        public void FireflySearchStarted(FireflySearchEvent e)
        {
            _statNumNewSol = _statLastIterNewSol = 0;
            _algorithmStarted = true;
        }

        public void FireflySearchStopped(FireflySearchEvent e)
        {
            Solution best = e.getFireflySearch().getBestSolution();
            if (best != null)
                _statEndObjVal = best.getObjectiveValue()[0];
            _algorithmStopped = true;
        }

        public void newBestSolutionFound(FireflySearchEvent e)
        {
            try
            {
                Solution solution = e.getFireflySearch().getBestSolution();
                _bestEverSolution = (Solution) e.getFireflySearch().getBestSolution().clone();
                if(_statNumNewSol==0)
                	_statInitObjVal = solution.getObjectiveValue()[0];

                _reporter.putNewSolution(System.currentTimeMillis(), e.getFireflySearch().getCurrentIteration(), solution.getObjectiveValue()[0], solution.toString());
                _statNumNewSol++;
                _statLastIterNewSol = e.getFireflySearch().getCurrentIteration();

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @SuppressWarnings("unused")
		public void noChangeInValueIterationMade(FireflySearchEvent e)
        {

        }

        // HAD TO IMPLEMENT BECAUSE OF INTERFACE, MAY CAUSE ALGORITHM NOT TO
        // WORK CORRECTLY
        public void newCurrentSolutionFound(FireflySearchEvent e)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void unimprovingMoveMade(FireflySearchEvent e)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void improvingMoveMade(FireflySearchEvent e)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void noChangeInValueMoveMade(FireflySearchEvent e)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
