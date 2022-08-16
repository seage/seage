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

package org.seage.problem.jsp.genetics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.data.DataNode;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *.
 * @author Richard Malek
 * @author Edited by David Omrai
 */
public class JspGeneticAlgorithmTest 
    implements IAlgorithmListener<GeneticAlgorithmEvent<Subject<Integer>>> {
  private Random generator = new Random();

  /**
   * .
   * @param args Input arguments.
   */
  public static void main(String[] args) {
    try {
      String instanceID = "ft10"; 
      String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(
          instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JspJobsDefinition jobs = null;
  
      try (InputStream stream = JspGeneticAlgorithmTest.class.getResourceAsStream(path)) {
        jobs = new JspJobsDefinition(jobInfo, stream);
      }

      new JspGeneticAlgorithmTest().runAlgorithm(jobs);
      new JspGeneticAlgorithmTest().runAlgorithmAdapter(jobs);
    } catch (Exception ex) {
      log.error(ex);
    }
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */
  public void runAlgorithm(JspJobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    System.out.println(jobs.getJobsCount());
    int populationCount = 100;
    System.out.println("Population: " + populationCount);
    List<Subject<Integer>> initialSolutions = generateInitialSubjects(jobs, populationCount);
    GeneticAlgorithm<Subject<Integer>> gs = new GeneticAlgorithm<>(
        new JspGeneticOperator(), new JspSubjectEvaluator(new JspPhenotypeEvaluator(pi, jobs)));
    gs.addGeneticSearchListener(this);
    gs.setEliteSubjectsPct(5);
    gs.setMutatePopulationPct(5);
    gs.setMutateChromosomeLengthPct(30);
    gs.setPopulationCount(populationCount);
    gs.setRandomSubjectsPct(5);
    gs.setCrossLengthPct(30);
    gs.setIterationToGo(1000);
    gs.startSearching(initialSolutions);
  }

  /**
   * .
   * @param jobs .
   * @throws Exception .
   */
  public void runAlgorithmAdapter(JspJobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules = problemProvider.generateInitialSolutions(
        jobs, 100, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
    JspGeneticAlgorithmFactory factory = new JspGeneticAlgorithmFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    try {
      IAlgorithmAdapter<JspPhenotype, Subject<Integer>> adapter =  factory.createAlgorithm(
          jobs, eval);
      adapter.solutionsFromPhenotype(schedules);
      adapter.startSearching(params);
      var solutions = adapter.solutionsToPhenotype();
      System.out.println(solutions[0].getObjValue());
      System.out.println(solutions[0].getScore());

    } catch (Exception e) {
      log.error(ex);
    }
}

  private List<Subject<Integer>> generateInitialSubjects(
      JspJobsDefinition jobs, int subjectCount) throws Exception {
    ArrayList<Subject<Integer>> result = new ArrayList<>(subjectCount);

    JspProblemProvider problemProvider = new JspProblemProvider();
    
    JspPhenotype[] schedules = problemProvider.generateInitialSolutions(
      jobs, subjectCount, this.generator.nextLong());
    
    for (int k = 0; k < subjectCount; k++) {
      result.add(new Subject<>(schedules[k].getSolution()));
    }

    return result;
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode(
        "Algorithms").getDataNodeById("GeneticAlgorithm");
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 10000);
    result.putValue("numSolutions", 100);
    return result;
  }

  @Override
  public void algorithmStarted(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("Genetic Algorithm for JSP started.");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("Genetic Algorithm for JSP stopped.");
  }

  @Override
  public void newBestSolutionFound(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("New best: " + e.getGeneticSearch().getBestSubject().getFitness()[0]);
  }

  @Override
  public void noChangeInValueIterationMade(GeneticAlgorithmEvent<Subject<Integer>> e) {

  }

  @Override
  public void iterationPerformed(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("Iteration performed: " + e.getGeneticSearch().getCurrentIteration());
  }

}
