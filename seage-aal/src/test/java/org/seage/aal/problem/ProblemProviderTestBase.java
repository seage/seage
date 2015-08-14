package org.seage.aal.problem;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.seage.aal.algorithm.IPhenotypeEvaluator;

//@Ignore 
public abstract class ProblemProviderTestBase
{
    protected IProblemProvider _problemProvider;
    protected ProblemInstance _problemInstance;

    public ProblemProviderTestBase(IProblemProvider provider)
    {
        _problemProvider = provider;
    }

    @Before
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
        IPhenotypeEvaluator phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(_problemInstance);
        assertNotNull(phenotypeEvaluator);
    }

    @Test
    public void testGenerateInitialSolutions() throws Exception
    {
        Object[][] solutions = _problemProvider.generateInitialSolutions(_problemInstance, 10, 10);
        assertNotNull(solutions);
        assertEquals(10, solutions.length);
    }

    @Test
    public void testPhenotypeEvaluator() throws Exception
    {
        IPhenotypeEvaluator phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(_problemInstance);
        assertNotNull(phenotypeEvaluator);
        Object[][] solutions = _problemProvider.generateInitialSolutions(_problemInstance, 10, 10);

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
