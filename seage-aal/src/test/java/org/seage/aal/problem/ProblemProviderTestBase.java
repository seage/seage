package org.seage.aal.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;

//@Ignore 
public abstract class ProblemProviderTestBase<P extends Phenotype<?>>
{
    protected IProblemProvider<P> _problemProvider;
    protected ProblemInstance _problemInstance;

    public ProblemProviderTestBase(IProblemProvider<P> provider)
    {
        _problemProvider = provider;
    }

    @BeforeEach
    public void setUp() throws Exception
    {
        assertNotNull(_problemProvider);
        List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
        assertTrue(infos.size() > 0);
        ProblemInstanceInfo pii = infos.get(0);
        _problemInstance = _problemProvider.initProblemInstance(pii);
    }

    @Test
    public void testProblemInfo() throws Exception
    {
        ProblemInfo pi = _problemProvider.getProblemInfo();
        assertNotNull(pi);
        assertEquals(pi.getDataNode("Algorithms").getDataNodes("Algorithm").size(),
                _problemProvider.getAlgorithmFactories().values().size());
    }

    @Test
    public void testInitProblemInstance() throws Exception
    {
        assertNotNull(_problemInstance);
    }

    @Test
    public void testInitPhenotypeEvaluator() throws Exception
    {
        IPhenotypeEvaluator<?> phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(_problemInstance);
        assertNotNull(phenotypeEvaluator);
    }

    @Test
    public void testGenerateInitialSolutions() throws Exception
    {
        Phenotype<?>[] solutions = _problemProvider.generateInitialSolutions(_problemInstance, 10, 10);
        assertNotNull(solutions);
        assertEquals(10, solutions.length);
    }

    @Test
    public void testPhenotypeEvaluator() throws Exception
    {
        IPhenotypeEvaluator<P> phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(_problemInstance);
        assertNotNull(phenotypeEvaluator);
        P[] solutions = _problemProvider.generateInitialSolutions(_problemInstance, 10, 10);

        for (int i = 0; i < 10; i++)
        {
            double[] value = phenotypeEvaluator.evaluate(solutions[i]);
            assertNotNull(value);
        }
    }
    //	
    //	@Test
    //	public void testAlgorithmFactories() throws Exception
    //	{
    //		_problemProvider.getProblemInfo().get
    //	}

}
