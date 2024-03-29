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

package org.seage.problem.jsp.tabusearch;

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
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author Richard Malek
 */
public class JspTabuSearchTest implements TabuSearchListener {
  private static final Logger log = LoggerFactory.getLogger(JspTabuSearchTest.class.getName());
  private Random generator = new Random();

  public static void main(String[] args) {
    try {
      // String instanceID = "yn_3x3_example";
      // String path = "/org/seage/problem/jsp/test-instances/yn_3x3_example.xml";
      String instanceID = "ft10";
      String path = "/org/seage/problem/jsp/instances/ft10.xml";
      ProblemInstanceInfo jobInfo =
          new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JspJobsDefinition jobs = null;

      try (InputStream stream = JspTabuSearchTest.class.getResourceAsStream(path)) {
        jobs = new JspJobsDefinition(jobInfo, stream);
      }

      new JspTabuSearchTest().runAlgorithm(jobs);
      // new JspTabuSearchTest().runAlgorithmAdapter(jobs);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void runAlgorithm(JspJobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    log.info("Number of jobs: {}", jobs.getJobsCount());

    JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, jobs);

    TabuSearch ts = new TabuSearch(
        new JspTabuSearchSolution(jobs.getJobsCount(),
            jobs.getJobInfos()[0].getOperationInfos().length),
        new JspMoveManager(jobs), new JspObjectiveFunction(evaluator), new SimpleTabuList(30),
        new BestEverAspirationCriteria(), false);

    ts.addTabuSearchListener(this);
    ts.setIterationsToGo(10000);
    ts.startSolving();
  }

  public void runAlgorithmAdapter(JspJobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules =
        problemProvider.generateInitialSolutions(jobs, 1, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
    JspTabuSearchFactory factory = new JspTabuSearchFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
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
