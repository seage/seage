package org.seage.aal.algorithm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstanceInfo;

public abstract class AlgorithmFactoryTestBase 
{
	protected IAlgorithmFactory _algorithmFactory;
	protected IProblemProvider _problemProvider;
	protected String _algorithmID;
	
	public AlgorithmFactoryTestBase(IProblemProvider problemProvider, String algorithmID)
	{
		_problemProvider = problemProvider;
		_algorithmID = algorithmID;
	}
	
	@Before
	public void setUp() throws Exception
	{		
		assertNotNull(_problemProvider);
		assertNotNull(_problemProvider.getProblemInfo());
		_algorithmFactory = _problemProvider.getAlgorithmFactory(_algorithmID);
		assertNotNull(_algorithmFactory);
	}
	
	@Test
	public void testGetAlgorithmClass()
	{
		assertNotNull(_algorithmFactory.getAlgorithmClass());
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
	
	@Test
	public void testAlgorithmAdapter() throws Exception
	{
		assertNotNull(_problemProvider);
		List<ProblemInstanceInfo> infos = _problemProvider.getProblemInfo().getProblemInstanceInfos();
		assertTrue(infos.size()>0);
		ProblemInstanceInfo pii = infos.get(0);
						
		IAlgorithmAdapter aa = _algorithmFactory.createAlgorithm(_problemProvider.initProblemInstance(pii));
		assertNotNull(aa);
		
		Object[][] solutions = _problemProvider.generateInitialSolutions(_problemProvider.initProblemInstance(pii), 10, 1);
		assertNotNull(solutions);
		aa.solutionsFromPhenotype(solutions);
		Object[][] solutions2 = aa.solutionsToPhenotype();
		assertNotNull(solutions2);
		
		// TODO: A - Compare solutions and solutions2
		for(int i=0;i<solutions.length;i++)
		{
			assertNotNull(solutions[i]);
			assertNotNull(solutions2[i]);
			for(int j=0;j<solutions[i].length;j++)
			{
				assertEquals(solutions[i][j], solutions2[i][j]);
			}
		}
	} 
}
