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
package org.seage.aal.tabusearch;

import java.util.*;
import org.seage.aal.AlgorithmReporter;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.AlgorithmReport;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.*;

/**
 *  TabuSearchAdapter interface.
 */
@AlgorithmParameters({
    @Parameter(name="numIteration", min=1, max=1000000, init=1000),
    @Parameter(name="numSolutions", min=1, max=1, init=1),
    @Parameter(name="tabuListLength", min=1, max=10000, init=30),
    @Parameter(name="numIterDivers", min=1, max=1, init=1)
})
public abstract class TabuSearchAdapter implements IAlgorithmAdapter
{

    private TabuSearch _tabuSearch;
    private ObjectiveFunction _objectiveFunction;
    private LongTermMemory _longTermMemory;
    private TabuSearchObserver _observer;
    private int _iterationToGo;
    private int _tabuListLength;
    private int _iterationDivers;
    private boolean _maximizing;
    protected Solution[] _solutions;
    protected int _solutionsToExplore;
    protected Solution _bestEverSolution;				// best of all solution
    private SolutionComparator _solutionComparator;
    private double _statInitObjVal;
    private double _statEndObjVal;
    private int _statNumIter;
    private int _statNumNewSol;
    private int _statLastIterNewSol;
    private String _searchID;
    private AlgorithmReporter _reporter;

    public TabuSearchAdapter(MoveManager moveManager,
            ObjectiveFunction objectiveFunction,
            LongTermMemory longTermMemory,
            String searchID)
    {
        _observer = new TabuSearchObserver();
        _tabuSearch = new TabuSearch(moveManager, objectiveFunction, longTermMemory, false);
        _tabuSearch.addTabuSearchListener(_observer);
        _objectiveFunction = objectiveFunction;
        _longTermMemory = longTermMemory;

        _tabuSearch.setAspirationCriteria(new BestEverAspirationCriteria());
        _iterationToGo = 0;
        _tabuListLength = 0;
        _solutionComparator = new SolutionComparator(false);
        _searchID = searchID;
    }

    @Override
    public void startSearching(DataNode params) throws Exception
    {
        _reporter = new AlgorithmReporter(_searchID);
        _reporter.putParameters(params);

        setParameters(params);

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
            Random rnd = new Random();
            mask[0] = true;
            for (int i = 1; i < _solutionsToExplore; i++)
            {
                mask[i] = true;
            }
        }

        _statNumNewSol = 0;
        Solution currentSolution = null;
        setBestEverSolution();				// z nactenych reseni, ulozi nejlepsi

        for (int i = 0; i < _solutions.length; i++)
        {
            //if(_solutions[i].getObjectiveValue()==null)
            if (mask[i] == false)
            {
                continue;
            }

            //currentSolution = _solutions[_random.nextInt(_solutions.length)];
            currentSolution = _solutions[i];
            //System.out.println("-\t" + currentSolution);
            _tabuSearch.setTabuList(new SimpleTabuList(_tabuListLength));
            _tabuSearch.setBestSolution(null);
            _longTermMemory.clearMemory();
            _longTermMemory.resetIterNumber();
            for (int j = 0; j < _iterationDivers; j++)
            {
                //if(i%2==1)
                //    _tabuSearch.setBestSolution(null);
                _tabuSearch.setIterationsToGo(_iterationToGo);
                _tabuSearch.setBestSolution(currentSolution);
                _tabuSearch.setCurrentSolution(currentSolution);

                _tabuSearch.startSolving();
                //while (_tabuSearch.isSolving())
                //    Thread.currentThread().sleep(500);
                //System.out.println("+\t" + _tabuSearch.getBestSolution());
                currentSolution = _longTermMemory.diversifySolution();
                //longTermMemory.clearMemory();
                if (currentSolution == null)
                {
                    currentSolution = _tabuSearch.getBestSolution();
                }
            }
            _solutions[i] = _tabuSearch.getBestSolution();

            //double[] val = _objectiveFunction.evaluate(_solutions[i], null);
            //int a = 0;

        }
        Arrays.sort(_solutions, _solutionComparator);
    }

    public void stopSearching() throws Exception
    {
        _tabuSearch.stopSolving();
    }

    private void setParameters(DataNode param) throws Exception
    {
        _iterationToGo = _statNumIter = param.getValueInt("numIteration");

        _tabuListLength = param.getValueInt("tabuListLength");
        _iterationDivers = param.getValueInt("numIterDivers");
        _solutionsToExplore = param.getValueInt("numSolutions");

        //_paramID = param.getParam("ID");
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

//		DataNode stats = new DataNode("statistics");
//		stats.putValue("NumberOfIter", new Integer(_statNumIter));
//		stats.putValue("NumberOfNewSolutions", new Integer(_statNumNewSol));
//		stats.putValue("LastIterNumberNewSol", new Integer(_statLastIterNewSol));
//		stats.putValue("ObjValDelta", new Double(Math.abs(_statInitObjVal - _statEndObjVal)));
//		stats.putValue("MinObjVal", new Double(_statEndObjVal));
//		stats.putValue("AvgObjVal", new Double(avg));

        _reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

        return _reporter.getReport();
    }

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
                if (_tabuSearch.firstIsBetterThanSecond(_solutions[i].getObjectiveValue(), _bestEverSolution.getObjectiveValue(), _maximizing))
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

        public int _newBestSolutionFound;
        private int _newCurrentSolutionFound;
        private int _unimprovingMoveMade;
        private int _improvingMoveMade;
        private int _noChangeInValueMoveMade;

        public TabuSearchObserver()
        {
            _newBestSolutionFound = 0;
            _newCurrentSolutionFound = 0;
            _unimprovingMoveMade = 0;
            _improvingMoveMade = 0;
            _noChangeInValueMoveMade = 0;
        }

        public void tabuSearchStarted(TabuSearchEvent e)
        {
            _statLastIterNewSol = 0;
        }

        public void tabuSearchStopped(TabuSearchEvent e)
        {
        }

        public void newBestSolutionFound(TabuSearchEvent e)
        {
            try
            {
                _newBestSolutionFound++;
                Solution newBest = e.getTabuSearch().getBestSolution();

                if (_bestEverSolution.getObjectiveValue() == null)
                {
                    _bestEverSolution = (Solution) newBest.clone();
                    //System.out.println(_searchID + "  " + newBest.getObjectiveValue()[0]);

                    //addSolutionToGraph(e.getTabuSearch().getIterationsCompleted(), _bestEverSolution);
                    _statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
                    _statNumNewSol++;

                    if (_statLastIterNewSol < e.getTabuSearch().getIterationsCompleted())
                    {
                        _statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();
                    }
                }
                else if (_tabuSearch.firstIsBetterThanSecond(newBest.getObjectiveValue(), _bestEverSolution.getObjectiveValue(), _maximizing))
                {
                    _bestEverSolution = (Solution) newBest.clone();
                    //System.out.println(_searchID + "  " + newBest.getObjectiveValue()[0]);
                    //addSolutionToGraph(e.getTabuSearch().getIterationsCompleted(), _bestEverSolution);
                    _statEndObjVal = _bestEverSolution.getObjectiveValue()[0];
                    _statNumNewSol++;

                    if (_statLastIterNewSol < e.getTabuSearch().getIterationsCompleted())
                    {
                        _statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();
                    }
                }

                //System.out.println(_bestEverSolution);
//                DataNode log = new DataNode("newSolution");
//                log.putValue("time", System.currentTimeMillis());
//                log.putValue("numIter", e.getTabuSearch().getIterationsCompleted());
//                log.putValue("objVal", newBest.getObjectiveValue()[0]);
//                log.putValue("solution", newBest.toString());
                _reporter.putNewSolution(System.currentTimeMillis(), e.getTabuSearch().getIterationsCompleted(), newBest.getObjectiveValue()[0], newBest.toString());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

        public void newCurrentSolutionFound(TabuSearchEvent e)
        {
            _newCurrentSolutionFound++;
        }

        public void unimprovingMoveMade(TabuSearchEvent e)
        {
            _unimprovingMoveMade++;
        }

        public void improvingMoveMade(TabuSearchEvent e)
        {
            _improvingMoveMade++;
        }

        public void noChangeInValueMoveMade(TabuSearchEvent e)
        {
            _noChangeInValueMoveMade++;
        }
    }
}
