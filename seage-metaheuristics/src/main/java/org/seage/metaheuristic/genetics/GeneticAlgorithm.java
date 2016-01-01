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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;

/**
 * @author Richard Malek (original)
 */
public class GeneticAlgorithm<S extends Subject<?>>
{
    protected static Logger _logger = LoggerFactory.getLogger(GeneticAlgorithm.class.getName());

    private AlgorithmEventProducer<IAlgorithmListener<GeneticAlgorithmEvent<S>>, GeneticAlgorithmEvent<S>> _eventProducer;
    private int _iterationCount;
    private int _currentIteration;
    private int _populationCount;
    private double _randomSubjectsCoef;
    private double _mutateSubjectsCoef;
    private double _eliteSubjectsCoef;

    private boolean _keepSearching;
    private boolean _isRunning;

    private Population<S> _population;
    private GeneticOperator<S> _operator;
    private SubjectEvaluator<S> _evaluator;
    private SubjectComparator<S> _subjectComparator;

    private S _bestSubject;

    private Random _random;

    public GeneticAlgorithm(GeneticOperator<S> operator, SubjectEvaluator<S> evaluator)
    {
        _eventProducer = new AlgorithmEventProducer<IAlgorithmListener<GeneticAlgorithmEvent<S>>, GeneticAlgorithmEvent<S>>(
                new GeneticAlgorithmEvent<S>(this));

        _subjectComparator = new SubjectComparator<S>();
        _operator = operator;
        _evaluator = evaluator;
        _population = new Population<S>();
        _random = new Random();

        _bestSubject = null;

        _keepSearching = _isRunning = false;
    }

    public void addGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<S>> listener)
    {
        _eventProducer.addAlgorithmListener(listener);
    }

    public void removeGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<S>> listener)
    {
        _eventProducer.removeGeneticSearchListener(listener);
    }

    public GeneticOperator<S> getOperator()
    {
        return _operator;
    }

    public List<S> getSubjects() throws Exception
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

    public S getBestSubject()
    {
        return _bestSubject;
    }

    @SuppressWarnings("unchecked")
    public void startSearching(List<S> subjects) throws Exception
    {
        try
        {
            _keepSearching = _isRunning = true;
            _eventProducer.fireAlgorithmStarted();

            if (subjects.size() == 0)
                throw new Exception("No subject entered for the evolution.");

            _bestSubject = null;
            _currentIteration = 0;

            Population<S> workPopulation = new Population<S>();
            int numEliteSubject = (int) Math.max(_eliteSubjectsCoef * _populationCount, 1);
            int numMutateSubject = (int) (_mutateSubjectsCoef * _populationCount);
            int numCrossSubject = _populationCount - numEliteSubject - numMutateSubject;
            int numRandomSubject = (int) (_randomSubjectsCoef * _populationCount);

            _population.removeAll();
            for (int i = 0; i < _populationCount; i++)
            {
                if (i < subjects.size())
                    _population.addSubject(subjects.get(i));
                else
                    break;
            }
            if (_population.getSize() < _populationCount)
                _population.mergePopulation(createRandomSubjects(_populationCount - _population.getSize()));

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
                    _bestSubject = (S) _population.getBestSubject().clone();
                    _eventProducer.fireNewBestSolutionFound();
                }

                if (_subjectComparator.compare(_population.getBestSubject(), _bestSubject) == -1)
                {
                    _bestSubject = (S) _population.getBestSubject().clone();
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
                    _population.mergePopulation(createRandomSubjects(_populationCount - _population.getSize()));

                if (_population.getSize() > _populationCount)
                    _population.resize(_populationCount);

                if (_population.getSize() != _populationCount)
                    throw new Exception("The new population has a wrong size.");

                _eventProducer.fireIterationPerformed();
            }
            _evaluator.evaluateSubjects(_population.getSubjects());
            _population.sort(_subjectComparator);
        }
        finally
        {
            _isRunning = false;
            _eventProducer.fireAlgorithmStopped();
        }
    }

    private Population<S> elitism(int numEliteSubject) throws Exception
    {
        Population<S> result = new Population<S>();
        S prev = _population.getBestSubject();
        for (int i = 0; i < numEliteSubject; i++)
        {
            S curr = _population.getSubject(i);
            if (i == 0 || curr.hashCode() != prev.hashCode())
            {
                prev = curr;
                result.addSubject(curr);
            }
        }
        return result;
    }

    private Population<S> crossover(int numCrossSubject) throws Exception
    {
        ArrayList<S> subjects = new ArrayList<S>(_population.getSubjects());
        Population<S> resultPopulation = new Population<S>();
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
            List<S> children = _operator.crossOver(subjects.get(ix[0]), subjects.get(ix[1]));

            resultPopulation.addSubject(_operator.mutate(children.get(0)));
            resultPopulation.addSubject(_operator.mutate(children.get(1)));

        }
        return resultPopulation;
    }

    private Population<S> mutate(int numMutateSubject) throws Exception
    {
        List<S> subjects = new ArrayList<S>(_population.getSubjects());
        Population<S> result = new Population<S>();

        for (int i = 1; i < subjects.size(); i++)
        {
            if ((1.0 * numMutateSubject / subjects.size()) > _random.nextDouble())
            {
                result.addSubject(_operator.mutate(subjects.get(i)));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Population<S> randomize(int numRandomSubject) throws Exception
    {
        List<S> subjects = _population.getSubjects();
        Population<S> result = new Population<S>();

        for (int i = 0; i < subjects.size(); i++)
        {
            if ((1.0 * numRandomSubject / subjects.size()) > _random.nextDouble())
            {
                result.addSubject(_operator.randomize((S) subjects.get(i).clone()));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Population<S> createRandomSubjects(int numRandomSubject) throws Exception
    {
        Population<S> result = new Population<S>();

        for (int i = 0; i < numRandomSubject; i++)
        {
            result.addSubject(_operator
                    .randomize((S) _population.getSubjects().get(i % _population.getSubjects().size()).clone()));
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
        _randomSubjectsCoef = pct / 100.0;
    }

    public void setMutatePopulationPct(double pct)
    {
        _mutateSubjectsCoef = pct / 100.0;
    }

    public void setEliteSubjectsPct(double pct)
    {
        _eliteSubjectsCoef = pct / 100.0;
    }

    // ----------------------------------------------------

    public void setCrossLengthPct(double crossLengthPct)
    {
        _operator.setCrossLengthCoef(crossLengthPct / 100.0);
    }

    public void setMutateChromosomeLengthPct(double pct)
    {
        _operator.setMutateLengthCoef(pct / 100.0);

    }
}
