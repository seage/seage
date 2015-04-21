package org.seage.aal.algorithm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstanceInfo;

public abstract class AlgorithmFactoryTestBase 
{
	protected IAlgorithmFactory _algorithmFactory;
	protected  IProblemProvider _problemProvider;
	public AlgorithmFactoryTestBase(IAlgorithmFactory algorithmFactory, IProblemProvider problemProvider)
	{
		_algorithmFactory = algorithmFactory;
		_problemProvider = problemProvider;
	}
	
	@Before
	public void setUp() throws Exception
	{
		assertNotNull(_algorithmFactory);
		assertNotNull(_problemProvider);
	}
	
	@Test
	public void testCreateAlgorithm() throws Exception
	{
		assertNotNull(_problemProvider);
		List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
		assertTrue(infos.size()>0);
		ProblemInstanceInfo pii = infos.get(0);
		
		IAlgorithmAdapter aa = _algorithmFactory.createAlgorithm(_problemProvider.initProblemInstance(pii));
		assertNotNull(aa);
	} 
}
