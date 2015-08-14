package org.seage.problem.sat.genetics;

import org.seage.metaheuristic.genetics.Chromosome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 * Summary description for SatObjectiveFunction.
 */
public class SatEvaluator extends SubjectEvaluator<Subject<Boolean>>
{
    private SatPhenotypeEvaluator _evaluator;

    public SatEvaluator(SatPhenotypeEvaluator evaluator)
    {
        _evaluator = evaluator;
    }

    public double[] evaluate(Subject<Boolean> solution) throws Exception
    {
        Chromosome<Boolean> chrom = solution.getChromosome();
        Boolean[] array = new Boolean[chrom.getLength()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = chrom.getGene(i);
        }
        double[] val = _evaluator.evaluate(array);
        return new double[] { val[0] };
    }
}
