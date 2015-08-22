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
package org.seage.problem.jssp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemProvider;

/**
 *
 * @author Richard Malek
 */
@Annotations.ProblemId("JSSP")
@Annotations.ProblemName("Job Shop Scheduling Problem")
public class JsspProblemProvider extends ProblemProvider
{
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
            System.err.println("SatProblemProvider.initProblemInstance - readCities failed, path: " + path);
            throw ex;
        }

        return jobsDefinition;
    }

    @Override
    public IPhenotypeEvaluator initPhenotypeEvaluator(ProblemInstance problemInstance) throws Exception
    {
        JobsDefinition jobsDefinition = (JobsDefinition) problemInstance;
        return new JsspPhenotypeEvaluator(jobsDefinition);
    }

    @Override
    public Object[][] generateInitialSolutions(ProblemInstance problemInstance, int numSolutions, long randomSeed)
            throws Exception
    {
        Random rnd = new Random(randomSeed);
        JobsDefinition jobsDefinition = (JobsDefinition)problemInstance;
        Integer[][] result = new Integer[numSolutions][];
        
        int numJobs = jobsDefinition.getJobsCount();
        int numOpers = jobsDefinition.getJobInfos()[0].getOperationInfos().length;
        
        for(int i=0;i<numSolutions;i++)
        {
            result[i] = new Integer[numJobs*numOpers];
            int l=0;
            for(int j=0;j<numJobs;j++)
                for(int k=0;k<numOpers;k++)
                    result[i][l++] = j+1;
        }
        
        for(int i=0;i<numSolutions;i++)
        {
            for(int j=0;j<numJobs*numOpers*2;j++)
            {
                int ix1=rnd.nextInt(numJobs*numOpers);
                int ix2=rnd.nextInt(numJobs*numOpers);
                int a = result[i][ix1];
                result[i][ix1] = result[i][ix2];
                result[i][ix2] = a;
            }
        }
        
        return result;
    }

    @Override
    public void visualizeSolution(Object[] solution, ProblemInstanceInfo problemInstanceInfo) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
