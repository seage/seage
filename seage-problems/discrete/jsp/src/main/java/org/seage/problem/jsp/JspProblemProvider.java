/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation - Added problem
 * annotations
 */
package org.seage.problem.jsp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("JSP")
@Annotations.ProblemName("Job Shop Scheduling Problem")
public class JspProblemProvider extends ProblemProvider<JspPhenotype>
{
  private static Logger logger = LoggerFactory.getLogger(JspProblemProvider.class.getName());

  @Override
  public JobsDefinition initProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception
  {
    ProblemInstanceOrigin origin = instanceInfo.getOrigin();
    String path = instanceInfo.getPath();

    InputStream stream0;
    if (origin == ProblemInstanceOrigin.RESOURCE)
      stream0 = getClass().getResourceAsStream(path);
    else
      stream0 = new FileInputStream(path);
    
    JobsDefinition jobsDefinition = null;
    
    try(InputStream stream=stream0)
    {
        jobsDefinition = new JobsDefinition(instanceInfo, stream);   
    }
    catch (Exception ex)
    {
      logger.error("SatProblemProvider.initProblemInstance - readCities failed, path: {}", path);
      throw ex;
    }

    return jobsDefinition;
  }

  @Override
  public IPhenotypeEvaluator<JspPhenotype> initPhenotypeEvaluator(ProblemInstance problemInstance) throws Exception
  {
    ProblemInfo pi = this.getProblemInfo();
    JobsDefinition jobsDefinition = (JobsDefinition) problemInstance;
    return new JspPhenotypeEvaluator(pi, jobsDefinition);
  }

  @Override
  public JspPhenotype[] generateInitialSolutions(ProblemInstance problemInstance, int numSolutions, long randomSeed)
    throws Exception
  {
    JobsDefinition jobs = (JobsDefinition)problemInstance;
    JspPhenotype[] result = new JspPhenotype[numSolutions];
    IPhenotypeEvaluator<JspPhenotype> evaluator = this.initPhenotypeEvaluator(problemInstance);
      
    for(int i=0;i<numSolutions;i++)
    {
      // Create random schedule
      result[i] = ScheduleProvider.createRandomSchedule(jobs, randomSeed);
      // Evaluate the random schedule
      double[] objVals = evaluator.evaluate(result[i]);
      result[i].setObjValue(objVals[0]);
      result[i].setScore(objVals[1]);
      
    }
      
    return result;
  }

  @Override
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo problemInstanceInfo) throws Exception
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
