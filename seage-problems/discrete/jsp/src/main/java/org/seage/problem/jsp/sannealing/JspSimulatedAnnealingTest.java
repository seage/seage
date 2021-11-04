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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.problem.jsp.tabusearch.JspTabuSearchTest;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Jan Zmatlik
 * Edited by David Omrai
 */
public class JspSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent>
{
  //private static String _dataPath = "D:\\eil51.tsp";

  public static void main(String[] args)
  {
    try
    {
      //String path = "data/tsp/eil51.tsp";//args[0];		// 426
      //String path = "data/tsp/berlin52.tsp";//args[0]; 	// 7542
      //String path = "data/tsp/ch130.tsp";//args[0]; 		// 6110
      //String path = "data/tsp/lin318.tsp";//args[0]; 		// 42029
      //String path = "data/tsp/pcb442.tsp";//args[0]; 		// 50778
      //String path = "data/tsp/u574.tsp";//args[0]; 		// 36905
      String instanceID = "ft10";
      String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      JobsDefinition jobs = null;

      try(InputStream stream = JspSimulatedAnnealingTest.class.getResourceAsStream(path)) {
        jobs = new JobsDefinition(jobInfo, stream);
      }

      new JspSimulatedAnnealingTest().runAlgorithm(jobs);
      //todo - new JspSimulatedAnnealingTest().run(path);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void runAlgorithm(JobsDefinition jobs) throws Exception
  {
//    _cities = CityProvider.readCities(new FileInputStream(path));
//    System.out.println("Loading cities from path: " + path);
//    System.out.println("Number of cities: " + _cities.length);
//
//    JspObjectiveFunction objFunction = new JspObjectiveFunction(_cities);
//    SimulatedAnnealing sa = new SimulatedAnnealing(objFunction, new TspMoveManager2(objFunction));
//
//    sa.setMaximalTemperature(1000000.0d);
//    sa.setMinimalTemperature(0.01d);
//    //sa.setAnnealingCoefficient( 0.9999999);
//    sa.setMaximalIterationCount(100000000);
//
//    sa.addSimulatedAnnealingListener(this);
//    TspGreedySolution s = new TspGreedySolution(_cities);
//    //TspRandomSolution s = new TspRandomSolution(_cities.length);
//
//    System.out.println(objFunction.getObjectiveValue(s));
//    sa.startSearching(s);
//
//    System.out.println(sa.getBestSolution().getObjectiveValue());
//    System.out.println(sa.getBestSolution());
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();

    JspObjectiveFunction objFunction = new JspObjectiveFunction(new JspPhenotypeEvaluator(pi, jobs));

    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    SimulatedAnnealing sa = new SimulatedAnnealing(objFunction, new JspMoveManager(jobs));

    // Set the sa algorithm
    sa.setMaximalTemperature(1000000);
    sa.setMinimalTemperature(0.01);
    sa.setMaximalIterationCount(100000000);

    // Create solution
    JspSimulatedAnnealingSolution s = new JspSimulatedAnnealingGreedySolution(eval, jobs);

    // Start the searching process
    sa.startSearching(s);
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
