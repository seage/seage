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
package org.seage.aal.algorithm.genetics;

import java.util.List;
import java.util.logging.Level;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectComparator;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 * GeneticSearchAdapter class
 */
@AlgorithmParameters({
        @Parameter(name = "crossLengthPct", min = 0, max = 100, init = 10),
        @Parameter(name = "eliteSubjectPct", min = 0, max = 100, init = 10),
        @Parameter(name = "iterationCount", min = 100, max = 100000, init = 100),
        @Parameter(name = "mutateLengthPct", min = 0, max = 100, init = 10),
        @Parameter(name = "mutateSubjectPct", min = 0, max = 100, init = 10),
        @Parameter(name = "numSolutions", min = 10, max = 1000, init = 100),
        @Parameter(name = "randomSubjectPct", min = 0, max = 100, init = 10) })
public abstract class GeneticAlgorithmAdapter<S extends Subject<?>> extends AlgorithmAdapterImpl
{

    protected List<S> _solutions;
    protected GeneticAlgorithm<S> _geneticSearch;
    protected SubjectEvaluator<S> _evaluator;
    protected SubjectComparator<S> _comparator;
    private GeneticAlgorithmListener _algorithmListener;
    protected S _bestEverSolution;
    protected AlgorithmParams _params;
    private String _searchID;
    // private String _paramID;

    private double _statInitObjVal;
    private double _statBestObjVal;
    private int _statNrOfIterationsDone;
    private int _statNumNewSol;
    private int _statLastImprovingIteration;
    // private int _nrOfIterationsDone;

    private AlgorithmReporter _reporter;

    // private DataNode _minutes;

    public GeneticAlgorithmAdapter(GeneticOperator<S> operator, SubjectEvaluator<S> evaluator, boolean maximizing,
            String searchID)
    {
        _evaluator = evaluator;
        _algorithmListener = new GeneticAlgorithmListener();
        _comparator = new SubjectComparator<S>();
        _geneticSearch = new GeneticAlgorithm<S>(operator, evaluator);
        _geneticSearch.addGeneticSearchListener(_algorithmListener);
        _searchID = searchID;
    }

    /**
     * <running> <parameters/> <minutes/> <statistics/> </running>
     * 
     * @param param
     * @throws java.lang.Exception
     */
    @Override
    public void startSearching(final AlgorithmParams params) throws Exception
    {
        if (params == null)
            throw new Exception("Parameters not set");
        setParameters(params);

        _reporter = new AlgorithmReporter(_searchID);
        _reporter.putParameters(_params);

        _geneticSearch.startSearching(_solutions);

        _solutions = _geneticSearch.getSubjects();
        if (_solutions == null)
            throw new Exception("Solutions null");
    }

    @Override
    public boolean isRunning()
    {
        return _geneticSearch.isRunning();
    }

    @Override
    public void stopSearching() throws Exception
    {
        _geneticSearch.stopSolving();

        while (isRunning())
            Thread.sleep(100);
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;

        _geneticSearch.getOperator().setCrossLengthCoef(_params.getValueDouble("crossLengthPct") / 100);
        _geneticSearch.getOperator().setMutateLengthCoef(_params.getValueDouble("mutateLengthPct") / 100);
        _geneticSearch.setEliteSubjectsPct(_params.getValueDouble("eliteSubjectPct"));
        _geneticSearch.setIterationToGo(_params.getValueInt("iterationCount"));
        _geneticSearch.setMutatePopulationPct(_params.getValueDouble("mutateSubjectPct"));
        _geneticSearch.setPopulationCount(_params.getValueInt("numSolutions"));
        _geneticSearch.setRandomSubjectsPct(_params.getValueDouble("randomSubjectPct"));

        // _paramID = param.getValue("ID");
    }

    @Override
    public AlgorithmReport getReport() throws Exception
    {
        int num = _solutions.size();// > 10 ? 10 : solutions.length;
        double avg = 0;
        for (int i = 0; i < num; i++)
            avg += _solutions.get(i).getObjectiveValue()[0];
        avg /= num;

        _reporter.putStatistics(_statNrOfIterationsDone, _statNumNewSol, _statLastImprovingIteration, _statInitObjVal,
                avg, _statBestObjVal);

        return _reporter.getReport();
    }

    @Override
    public abstract void solutionsFromPhenotype(Object[][] source) throws Exception;

    // Returns solutions in best-first order
    @Override
    public abstract Object[][] solutionsToPhenotype() throws Exception;

    private class GeneticAlgorithmListener implements IAlgorithmListener<GeneticAlgorithmEvent<S>>
    {
        @Override
        public void algorithmStarted(GeneticAlgorithmEvent<S> e)
        {
            _algorithmStarted = true;
            _statNumNewSol = _statLastImprovingIteration = 0;
        }

        @Override
        public void algorithmStopped(GeneticAlgorithmEvent<S> e)
        {
            _algorithmStopped = true;
            _statNrOfIterationsDone = e.getGeneticSearch().getCurrentIteration();
            S s = e.getGeneticSearch().getBestSubject();
            if (s != null)
                _statBestObjVal = s.getObjectiveValue()[0];
        }

        @Override
        @SuppressWarnings("unchecked")
        public void newBestSolutionFound(GeneticAlgorithmEvent<S> e)
        {
            try
            {
                GeneticAlgorithm<S> gs = e.getGeneticSearch();
                S subject = gs.getBestSubject();
                if (_statNumNewSol == 0)
                    _statInitObjVal = subject.getObjectiveValue()[0];

                _bestEverSolution = (S) gs.getBestSubject().clone();
                _reporter.putNewSolution(System.currentTimeMillis(), gs.getCurrentIteration(),
                        subject.getObjectiveValue()[0], subject.toString());
                _statNumNewSol++;
                _statLastImprovingIteration = gs.getCurrentIteration();

            }
            catch (Exception ex)
            {
                _logger.warn( ex.getMessage(), ex);
            }
        }

        @Override
        public void noChangeInValueIterationMade(GeneticAlgorithmEvent<S> e)
        {

        }

        @Override
        public void iterationPerformed(GeneticAlgorithmEvent<S> e)
        {

        }
    }
}
