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
	protected IProblemProvider _provider;
	protected ProblemInstance _problemInstance;
	
	public ProblemProviderTestBase(IProblemProvider provider)
	{
		_provider = provider;
	}
	
	@Before
	public void setUp() throws Exception
	{
		assertNotNull(_provider);
		List<ProblemInstanceInfo> infos = _provider.getProblemInfo().getProblemInstanceInfos();
		assertTrue(infos.size()>0);
		ProblemInstanceInfo pii = infos.get(0);
		_problemInstance = _provider.initProblemInstance(pii);
	}

	@Test
	public void testProblemInfo() throws Exception
	{
		ProblemInfo pi = _provider.getProblemInfo();
		assertNotNull(pi);
	}
	
	@Test
	public void testInitProblemInstance() throws Exception
	{		
		assertNotNull(_problemInstance);
	}
	
	@Test
	public void testInitPhenotypeEvaluator() throws Exception
	{
		IPhenotypeEvaluator phenotypeEvaluator = _provider.initPhenotypeEvaluator(_problemInstance);
		assertNotNull(phenotypeEvaluator);
	}
	
	@Test
	public void testGenerateInitialSolutions() throws Exception
	{
		Object[][] solutions = _provider.generateInitialSolutions(_problemInstance, 10, 10);
		assertNotNull(solutions);
		assertEquals(10, solutions.length);
	}
	
	@Test
	public void testPhenotypeEvaluator() throws Exception
	{
		IPhenotypeEvaluator phenotypeEvaluator = _provider.initPhenotypeEvaluator(_problemInstance);
		assertNotNull(phenotypeEvaluator);
		Object[][] solutions = _provider.generateInitialSolutions(_problemInstance, 10, 10);
		
		for(int i=0;i<10;i++)
		{
			double[] value = phenotypeEvaluator.evaluate(solutions[i]);
			assertNotNull(value);
		}
	}

}
