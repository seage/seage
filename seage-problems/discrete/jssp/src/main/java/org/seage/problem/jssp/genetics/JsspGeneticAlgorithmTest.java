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
package org.seage.problem.jssp.genetics;

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
import org.seage.problem.jssp.JobsDefinition;
import org.seage.problem.jssp.JsspPhenotype;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;
import org.seage.problem.jssp.JsspProblemProvider;

/**
 *
 * @author Richard Malek
 * Edited by David Omrai
 */
public class JsspGeneticAlgorithmTest implements IAlgorithmListener<GeneticAlgorithmEvent<Subject<Integer>>> {
  private Random generator = new Random();

  public static void main(String[] args) {
    try {
      String instanceID = "ft10"; 
      String path = String.format("/org/seage/problem/jssp/instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JobsDefinition jobs = null;
  
      try(InputStream stream = JsspGeneticAlgorithmTest.class.getResourceAsStream(path)) {    
        jobs = new JobsDefinition(jobInfo, stream);
      }

      new JsspGeneticAlgorithmTest().runAlgorithm(jobs);
      new JsspGeneticAlgorithmTest().runAlgorithmAdapter(jobs);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void runAlgorithm(JobsDefinition jobs) throws Exception {
    JsspProblemProvider problemProvider = new JsspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    System.out.println(jobs.getJobsCount());
    int populationCount = 100;
    System.out.println("Population: " + populationCount);
    List<Subject<Integer>> initialSolutions = generateInitialSubjects(jobs, populationCount);
    GeneticAlgorithm<Subject<Integer>> gs = new GeneticAlgorithm<>(
        new JsspGeneticOperator(), new JsspSubjectEvaluator(new JsspPhenotypeEvaluator(pi, jobs)));
    gs.addGeneticSearchListener(this);
    gs.setEliteSubjectsPct(5);
    gs.setMutatePopulationPct(5);
    gs.setMutateChromosomeLengthPct(30);
    gs.setPopulationCount(populationCount);
    gs.setRandomSubjectsPct(5);
    gs.setCrossLengthPct(30);
    gs.setIterationToGo(100);
    gs.startSearching(initialSolutions);
  }

  public void runAlgorithmAdapter(JobsDefinition jobs) throws Exception {
    JsspProblemProvider problemProvider = new JsspProblemProvider();
    JsspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, 100, (new Random()).nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
    JsspGeneticAlgorithmFactory factory = new JsspGeneticAlgorithmFactory();
    JsspPhenotypeEvaluator eval = new JsspPhenotypeEvaluator(pi, jobs);
    try {
        IAlgorithmAdapter<JsspPhenotype, Subject<Integer>> adapter =  factory.createAlgorithm(jobs, eval);
        adapter.solutionsFromPhenotype(schedules);
        adapter.startSearching(params);
        adapter.solutionsToPhenotype();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

  private List<Subject<Integer>> generateInitialSubjects(JobsDefinition jobs, int subjectCount) throws Exception {
    ArrayList<Subject<Integer>> result = new ArrayList<>(subjectCount);

    JsspProblemProvider problemProvider = new JsspProblemProvider();
    
    JsspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, subjectCount, this.generator.nextLong());
    
    for (int k = 0; k < subjectCount; k++)
      result.add(new Subject<>(schedules[k].getSolution()));

    return result;
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("GeneticAlgorithm");
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 100);
    result.putValue("numSolutions", 100);
    return result;
}

  @Override
  public void algorithmStarted(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("Genetic Algorithm for JSSP started.");
  }

  @Override
  public void algorithmStopped(GeneticAlgorithmEvent<Subject<Integer>> e) {
    System.out.println("Genetic Algorithm for JSSP stopped.");
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
