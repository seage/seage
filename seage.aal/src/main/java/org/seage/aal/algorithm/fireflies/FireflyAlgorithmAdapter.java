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
 *     Karel Durkota
 */
package org.seage.aal.algorithm.fireflies;


import org.seage.aal.algorithm.fireflies.*;
import org.seage.data.DataNode;
import org.seage.metaheuristic.fireflies.*;
import java.util.Arrays;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;

/**
 * FireflySearchAdapter class
 */

@AlgorithmParameters({
    @Parameter(name="iterationCount", min=0, max=1000000, init=1000),
    @Parameter(name="numSolutions", min=2, max=1000000, init=100),
    @Parameter(name="timeStep", min=0.1, max=1000, init=0.5),
    @Parameter(name="withDecreasingRandomness", min=0, max=1, init=1),
//    @Parameter(name="initialIntensity", min=0, max=100000, init=1),
//    @Parameter(name="initialRandomness", min=0, max=100000, init=1),
//    @Parameter(name="finalRandomness", min=0, max=100000, init=0.2),
    @Parameter(name="absorption", min=0, max=10000, init=0.1)
//    @Parameter(name="populationSize", min=0, max=10000, init=0.1)
})
public abstract class FireflyAlgorithmAdapter extends  AlgorithmAdapterImpl
{
    protected Solution[] _solutions;
    private FireflySearch _fireflySearch;
    private ObjectiveFunction _evaluator;
    private SolutionComparator _comparator;
    private FireflySearchObserver _observer;
    private Solution _bestEverSolution;
    private boolean _maximizing;
    private AlgorithmParams _params;
    private String _searchID;
    //private String _paramID;

    private double _statInitObjVal;
    private double _statEndObjVal;
    private int _statNumIter;
    private int _statNumNewSol;
    private int _statLastIterNewSol;
    
    private AlgorithmReporter _reporter;
    //private DataNode _minutes;


    public FireflyAlgorithmAdapter(FireflyOperator operator,
                                ObjectiveFunction evaluator,
                                boolean maximizing,
                                String  searchID)
    {
        _evaluator = evaluator;
        _observer = new FireflySearchObserver();
        _comparator = new SolutionComparator();
        _fireflySearch = new FireflySearch(operator, evaluator);
        _fireflySearch.addFireflySearchListener(_observer);
        _searchID = searchID;
        _maximizing=maximizing;
    }

    /**
     * <running>
     *      <parameters/>
     *      <minutes/>
     *      <statistics/>
     * </running>
     * @param param
     * @throws java.lang.Exception
     */

    public void startSearching() throws Exception
    {
        _reporter = new AlgorithmReporter(_searchID);
        _reporter.putParameters(_params);

        setBestEverSolution();

        _fireflySearch.startSolving(_solutions);
        _solutions = _fireflySearch.getSolutions();
        if(_solutions == null)
            throw new Exception("Solutions null");
    }

    public void stopSearching() throws Exception
    {
        _fireflySearch.stopSolving();
        
        while(isRunning())
            Thread.sleep(100);
    }
    
    public boolean isRunning() {
        return _fireflySearch.isRunning();
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;
        DataNode p = params.getDataNode("Parameters");
        _fireflySearch.setIterationsToGo(p.getValueInt("iterationCount"));
        _statNumIter = p.getValueInt("iterationCount");
//        _geneticSearch.setSolutionCount(param.getValueInt("numSolution"));
        _fireflySearch.setPopulationCount(p.getValueInt("numSolutions"));
        _fireflySearch.setAbsorption(p.getValueDouble("absorption"));
//        _EFASearch.setInitialIntensity(param.getValueDouble("initialIntensity"));
//        _EFASearch.setInitialRandomness(param.getValueDouble("initialRandomness"));
//        _EFASearch.setFinalRandomness(param.getValueDouble("finalRandomness"));
//        _EFASearch.setAbsorption(param.getValueDouble("absorption"));
        _fireflySearch.setTimeStep(p.getValueDouble("timeStep"));
        _fireflySearch.setWithDecreasingRandomness(((p.getValueDouble("withDecreasingRandomness")>0)?true:false));
        // EDD OWN PARAMETERS

        //_paramID = param.getValue("ID");
    }

    private void setBestEverSolution() throws Exception
    {
        if(_solutions==null || _solutions.length==0)
            return;
        try
        {
            if (_solutions[0].getObjectiveValue()==null || _solutions[0].getObjectiveValue().length==0)
                _solutions[0].setObjectiveValue(_evaluator.evaluate(_solutions[0]));
            _bestEverSolution = (Solution)_solutions[0].clone();

            for (int i = 1; i < _solutions.length; i++)
            {
                if (_solutions[i].getObjectiveValue()==null)
                    _solutions[i].setObjectiveValue(_evaluator.evaluate(_solutions[i]));

                if (_comparator.compare(_solutions[i], _bestEverSolution)==1)
                    _bestEverSolution = (Solution)_solutions[i].clone();
            }
            _statInitObjVal = _bestEverSolution.getObjectiveValue()[0];
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
	
    public AlgorithmReport getReport() throws Exception
    {
        int num = _solutions.length;// > 10 ? 10 : solutions.length;
        double avg = 0;
        for (int i = 0; i < num; i++)
            avg +=_solutions[i].getObjectiveValue()[0];
        avg /= num;
        //DataNode stats = new DataNode("statistics");
//        stats.putValue("numberOfIter", _statNumIter);
//        stats.putValue("numberOfNewSolutions", _statNumNewSol);
//        stats.putValue("lastIterNumberNewSol", _statLastIterNewSol);
//        stats.putValue("initObjVal", _statInitObjVal);
//        stats.putValue("avgObjVal", avg);
//        stats.putValue("bestObjVal", _statEndObjVal);

        _reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

        return _reporter.getReport();
    }

    private class FireflySearchObserver implements FireflySearchListener
    { 
        public void FireflySearchStarted(FireflySearchEvent e)
        {
            _statNumNewSol = _statLastIterNewSol = 0;
        }

        public void FireflySearchStopped(FireflySearchEvent e)
        {
            _statEndObjVal = e.getFireflySearch().getBestSolution().getObjectiveValue()[0];
        }
        public void newBestSolutionFound(FireflySearchEvent e)
        {
            try
            {
                Solution solution = e.getFireflySearch().getBestSolution();
                _bestEverSolution = (Solution)e.getFireflySearch().getBestSolution().clone();

                //System.out.println(_searchID+"  "+solution.getObjectiveValue()[0] + " \t" + solution.hashCode() + "\t" + solution.toString());
//                DataNode log = new DataNode("newSolution");
//                log.putValue("time", System.currentTimeMillis());
//                log.putValue("numIter", e.getFireflySearch().getCurrentIteration());
//                log.putValue("objVal", subject.getObjectiveValue()[0]);
//                log.putValue("solution", subject.toString());
                _reporter.putNewSolution(System.currentTimeMillis(), e.getFireflySearch().getCurrentIteration(), solution.getObjectiveValue()[0], solution.toString());
                _statNumNewSol++;
                _statLastIterNewSol = e.getFireflySearch().getCurrentIteration();

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void noChangeInValueIterationMade(FireflySearchEvent e)
        {

        }


        // HAD TO IMPLEMENT BECAUSE OF INTERFACE, MAY CAUSE ALGORITHM NOT TO WORK CORRECTLY
        public void newCurrentSolutionFound(FireflySearchEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void unimprovingMoveMade(FireflySearchEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void improvingMoveMade(FireflySearchEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void noChangeInValueMoveMade(FireflySearchEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
