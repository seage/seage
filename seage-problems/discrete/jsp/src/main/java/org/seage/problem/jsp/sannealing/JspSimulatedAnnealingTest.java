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
 *   Jan Zmatlik
 *   - Initial implementation
 *   David Omrai
 *   - Implementing the tests
 */
package org.seage.problem.jsp.sannealing;

import java.io.InputStream;
import java.util.Random;
import org.seage.data.DataNode;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Jan Zmatlik
 * Edited by David Omrai
 */
public class JspSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent>
{
  private Random generator = new Random();

  public static void main(String[] args)
  {
    try
    {
      String instanceID = "ft10";
      String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JobsDefinition jobs = null;

      try(InputStream stream = JspSimulatedAnnealingTest.class.getResourceAsStream(path)) {
        jobs = new JobsDefinition(jobInfo, stream);
      }

      new JspSimulatedAnnealingTest().runAlgorithm(jobs);
      new JspSimulatedAnnealingTest().runAlgorithmAdapter(jobs);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void runAlgorithm(JobsDefinition jobs) throws Exception
  {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();

    JspObjectiveFunction objFunction = new JspObjectiveFunction(new JspPhenotypeEvaluator(pi, jobs));

    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    SimulatedAnnealing<JspSimulatedAnnealingSolution> sa = new SimulatedAnnealing<>(objFunction, new JspMoveManager(jobs));

    // Set the sa algorithm
    sa.setMaximalTemperature(1000000);
    sa.setMinimalTemperature(0.01);
    sa.setMaximalIterationCount(10000);

    // Create solution
    JspSimulatedAnnealingSolution s = new JspSimulatedAnnealingRandomSolution(eval, jobs);

    // Add event listener
    sa.addSimulatedAnnealingListener(this);
    
    // Start the searching process
    sa.startSearching(s);
  }

  private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
    AlgorithmParams result = new AlgorithmParams();
    DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("SimulatedAnnealing");
    for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
      result.putValue(param.getValueStr("name"), param.getValue("init"));
    }
    result.putValue("iterationCount", 150000);
    result.putValue("numSolutions", 1);
    return result;
  }

  public void runAlgorithmAdapter(JobsDefinition jobs) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, 1, generator.nextLong());

    AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

    ProblemInfo pi = problemProvider.getProblemInfo();
    JspSimulatedAnnealingFactory factory = new JspSimulatedAnnealingFactory();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    try {
      IAlgorithmAdapter<JspPhenotype, JspSimulatedAnnealingSolution> adapter = factory.createAlgorithm(jobs, eval);
      adapter.solutionsFromPhenotype(schedules);
      adapter.startSearching(params);
      var solutions = adapter.solutionsToPhenotype();
      System.out.println(solutions[0].getObjValue());
      System.out.println(solutions[0].getScore());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void algorithmStarted(SimulatedAnnealingEvent e)
  {
    System.out.println("Started");
  }

  @Override
  public void algorithmStopped(SimulatedAnnealingEvent e)
  {
    System.out.println("Stopped");
  }

  @Override
  public void iterationPerformed(SimulatedAnnealingEvent e)
  {

  }

  @Override
  public void noChangeInValueIterationMade(SimulatedAnnealingEvent e)
  {

  }

  @Override
  public void newBestSolutionFound(SimulatedAnnealingEvent e)
  {
    ISimulatedAnnealing sa = e.getSimulatedAnnealing();
    System.out.println(String.format("New best: %f - iter: %d - temp: %f", sa.getBestSolution().getObjectiveValue(),
        sa.getCurrentIteration(), sa.getCurrentTemperature()));
  }

}
