/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.seage.metaheuristic.AlgorithmEventProducer;
import org.seage.metaheuristic.IAlgorithmListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeneticAlgorithm implementation.
 * 
 * @author Richard Malek (original)
 */
public class GeneticAlgorithm<S extends Subject<?>> {
  protected static Logger _logger = LoggerFactory.getLogger(GeneticAlgorithm.class.getName());

  private AlgorithmEventProducer<IAlgorithmListener<GeneticAlgorithmEvent<S>>, GeneticAlgorithmEvent<S>> eventProducer;
  private int iterationCount;
  private int currentIteration;
  private int populationCount;
  private double randomSubjectsCoef;
  private double mutateSubjectsCoef;
  private double eliteSubjectsCoef;

  private boolean keepSearching;
  private boolean isRunning;

  private Population<S> population;
  private GeneticOperator<S> operator;
  private SubjectEvaluator<S> evaluator;
  private SubjectComparator<S> subjectComparator;

  private S bestSubject;

  private Random random;

  /**
   * GeneticAlgorithm constructor.
   * 
   * @param operator  .
   * @param evaluator .
   */
  public GeneticAlgorithm(GeneticOperator<S> operator, SubjectEvaluator<S> evaluator) {
    this.subjectComparator = new SubjectComparator<S>();
    this.operator = operator;
    this.evaluator = evaluator;
    this.population = new Population<S>();
    this.random = new Random();
    this.bestSubject = null;
    this.keepSearching = isRunning = false;

    this.eventProducer =
        new AlgorithmEventProducer
            <IAlgorithmListener<GeneticAlgorithmEvent<S>>, GeneticAlgorithmEvent<S>>(
                new GeneticAlgorithmEvent<S>(this));
  }

  public void addGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<S>> listener) {
    eventProducer.addAlgorithmListener(listener);
  }

  public void removeGeneticSearchListener(IAlgorithmListener<GeneticAlgorithmEvent<S>> listener) {
    eventProducer.removeGeneticSearchListener(listener);
  }

  public GeneticOperator<S> getOperator() {
    return operator;
  }

  public List<S> getSubjects() throws Exception {
    return population.getSubjects(populationCount);
  }

  public void stopSolving() {
    keepSearching = false;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public S getBestSubject() {
    return bestSubject;
  }

  /** . */
  @SuppressWarnings("unchecked")
  public void startSearching(List<S> subjects) throws Exception {
    try {
      keepSearching = isRunning = true;
      eventProducer.fireAlgorithmStarted();

      if (subjects.size() == 0) {
        throw new IllegalArgumentException("No subject entered for the evolution.");
      }

      bestSubject = null;
      currentIteration = 0;

      Population<S> workPopulation = new Population<S>();
      int numEliteSubject = (int) Math.max(eliteSubjectsCoef * populationCount, 1);
      int numMutateSubject = (int) (mutateSubjectsCoef * populationCount);
      int numCrossSubject = populationCount - numEliteSubject - numMutateSubject;
      int numRandomSubject = (int) (randomSubjectsCoef * populationCount);

      population.removeAll();
      for (int i = 0; i < populationCount; i++) {
        if (i < subjects.size()) {
          population.addSubject(subjects.get(i));
        } else {
          break;
        }
      }
      if (population.getSize() < populationCount) {
        population.mergePopulation(createRandomSubjects(populationCount - population.getSize()));
      }

      // double currBestFitness = Double.MAX_VALUE;
      int i = 0;
      while (i++ < iterationCount && keepSearching) {
        currentIteration++;

        evaluator.evaluateSubjects(population.getSubjects());
        population.sort(subjectComparator);
        // _population.removeTwins();

        if (bestSubject == null) {
          bestSubject = (S) population.getBestSubject().clone();
          eventProducer.fireNewBestSolutionFound();
        }

        if (subjectComparator.compare(population.getBestSubject(), bestSubject) == -1) {
          bestSubject = (S) population.getBestSubject().clone();
          eventProducer.fireNewBestSolutionFound();
        }
        // else
        // fireNoChangeInValueIterationMade();

        workPopulation.removeAll();
        // elitism
        workPopulation.mergePopulation(elitism(numEliteSubject));
        // crossover
        workPopulation.mergePopulation(crossover(numCrossSubject));
        // mutate
        workPopulation.mergePopulation(mutate(numMutateSubject));
        // randoms
        workPopulation.mergePopulation(randomize(numRandomSubject));

        population.removeAll();
        population.mergePopulation(workPopulation);

        if (population.getSize() < populationCount) {
          population.mergePopulation(createRandomSubjects(populationCount - population.getSize()));
        }

        if (population.getSize() > populationCount) {
          population.resize(populationCount);
        }

        if (population.getSize() != populationCount) {
          throw new IllegalArgumentException("The new population has a wrong size.");
        }

        eventProducer.fireIterationPerformed();
      }
      evaluator.evaluateSubjects(population.getSubjects());
      population.sort(subjectComparator);
      if (subjectComparator.compare(population.getBestSubject(), bestSubject) == -1) {
        bestSubject = (S) population.getBestSubject().clone();
        eventProducer.fireNewBestSolutionFound();
      }
    } finally {
      isRunning = false;
      eventProducer.fireAlgorithmStopped();
    }
  }

  private Population<S> elitism(int numEliteSubject) throws Exception {
    Population<S> result = new Population<S>();
    S prev = population.getBestSubject();
    for (int i = 0; i < numEliteSubject; i++) {
      S curr = population.getSubject(i);
      if (i == 0 || curr.hashCode() != prev.hashCode()) {
        prev = curr;
        result.addSubject(curr);
      }
    }
    return result;
  }

  private Population<S> crossover(int numCrossSubject) throws Exception {
    ArrayList<S> subjects = new ArrayList<S>(population.getSubjects());
    Population<S> resultPopulation = new Population<S>();
    if ((numCrossSubject & 1) == 1) {
      numCrossSubject++;
    }
    for (int i = 0; i < numCrossSubject / 2; i++) {
      int[] ix = operator.select(subjects);

      int timeOut = 100;
      while (subjects.get(ix[0]).hashCode() == subjects.get(ix[1]).hashCode() && timeOut-- > 0) {
        ix = operator.select(subjects);
      }
      List<S> children = operator.crossOver(subjects.get(ix[0]), subjects.get(ix[1]));

      resultPopulation.addSubject(operator.mutate(children.get(0)));
      resultPopulation.addSubject(operator.mutate(children.get(1)));

    }
    return resultPopulation;
  }

  private Population<S> mutate(int numMutateSubject) throws Exception {
    List<S> subjects = new ArrayList<S>(population.getSubjects());
    Population<S> result = new Population<S>();

    for (int i = 1; i < subjects.size(); i++) {
      if ((1.0 * numMutateSubject / subjects.size()) > random.nextDouble()) {
        result.addSubject(operator.mutate(subjects.get(i)));
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private Population<S> randomize(int numRandomSubject) throws Exception {
    List<S> subjects = population.getSubjects();
    Population<S> result = new Population<S>();

    for (int i = 0; i < subjects.size(); i++) {
      if ((1.0 * numRandomSubject / subjects.size()) > random.nextDouble()) {
        result.addSubject(operator.randomize((S) subjects.get(i).clone()));
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private Population<S> createRandomSubjects(int numRandomSubject) throws Exception {
    Population<S> result = new Population<S>();

    for (int i = 0; i < numRandomSubject; i++) {
      result.addSubject(operator.randomize(
          (S) population.getSubjects().get(i % population.getSubjects().size()).clone()));
    }
    return result;
  }

  // setters / getters

  public void setIterationToGo(int iter) {
    iterationCount = iter;
  }

  public int getIterationToGo() {
    return iterationCount;
  }

  public int getCurrentIteration() {
    return currentIteration;
  }

  public void setPopulationCount(int count) {
    populationCount = count;
  }

  public int getPopulationCount() {
    return populationCount;
  }

  public void setRandomSubjectsPct(double pct) {
    randomSubjectsCoef = pct / 100.0;
  }

  public void setMutatePopulationPct(double pct) {
    mutateSubjectsCoef = pct / 100.0;
  }

  public void setEliteSubjectsPct(double pct) {
    eliteSubjectsCoef = pct / 100.0;
  }

  // ----------------------------------------------------

  public void setCrossLengthPct(double crossLengthPct) {
    operator.setCrossLengthCoef(crossLengthPct / 100.0);
  }

  public void setMutateChromosomeLengthPct(double pct) {
    operator.setMutateLengthCoef(pct / 100.0);
  }
}
