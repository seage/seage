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


import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.*;

import java.util.Arrays;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;

/**
 * GeneticSearchAdapter class
 */
@AlgorithmParameters({
    @Parameter(name="crossLengthPct", min=0, max=100, init=10),
    @Parameter(name="eliteSubjectPct", min=0, max=100, init=10),
    @Parameter(name="iterationCount", min=100, max=100000, init=100),
    @Parameter(name="mutateLengthPct", min=0, max=100, init=10),   
    @Parameter(name="mutateSubjectPct", min=0, max=100, init=10),
    @Parameter(name="numSolutions", min=10, max=1000, init=100),   
    @Parameter(name="randomSubjectPct", min=0, max=100, init=10)
})
public class GeneticAlgorithmAdapter extends AlgorithmAdapterImpl
{
    protected Subject[] _solutions;
    private GeneticSearch _geneticSearch;
    private Evaluator _evaluator;
    private SubjectComparator _comparator;
    private GeneticSearchListener _algorithmListener;
    private Subject _bestEverSolution;
    private AlgorithmParams _params;
    private String _searchID;
    //private String _paramID;
    
    private boolean _isRunning;

    private double _statInitObjVal;
    private double _statEndObjVal;
    private int _statNrOfIterationsDone;
    private int _statNumNewSol;
    private int _statLastIterNewSol;
    //private int _nrOfIterationsDone;
    
    private AlgorithmReporter _reporter;
    //private DataNode _minutes;


    public GeneticAlgorithmAdapter(GeneticOperator operator, 
                                Evaluator evaluator, 
                                boolean maximizing,
                                String searchID)
    {
        _evaluator = evaluator;
        _algorithmListener = new GeneticSearchListener();
        _comparator = new SubjectComparator();
        _geneticSearch = new GeneticSearch(operator, evaluator);
        _geneticSearch.addGeneticSearchListener(_algorithmListener);
        _searchID = searchID;
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

        _geneticSearch.startSearching(_solutions);

        _solutions = _geneticSearch.getSubjects();
        if(_solutions == null)
            throw new Exception("Solutions null");
    }
    
    public boolean isRunning() {
        return _geneticSearch.isRunning();
    }

    public void stopSearching() throws Exception
    {
        _geneticSearch.stopSolving();
        
        while(isRunning())
            Thread.sleep(100);
    }

    public void setParameters(AlgorithmParams params) throws Exception
    {
        _params = params;
        _params.putValue("id", "GeneticAlgorithm");
        
        DataNode p = params.getDataNode("Parameters");
        _geneticSearch.getOperator().setCrossLengthPct(p.getValueInt("crossLengthPct") / 100.0);
        _geneticSearch.getOperator().setMutatePct(p.getValueInt("mutateLengthPct") / 100.0);
        _geneticSearch.setEliteSubjectPct(p.getValueInt("eliteSubjectPct") / 100.0);
        _geneticSearch.setIterationToGo(p.getValueInt("iterationCount"));
        //_statNumIter = p.getValueInt("iterationCount");
        _geneticSearch.setMutateSubjectPct(p.getValueInt("mutateSubjectPct") / 100.0);
        _geneticSearch.setPopulationCount(p.getValueInt("numSolutions"));
        _geneticSearch.setRandomSubjectPct(p.getValueInt("randomSubjectPct") / 100.0);

        //_paramID = param.getValue("ID");
    }

    private void setBestEverSolution() throws Exception
    {
        try
        {
            if (_solutions[0].getObjectiveValue()==null || _solutions[0].getObjectiveValue().length==0)
                _solutions[0].setObjectiveValue(_evaluator.evaluate(_solutions[0]));
            _bestEverSolution = (Subject)_solutions[0].clone();

            for (int i = 1; i < _solutions.length; i++)
            {
                if (_solutions[i].getObjectiveValue()==null || _solutions[0].getObjectiveValue().length==0)
                    _solutions[i].setObjectiveValue(_evaluator.evaluate(_solutions[i]));

                if (_comparator.compare(_solutions[i], _bestEverSolution)==1)
                    _bestEverSolution = (Subject)_solutions[i].clone();
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

        _reporter.putStatistics(_statNrOfIterationsDone, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

        return _reporter.getReport();
    }

    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        int numSolutions = source.length;

        _solutions = new Subject[numSolutions];

        for (int i = 0; i < numSolutions; i++)
        {
            _solutions[i] = new Subject(new Genome(1, source[i].length));
            for (int j = 0; j < source[i].length; j++)
            {
                int geneValue = ((Integer)source[i][j]).intValue();
                _solutions[i].getGenome().getChromosome(0).getGene(j).setValue(geneValue);
            }
        }
    }

    // Returns solutions in best-first order
    public Object[][] solutionsToPhenotype() throws Exception
    {
        try
        {
            for (int i = 0; i < _solutions.length; i++)
                _solutions[i].setObjectiveValue(_evaluator.evaluate(_solutions[i]));
            Arrays.sort(_solutions, _comparator);
            
            Object[][] result = new Object[_solutions.length][];

            for (int i = 0; i < _solutions.length; i++)
            {
                int numGenes = _solutions[i].getGenome().getChromosome(0).getLength();
                result[i] = new Integer[numGenes];

                for (int j = 0; j < numGenes; j++)
                {
                    result[i][j] =  _solutions[i].getGenome().getChromosome(0).getGene(j).getValue();
                }
            }
            return result;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    private class GeneticSearchListener implements IAlgorithmListener<GeneticSearchEvent>
    { 
        public void algorithmStarted(GeneticSearchEvent e)
        {
            _algorithmStarted = true;
            _statNumNewSol = _statLastIterNewSol = 0;
        }

        public void algorithmStopped(GeneticSearchEvent e)
        {
            _algorithmStopped = true;
            _statNrOfIterationsDone = e.getGeneticSearch().getCurrentIteration();
            Subject s= e.getGeneticSearch().getBestSubject();
            if(s != null)
                _statEndObjVal = s.getObjectiveValue()[0];
        }
        public void newBestSolutionFound(GeneticSearchEvent e)
        {
            try
            {
                Subject subject = e.getGeneticSearch().getBestSubject();  // e.getGeneticSearch().getSubjects().length - 1
                _bestEverSolution = (Subject)e.getGeneticSearch().getBestSubject().clone();
                _reporter.putNewSolution(System.currentTimeMillis(), e.getGeneticSearch().getCurrentIteration(), subject.getObjectiveValue()[0], subject.toString());
                _statNumNewSol++;
                _statLastIterNewSol = e.getGeneticSearch().getCurrentIteration();

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void noChangeInValueIterationMade(GeneticSearchEvent e)
        {

        }

		@Override
		public void iterationPerformed(GeneticSearchEvent e)
		{
			// TODO Auto-generated method stub
			
		}
    }
}
