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

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectComparator;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GeneticSearchAdapter class.
 */
@AlgorithmParameters({ @Parameter(name = "crossLengthPct", min = 0, max = 100, init = 10),
    @Parameter(name = "eliteSubjectPct", min = 0, max = 100, init = 10),
    @Parameter(name = "iterationCount", min = 100, max = 1000000, init = 1000000),
    @Parameter(name = "mutateLengthPct", min = 0, max = 100, init = 10),
    @Parameter(name = "mutateSubjectPct", min = 0, max = 100, init = 10),
    @Parameter(name = "numSolutions", min = 10, max = 1000, init = 100),
    @Parameter(name = "randomSubjectPct", min = 0, max = 100, init = 10) })
public abstract class GeneticAlgorithmAdapter<P extends Phenotype<?>, S extends Subject<?>>
    extends AlgorithmAdapterImpl<P, S> {

  private static final Logger logger = 
      LoggerFactory.getLogger(GeneticAlgorithmAdapter.class.getName());
  
  protected List<S> solutions;
  protected GeneticAlgorithm<S> geneticAlgorithm;
  protected SubjectEvaluator<S> evaluator;
  protected SubjectComparator<S> comparator;
  private GeneticAlgorithmListener algorithmListener;
  protected S bestEverSolution;
  protected AlgorithmParams params;

  private double statInitObjVal;
  private double statBestObjVal;
  private int statNrOfIterationsDone;
  private int statNumNewSol;
  private int statLastImprovingIteration;

  /** GeneticAlgorithmAdapter. */
  protected GeneticAlgorithmAdapter(GeneticOperator<S> operator, SubjectEvaluator<S> evaluator,
      IPhenotypeEvaluator<P> phenotypeEvaluator, boolean maximizing) {
    super(phenotypeEvaluator);
    this.evaluator = evaluator;
    
    this.algorithmListener = new GeneticAlgorithmListener();
    this.comparator = new SubjectComparator<>();
    this.geneticAlgorithm = new GeneticAlgorithm<>(operator, evaluator);
    this.geneticAlgorithm.addGeneticSearchListener(algorithmListener);
  }

  @Override
  public void startSearching(final AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

    reporter = new AlgorithmReporter<>(phenotypeEvaluator);
    reporter.putParameters(params);

    geneticAlgorithm.startSearching(this.solutions);

    this.solutions = geneticAlgorithm.getSubjects();
    if (this.solutions == null) {
      throw new RuntimeException("Solutions null");
    }
  }

  @Override
  public boolean isRunning() {
    return geneticAlgorithm.isRunning();
  }

  @Override
  public void stopSearching() throws Exception {
    geneticAlgorithm.stopSolving();

    while (isRunning()) {
      Thread.sleep(100);
    }
  }

  /**
   * Sets algorithm parameters.
   */
  public void setParameters(AlgorithmParams newParams) throws Exception {
    this.params = newParams;

    geneticAlgorithm.getOperator().setCrossLengthCoef(
        params.getValueDouble("crossLengthPct") / 100);
    geneticAlgorithm.getOperator().setMutateLengthCoef(
        params.getValueDouble("mutateLengthPct") / 100);
    geneticAlgorithm.setEliteSubjectsPct(params.getValueDouble("eliteSubjectPct"));
    geneticAlgorithm.setIterationToGo(params.getValueInt("iterationCount"));
    geneticAlgorithm.setMutatePopulationPct(params.getValueDouble("mutateSubjectPct"));
    geneticAlgorithm.setPopulationCount(params.getValueInt("numSolutions"));
    geneticAlgorithm.setRandomSubjectsPct(params.getValueDouble("randomSubjectPct"));
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    int num = this.solutions.size();
    double avg = 0;
    for (int i = 0; i < num; i++) {
      avg += this.solutions.get(i).getObjectiveValue()[0];
    }
    avg /= num;

    reporter.putStatistics(
        statNrOfIterationsDone, 
        statNumNewSol, 
        statLastImprovingIteration, 
        statInitObjVal, 
        avg,
        statBestObjVal);

    return reporter.getReport();
  }

  private class GeneticAlgorithmListener implements IAlgorithmListener<GeneticAlgorithmEvent<S>> {
    @Override
    public void algorithmStarted(GeneticAlgorithmEvent<S> e) {
      algorithmStarted = true;
      statNumNewSol = statLastImprovingIteration = 0;
    }

    @Override
    public void algorithmStopped(GeneticAlgorithmEvent<S> e) {
      algorithmStopped = true;
      statNrOfIterationsDone = e.getGeneticSearch().getCurrentIteration();
      S s = e.getGeneticSearch().getBestSubject();
      if (s != null) {
        statBestObjVal = s.getObjectiveValue()[0];
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void newBestSolutionFound(GeneticAlgorithmEvent<S> e) {
      try {
        GeneticAlgorithm<S> gs = e.getGeneticSearch();
        S subject = gs.getBestSubject();
        if (statNumNewSol == 0) {
          statInitObjVal = subject.getObjectiveValue()[0];
        }

        bestEverSolution = (S) gs.getBestSubject().clone();
        reporter.putNewSolution(
            System.currentTimeMillis(), 
            gs.getCurrentIteration(), 
            solutionToPhenotype(subject));
        statNumNewSol++;
        statLastImprovingIteration = gs.getCurrentIteration();

      } catch (Exception ex) {
        logger.warn(ex.getMessage(), ex);
      }
    }

    @Override
    public void noChangeInValueIterationMade(GeneticAlgorithmEvent<S> e) {
      // No implementation for this yet
    }

    @Override
    public void iterationPerformed(GeneticAlgorithmEvent<S> e) {
      // No implementation for this yet
    }
  }
}
