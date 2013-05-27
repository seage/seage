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

import java.util.*;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;

/**
 * @author Richard Malek (original)
 */
public class GeneticAlgorithm<GeneType>
{
	private AlgorithmEventProducer<IAlgorithmListener<GeneticAlgorithmEvent<GeneType>>, GeneticAlgorithmEvent<GeneType>> _eventProducer;
	private int _iterationCount;
	private int _currentIteration;
	private int _populationCount;
	private double _randomSubjectsPct;
	private double _mutateSubjectsPct;
	private double _eliteSubjectsPct;

	private boolean _keepSearching;
	private boolean _isRunning;

	private Population<GeneType> _population;
	private GeneticOperator<GeneType> _operator;
	private SubjectEvaluator<GeneType> _evaluator;
	private SubjectComparator<GeneType> _subjectComparator;

	private Subject<GeneType> _bestSubject;

	private Random _random;

	public GeneticAlgorithm(GeneticOperator<GeneType> operator, SubjectEvaluator<GeneType> evaluator)
	{
		_eventProducer = new AlgorithmEventProducer<IAlgorithmListener<GeneticAlgorithmEvent<GeneType>>, GeneticAlgorithmEvent<GeneType>>(new GeneticAlgorithmEvent<GeneType>(this));

		_subjectComparator = new SubjectComparator<GeneType>();
		_operator = operator;
		_evaluator = evaluator;
		_population = new Population<GeneType>();
		_random = new Random();

		_bestSubject = null;

		_keepSearching = _isRunning = false;
	}

	public void addGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<GeneType>> listener)
	{
		_eventProducer.addAlgorithmListener(listener);
	}

	public void removeGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<GeneType>> listener)
	{
		_eventProducer.removeGeneticSearchListener(listener);
	}

	public GeneticOperator<GeneType> getOperator()
	{
		return _operator;
	}

	public List<Subject<GeneType>> getSubjects() throws Exception
	{
		return _population.getSubjects(_populationCount);
	}

	public void stopSolving()
	{
		_keepSearching = false;
	}

	public boolean isRunning()
	{
		return _isRunning;
	}

	public Subject<GeneType> getBestSubject()
	{
		return _bestSubject;
	}

	public void startSearching(List<Subject<GeneType>> subjects) throws Exception
	{
		_keepSearching = _isRunning = true;
		_eventProducer.fireAlgorithmStarted();

		_bestSubject = null;
		_currentIteration = 0;

		Population<GeneType> workPopulation = new Population<GeneType>();
		int numEliteSubject = (int) Math.max(_eliteSubjectsPct * _populationCount, 1);
		int numMutateSubject = (int) (_mutateSubjectsPct * _populationCount);
		int numCrossSubject = _populationCount - numEliteSubject - numMutateSubject;
		int numRandomSubject = (int) (_randomSubjectsPct * _populationCount);

		_population.removeAll();
		for (int i = 0; i < _populationCount; i++)
		{
			if (i < subjects.size())
				_population.addSubject(subjects.get(i));
			else
				break;
		}

		//double currBestFitness = Double.MAX_VALUE;
		int i = 0;
		while (i++ < _iterationCount && _keepSearching)
		{
			_currentIteration++;

			_evaluator.evaluateSubjects(_population.getSubjects());
			_population.sort(_subjectComparator);
			// _population.removeTwins();

			if (_bestSubject == null)
			{
				_bestSubject = _population.getBestSubject().clone();
				_eventProducer.fireNewBestSolutionFound();
			}

			if (_subjectComparator.compare(_population.getBestSubject(), _bestSubject) == -1)
			{
				_bestSubject = _population.getBestSubject().clone();
				_eventProducer.fireNewBestSolutionFound();
			}
			else
				;// fireNoChangeInValueIterationMade();

			workPopulation.removeAll();
			// elitism
			workPopulation.mergePopulation(elitism(numEliteSubject));
			// crossover
			workPopulation.mergePopulation(crossover(numCrossSubject));
			// mutate
			workPopulation.mergePopulation(mutate(numMutateSubject));
			// randoms
			workPopulation.mergePopulation(randomize(numRandomSubject));

			_population.removeAll();
			_population.mergePopulation(workPopulation);

			if (_population.getSize() < _populationCount)
				_population.mergePopulation(randomize(_populationCount - _population.getSize()));

			if (_population.getSize() > _populationCount)
				_population.resize(_populationCount);

		}
		_evaluator.evaluateSubjects(_population.getSubjects());
		_population.sort(_subjectComparator);
		_isRunning = false;
		_eventProducer.fireAlgorithmStopped();
	}

	private Population<GeneType> elitism(int numEliteSubject) throws Exception
	{

		Population<GeneType> result = new Population<GeneType>();
		Subject<GeneType> prev = _population.getBestSubject();
		for (int i = 0; i < numEliteSubject; i++)
		{
			Subject<GeneType> curr = _population.getSubject(i);
			if (i == 0 || curr.hashCode() != prev.hashCode())
			{
				prev = curr;
				result.addSubject(curr);
			}
		}
		return result;

	}

	private Population<GeneType> crossover(int numCrossSubject) throws Exception
	{
		ArrayList<Subject<GeneType>> subjects = new ArrayList<Subject<GeneType>>(_population.getSubjects());
		Population<GeneType> resultPopulation = new Population<GeneType>();
		if (numCrossSubject % 2 == 1)
			numCrossSubject++;
		for (int i = 0; i < numCrossSubject / 2; i++)
		{
			int ix[] = _operator.select(subjects);

			int timeOut = 100;
			while (subjects.get(ix[0]).hashCode() == subjects.get(ix[1]).hashCode() && timeOut-- > 0)
			{
				ix = _operator.select(subjects);
			}
			List<Subject<GeneType>> children = _operator.crossOver(subjects.get(ix[0]), subjects.get(ix[1]));			

			resultPopulation.addSubject(_operator.mutate(children.get(0)));
			resultPopulation.addSubject(_operator.mutate(children.get(1)));

		}
		return resultPopulation;

	}

	private Population<GeneType> mutate(int numMutateSubject) throws Exception
	{		
		List<Subject<GeneType>> subjects = new ArrayList<Subject<GeneType>>(_population.getSubjects());
		Population<GeneType> result = new Population<GeneType>();

		for (int i = 1; i < subjects.size(); i++)
		{
			if ((1.0 * numMutateSubject / subjects.size()) > _random.nextDouble())
			{
				result.addSubject(_operator.mutate(subjects.get(i)));
			}
		}
		return result;		
	}

	private Population<GeneType> randomize(int numRandomSubject) throws Exception
	{		
		List<Subject<GeneType>> subjects = _population.getSubjects();
		Population<GeneType> result = new Population<GeneType>();

		for (int i = 0; i < subjects.size(); i++)
		{
			if ((1.0 * numRandomSubject/ subjects.size()) > _random.nextDouble())
			{
				result.addSubject(_operator.randomize(subjects.get(i).clone()));
			}
		}
		return result;

	}

	// setters / getters

	public void setIterationToGo(int iter)
	{
		_iterationCount = iter;
	}

	public int getIterationToGo()
	{
		return _iterationCount;
	}

	public int getCurrentIteration()
	{
		return _currentIteration;
	}

	public void setPopulationCount(int count)
	{
		_populationCount = count;
	}
	
	public int getPopulationCount()
	{
		return _populationCount;
	}

	public void setRandomSubjectsPct(double pct)
	{
		_randomSubjectsPct = pct;
	}

	public void setMutatePopulationPct(double pct)
	{
		_mutateSubjectsPct = pct;
	}

	public void setEliteSubjectsPct(double pct)
	{
		_eliteSubjectsPct = pct;
	}

	// ----------------------------------------------------

	public void setCrossLengthPct(double crossLengthPct)
	{
		_operator.setCrossLengthPct(crossLengthPct);
	}

	public void setMutateChromosomeLengthPct(double pct)
	{
		_operator.setMutateLengthPct(pct);

	}
}
