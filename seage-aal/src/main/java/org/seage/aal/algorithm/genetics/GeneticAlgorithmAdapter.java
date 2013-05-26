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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.GeneticSearch;
import org.seage.metaheuristic.genetics.GeneticSearchEvent;
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
public class GeneticAlgorithmAdapter<GeneType> extends AlgorithmAdapterImpl
{

	protected List<Subject<GeneType>> _solutions;
	private GeneticSearch<GeneType> _geneticSearch;
	private SubjectEvaluator<GeneType> _evaluator;
	private SubjectComparator<GeneType> _comparator;
	private GeneticSearchListener _algorithmListener;
	private Subject<GeneType> _bestEverSolution;
	private AlgorithmParams _params;
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

	public GeneticAlgorithmAdapter(GeneticOperator<GeneType> operator, SubjectEvaluator<GeneType> evaluator, boolean maximizing, String searchID)
	{
		_evaluator = evaluator;
		_algorithmListener = new GeneticSearchListener();
		_comparator = new SubjectComparator<GeneType>();
		_geneticSearch = new GeneticSearch<GeneType>(operator, evaluator);
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
		_params.putValue("id", "GeneticAlgorithm");

		DataNode p = params.getDataNode("Parameters");
		_geneticSearch.getOperator().setCrossLengthPct(p.getValueInt("crossLengthPct") / 100.0);
		_geneticSearch.getOperator().setMutateLengthPct(p.getValueInt("mutateLengthPct") / 100.0);
		_geneticSearch.setEliteSubjectsPct(p.getValueInt("eliteSubjectPct") / 100.0);
		_geneticSearch.setIterationToGo(p.getValueInt("iterationCount"));
		// _statNumIter = p.getValueInt("iterationCount");
		_geneticSearch.setMutatePopulationPct(p.getValueInt("mutateSubjectPct") / 100.0);
		_geneticSearch.setPopulationCount(p.getValueInt("numSolutions"));
		_geneticSearch.setRandomSubjectsPct(p.getValueInt("randomSubjectPct") / 100.0);

		// _paramID = param.getValue("ID");
	}

	private void setBestEverSolution() throws Exception
	{
		_evaluator.evaluateSubjects(_solutions);
		Collections.sort(_solutions, _comparator);
		_bestEverSolution = _solutions.get(0);
		_statInitObjVal = _bestEverSolution.getObjectiveValue()[0];

	}

	public AlgorithmReport getReport() throws Exception
	{
		setBestEverSolution();
		int num = _solutions.size();// > 10 ? 10 : solutions.length;
		double avg = 0;
		for (int i = 0; i < num; i++)
			avg += _solutions.get(i).getObjectiveValue()[0];
		avg /= num;

		_reporter.putStatistics(_statNrOfIterationsDone, _statNumNewSol, _statLastImprovingIteration, _statInitObjVal, avg, _statBestObjVal);

		return _reporter.getReport();
	}

	@SuppressWarnings("unchecked")
	public void solutionsFromPhenotype(Object[][] source) throws Exception
	{
		int numSolutions = source.length;

		//_solutions = (Subject<GeneType>[]) new Object[numSolutions];
		_solutions = new ArrayList<Subject<GeneType>>(numSolutions);//(Subject<GeneType>[]) Array.newInstance(Subject.class, numSolutions);

		for (int i = 0; i < numSolutions; i++)
		{
			_solutions.add( new Subject<GeneType>((GeneType[]) source[i]));
			// for (int j = 0; j < source[i].length; j++)
			// {
			// int geneValue = ((Integer)source[i][j]).intValue();
			// _solutions[i].getChromosome().getGene(j).setValue(geneValue);
			// }
		}
	}

	// Returns solutions in best-first order
	public Object[][] solutionsToPhenotype() throws Exception
	{
		_evaluator.evaluateSubjects(_solutions);
		Collections.sort(_solutions, _comparator);

		Object[][] result = new Object[_solutions.size()][];

		for (int i = 0; i < _solutions.size(); i++)
		{
			int numGenes = _solutions.get(i).getChromosome().getLength();
			result[i] = Arrays.copyOf(_solutions.get(i).getChromosome().getGenes(), numGenes);

			// for (int j = 0; j < numGenes; j++)
			// {
			// result[i][j] =
			// _solutions[i].getChromosome().getGene(j).getValue();
			// }
		}
		return result;

	}

	private class GeneticSearchListener implements IAlgorithmListener<GeneticSearchEvent<GeneType>>
	{
		public void algorithmStarted(GeneticSearchEvent<GeneType> e)
		{
			_algorithmStarted = true;
			_statNumNewSol = _statLastImprovingIteration = 0;
		}

		public void algorithmStopped(GeneticSearchEvent<GeneType> e)
		{
			_algorithmStopped = true;
			_statNrOfIterationsDone = e.getGeneticSearch().getCurrentIteration();
			Subject<GeneType> s = e.getGeneticSearch().getBestSubject();
			if (s != null)
				_statBestObjVal = s.getObjectiveValue()[0];
		}

		public void newBestSolutionFound(GeneticSearchEvent<GeneType> e)
		{
			try
			{
				GeneticSearch<GeneType> gs = e.getGeneticSearch();
				Subject<GeneType> subject = gs.getBestSubject();
				if(_statNumNewSol==0)
					_statInitObjVal = subject.getObjectiveValue()[0];
				 
				_bestEverSolution = gs.getBestSubject().clone();
				_reporter.putNewSolution(System.currentTimeMillis(), gs.getCurrentIteration(), subject.getObjectiveValue()[0], subject.toString());
				_statNumNewSol++;
				_statLastImprovingIteration = gs.getCurrentIteration();

			}
			catch (Exception ex)
			{
				_logger.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		public void noChangeInValueIterationMade(GeneticSearchEvent<GeneType> e)
		{

		}

		@Override
		public void iterationPerformed(GeneticSearchEvent<GeneType> e)
		{
			// TODO Auto-generated method stub

		}
	}
}
