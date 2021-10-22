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
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.jssp.genetics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.checkerframework.common.reflection.qual.GetClass;
import org.seage.aal.Annotations;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.jssp.JobInfo;
import org.seage.problem.jssp.JobsDefinition;
import org.seage.problem.jssp.JsspPhenotype;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;
import org.seage.problem.jssp.JsspProblemProvider;
//import org.seage.problem.jssp.sannealing.JsspSolution;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("Genetic Algorithm")
public class JsspGeneticAlgorithmFactory implements IAlgorithmFactory<JsspPhenotype, Subject<Integer>>
{

    @Override
    public Class<?> getAlgorithmClass()
    {
        return GeneticAlgorithmAdapter.class;
    }

    @Override
    public IAlgorithmAdapter<JsspPhenotype, Subject<Integer>> createAlgorithm(
        ProblemInstance instance,
        IPhenotypeEvaluator<JsspPhenotype> phenotypeEvaluator
      ) throws Exception
    {
        //JobsDefinition jobsDefinition = (JobsDefinition) instance;
        return new GeneticAlgorithmAdapter<JsspPhenotype, Subject<Integer>>(
                new JsspGeneticOperator(),
                new JsspSubjectEvaluator(phenotypeEvaluator), phenotypeEvaluator, false)
        {
            @Override
            public void solutionsFromPhenotype(JsspPhenotype[] source) throws Exception
            {
                this.solutions = new ArrayList<Subject<Integer>>(source.length);
                for (int i = 0; i < source.length; i++)
                    this.solutions.add(new Subject<Integer>(source[i].getSolution()));
            }

            @Override
            public JsspPhenotype[] solutionsToPhenotype() throws Exception
            {
                JsspPhenotype[] result = new JsspPhenotype[this.solutions.size()];

                for (int i = 0; i < this.solutions.size(); i++)
                {
                    //int numGenes = this.solutions.get(i).getChromosome().getLength();
                    result[i] = solutionToPhenotype(this.solutions.get(i));
                    //result[i] = new JsspPhenotype(Arrays.copyOf(this.solutions.get(i).getChromosome().getGenes(), numGenes));
                }
                return result;
            }

			@Override
			public JsspPhenotype solutionToPhenotype(Subject<Integer> solution) throws Exception {
                JsspPhenotype result = new JsspPhenotype(solution.getChromosome().getGenes());
                
                double[] objVals = this.phenotypeEvaluator.evaluate(result);

                result.setObjValue(objVals[0]);
                result.setScore(objVals[1]);
				return result;
			}
        };
    }

    public static void main(String[] args) {
        String instanceID = "ft10";
        JsspGeneticAlgorithmFactory factory = new JsspGeneticAlgorithmFactory();
        try {
            factory.run(instanceID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private AlgorithmParams createAlgorithmParams(ProblemInfo problemInfo) throws Exception {
        AlgorithmParams result = new AlgorithmParams();
        DataNode algParamsNode = problemInfo.getDataNode("Algorithms").getDataNodeById("GeneticAlgorithm");
        for (DataNode param : algParamsNode.getDataNodes("Parameter")) {
          result.putValue(param.getValueStr("name"), param.getValue("init"));
        }
        result.putValue("iterationCount", 1);
        result.putValue("numSolutions", 1);
        return result;
    }

    public void run(String instanceID) throws Exception {
        String path = String.format("/org/seage/problem/jssp/instances/%s.xml", instanceID);
        ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);

        JobsDefinition jobs = null;
        try(InputStream stream = getClass().getResourceAsStream(path)) {    
            jobs = new JobsDefinition(jobInfo, stream);
        }

        JsspProblemProvider problemProvider = new JsspProblemProvider();
        JsspPhenotype[] schedules = problemProvider.generateInitialSolutions(jobs, 100, (new Random()).nextLong());

        AlgorithmParams params = createAlgorithmParams(problemProvider.getProblemInfo());

        JsspGeneticAlgorithmFactory factory = new JsspGeneticAlgorithmFactory();
        JsspPhenotypeEvaluator eval = new JsspPhenotypeEvaluator(jobs);
        try {
            IAlgorithmAdapter<JsspPhenotype, Subject<Integer>> adapter =  factory.createAlgorithm(jobs, eval);
            adapter.solutionsFromPhenotype(schedules);
            adapter.startSearching(params);
            //adapter.solutionsToPhenotype();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
