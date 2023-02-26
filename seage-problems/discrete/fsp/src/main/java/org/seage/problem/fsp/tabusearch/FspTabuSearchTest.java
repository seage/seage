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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.fsp.tabusearch;

import java.io.InputStream;
import java.util.Random;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.fsp.FspJobsDefinition;
import org.seage.problem.fsp.FspPhenotypeEvaluator;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.tabusearch.JspMoveManager;
import org.seage.problem.jsp.tabusearch.JspTabuSearchSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author Richard Malek
 */
public class FspTabuSearchTest implements TabuSearchListener {
  private static final Logger log = LoggerFactory.getLogger(FspTabuSearchTest.class.getName());
  private Random generator = new Random();

  public static void main(String[] args) {
    try {
      // String instanceID = "yn_3x3_example";
      // String path = "/org/seage/problem/jsp/test-instances/yn_3x3_example.xml";
      String instanceID = "tai20_05_01";
      String path = "/org/seage/problem/fsp/instances/tai20_05_01.txt";
      ProblemInstanceInfo jobInfo =
          new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      FspJobsDefinition jobs = null;

      try (InputStream stream = FspTabuSearchTest.class.getResourceAsStream(path)) {
        jobs = new FspJobsDefinition(jobInfo, stream);
      }

      new FspTabuSearchTest().runAlgorithm(jobs);
      // new JspTabuSearchTest().runAlgorithmAdapter(jobs);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void runAlgorithm(FspJobsDefinition jobs) throws Exception {
    FspProblemProvider problemProvider = new FspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    log.info("Number of jobs: {}", jobs.getJobsCount());

    FspPhenotypeEvaluator evaluator = new FspPhenotypeEvaluator(pi, jobs);

    TabuSearch ts = new TabuSearch(
        new JspTabuSearchSolution(jobs.getJobsCount(),
            jobs.getJobInfos()[0].getOperationInfos().length),
        new JspMoveManager(jobs), new FspObjectiveFunction(evaluator), new SimpleTabuList(130),
        new BestEverAspirationCriteria(), false);

    ts.addTabuSearchListener(this);
    ts.setIterationsToGo(10000000);
    ts.startSolving();
  }

  public void runAlgorithmAdapter(FspJobsDefinition jobs) throws Exception {
    FspProblemProvider problemProvider = new FspProblemProvider();
    JspPhenotype[] schedules =
        problemProvider.generateInitialSolutions(jobs, 1, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
    FspTabuSearchFactory factory = new FspTabuSearchFactory();
    FspPhenotypeEvaluator eval = new FspPhenotypeEvaluator(pi, jobs);
    try {
      IAlgorithmAdapter<JspPhenotype, JspTabuSearchSolution> adapter =
          factory.createAlgorithm(jobs, eval);
      adapter.solutionsFromPhenotype(schedules);
      adapter.startSearching(params);
      var solutions = adapter.solutionsToPhenotype();
      log.info("{}", solutions[0].getObjValue());
      log.info("{}", solutions[0].getScore());

    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("TabuSearch");
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 150000);
    result.putValue("numSolutions", 1);
    return result;
  }

  @Override
  public void newBestSolutionFound(TabuSearchEvent e) {
    log.info("{} - {}", e.getTabuSearch().getBestSolution(),
        e.getTabuSearch().getIterationsCompleted());
  }

  @Override
  public void improvingMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void newCurrentSolutionFound(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void noChangeInValueMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void tabuSearchStarted(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void tabuSearchStopped(TabuSearchEvent e) {
    log.info("finished");
  }

  @Override
  public void unimprovingMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

}
