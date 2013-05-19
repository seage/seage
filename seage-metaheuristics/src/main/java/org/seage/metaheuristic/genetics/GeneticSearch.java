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
public class GeneticSearch
{
	private AlgorithmEventProducer<IAlgorithmListener<GeneticSearchEvent>, GeneticSearchEvent> _eventProducer;
	private int _iterationCount;
	private int _currentIteration;
	private int _populationCount;
	private double _randomSubjectsPct;
	private double _mutateSubjectsPct;
	private double _eliteSubjectsPct;
	//private double _crossLengthPct;

	private boolean _keepSearching;
	private boolean _isRunning;

	private Population _population;
	private GeneticOperator _operator;
	private Evaluator _evaluator;

	private Subject _bestSubject;
	private SubjectComparator _subjectComparator;

	private Random _random;

	public GeneticSearch(GeneticOperator operator, Evaluator evaluator)
	{
		_eventProducer = new AlgorithmEventProducer<IAlgorithmListener<GeneticSearchEvent>, GeneticSearchEvent>(new GeneticSearchEvent(this));

		_operator = operator;
		_evaluator = evaluator;
		_population = new Population();
		_random = new Random();

		_bestSubject = null;
		_subjectComparator = new SubjectComparator();

		_keepSearching = _isRunning = false;
	}

	public void addGeneticSearchListener(IAlgorithmListener<GeneticSearchEvent> listener)
	{
		_eventProducer.addAlgorithmListener(listener);
	}

	public void removeGeneticSearchListener(IAlgorithmListener<GeneticSearchEvent> listener)
	{
		_eventProducer.removeGeneticSearchListener(listener);
	}

	public GeneticOperator getOperator()
	{
		return _operator;
	}

	public Subject[] getSubjects() throws Exception
	{
		try
		{
			return _population.getSubjects(_populationCount);
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public void stopSolving()
	{
		_keepSearching = false;
	}

	public boolean isRunning()
	{
		return _isRunning;
	}

	public Subject getBestSubject()
	{
		return _bestSubject;
	}

	public void startSearching(Subject[] subjects) throws Exception
	{
		try
		{
			_keepSearching = _isRunning = true;
			_eventProducer.fireAlgorithmStarted();

			_bestSubject = null;
			_currentIteration = 0;

			Population workPopulation = new Population();
			int numEliteSubject = (int) Math.max(_eliteSubjectsPct * _populationCount, 1);
			int numMutateSubject = (int) (_mutateSubjectsPct * _populationCount);
			int numCrossSubject = _populationCount - numEliteSubject - numMutateSubject;
			int numRandomSubject = (int) (_randomSubjectsPct * _populationCount);

			
			_population.removeAll();
			for (int i = 0; i < _populationCount; i++)
			{
				if (i < subjects.length)
					_population.addSubject(subjects[i]);
				else
					break;
			}

			double currBestFitness = Double.MAX_VALUE;			
			int i = 0;
			while (i++ < _iterationCount && _keepSearching)
			{
				_currentIteration++;

				evaluatePopulation(_population);
				// _population.removeTwins();
				
				if(_bestSubject == null)
				{
					_bestSubject = (Subject) _population.getBestSubject().clone();
				}
				
				if (_subjectComparator.compare(_population.getBestSubject(), _bestSubject) == -1)
				{
					_bestSubject = (Subject) _population.getBestSubject().clone();
					_eventProducer.fireNewBestSolutionFound();
				}
				else
					;// fireNoChangeInValueIterationMade();

				// check a worse solution than previous
				currBestFitness = _evaluator.evaluate(_population.getBestSubject())[0];// .getObjectiveValue()[0];
				if (currBestFitness != _population.getBestSubject().getFitness()[0]) // SOLVED
					throw new Exception("Fitness function failed");
				if (_bestSubject.getFitness()[0] < currBestFitness && numEliteSubject > 0)
					throw new Exception("Elitism failed");
				// prevFitness = currFitness;

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
			evaluatePopulation(_population);
			_isRunning = false;
			_eventProducer.fireAlgorithmStopped();
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	private void evaluatePopulation(Population population) throws Exception
	{
		try
		{
			for (int i = 0; i < population.getSize(); i++)
			{
				Subject subject = population.getSubject(i);
				subject.setObjectiveValue(_evaluator.evaluate(subject));
			}
			Collections.sort(population.getList(), _subjectComparator);
		}
		catch (Exception ex)
		{
			throw ex;
		}

	}

	private Population elitism(int numEliteSubject) throws Exception
	{
		try
		{
			Population result = new Population();
			Subject prev = _population.getBestSubject();
			for (int i = 0; i < numEliteSubject; i++)
			{
				Subject curr = _population.getSubject(i);
				if (i == 0 || curr.hashCode() != prev.hashCode())
				{
					prev = curr;
					result.addSubject(curr);
				}
			}
			return result;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	private Population crossover(int numCrossSubject) throws Exception
	{
		try
		{
			Subject[] subjects = _population.getSubjects();
			Population result = new Population();
			if (numCrossSubject % 2 == 1)
				numCrossSubject++;
			for (int i = 0; i < numCrossSubject / 2; i++)
			{
				int ix[] = _operator.select(subjects);

				int timeOut = 100;
				while (subjects[ix[0]].hashCode() == subjects[ix[1]].hashCode() && timeOut-- > 0)
				{
					ix = _operator.select(subjects);
					// if (subjects[ix[0]].hashCode() !=
					// subjects[ix[1]].hashCode())
					// System.out.println(" -> OK");
				}
				Subject[] childs = null;
				if (_random.nextDouble() < 0.90)
				{
					childs = _operator.crossOver(subjects[ix[0]], subjects[ix[1]]);
				}
				else
				{
					childs = new Subject[] { (Subject) subjects[ix[0]].clone(), (Subject) subjects[ix[1]].clone() };
				}
				Subject child0 = _operator.mutate(childs[0]);
				Subject child1 = _operator.mutate(childs[1]);

				result.addSubject(child0);
				result.addSubject(child1);

				// result.add(childs[0]);
				// result.add(childs[1]);
			}
			return result;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	private Population mutate(int numMutateSubject) throws Exception
	{
		try
		{
			Subject[] subjects = _population.getSubjects();
			Population result = new Population();

			for (int i = 1; i < subjects.length; i++)
			{
				if ((1.0 * numMutateSubject / subjects.length) > _random.nextDouble())
				{
					// int ix = _random.nextInt(subjects.length);
					result.addSubject(_operator.mutate(subjects[i]));
				}
			}
			return result;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	private Population randomize(int numRandomSubject) throws Exception
	{
		try
		{
			Subject[] subjects = _population.getSubjects();
			Population result = new Population();

			for (int i = 0; i < numRandomSubject; i++)
			{
				int ix = _random.nextInt(subjects.length);
				// if(ix == 0)
				// System.out.println("POZOR 0 ***********************************************************************");
				result.addSubject(_operator.randomize((Subject) subjects[ix].clone()));
			}
			return result;
		}
		catch (Exception ex)
		{
			throw ex;
		}
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

	//----------------------------------------------------
	
	public void setCrossLengthPct(double crossLengthPct)
	{
		_operator.setCrossLengthPct(crossLengthPct);
	}
	
	public void setMutateChromosomeLengthPct(double pct)
	{
		_operator.setMutateLengthPct(pct);
		
	}
}
