package org.seage.problem.tsp.grasp;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.grasp.GraspAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.tsp.TspPhenotype;

@Annotations.AlgorithmId("GRASP")
@Annotations.AlgorithmName("GRASP")
public class TspGraspAlgorithmFactory implements IAlgorithmFactory<TspPhenotype, TspSolution>
{
    @Override
    public Class<GraspAlgorithmAdapter> getAlgorithmClass()
    {
        return GraspAlgorithmAdapter.class;
    }

    @Override
    public IAlgorithmAdapter<TspPhenotype, TspSolution> createAlgorithm(ProblemInstance instance,
            IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception {
        return new TspGraspAlgorithmAdapter();
    }

}
