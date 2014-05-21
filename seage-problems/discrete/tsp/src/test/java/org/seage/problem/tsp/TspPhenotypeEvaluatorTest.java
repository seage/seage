package org.seage.problem.tsp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.Instance;
import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemProvider;

public class TspPhenotypeEvaluatorTest {

	@Test
	public void testTspPhenotypeEvaluator() {
		fail("Not yet implemented");
	}

	@Test
	public void testEvaluate() throws Exception {
		IProblemProvider provider = ProblemProvider.getProblemProviders().get("TSP");
		Instance instance = provider.initProblemInstance(provider.getProblemInfo().getInstanceInfo("berlin52"));
		TspPhenotypeEvaluator evaluator = new TspPhenotypeEvaluator((TspProblemInstance)instance);
		
		Integer[] subject = new Integer[]{
				1, 49, 32, 45, 19, 41, 8, 9, 10, 43, 
				33, 51, 11, 52, 14, 13, 47, 26, 27, 28,
				12, 25, 4, 6, 15, 5, 24, 48, 38, 37, 
				40, 39, 36, 35, 34, 44, 46, 16, 29, 50,
				20, 23, 30, 2, 7, 42, 21, 17, 3, 18, 
				31, 22};
		Integer[] s = subject;
		printArray(s);
		assertEquals(7542, (int)evaluator.evaluate(s)[0]);
		
		s = mirrorArray(subject);
		printArray(s);
		assertEquals(7542, (int)evaluator.evaluate(s)[0]);
		
		s = shiftArray(subject, 10);
		printArray(s);
		assertEquals(7542, (int)evaluator.evaluate(s)[0]);
		
		s = mirrorArray(shiftArray(subject, 10));
		printArray(s);
		assertEquals(7542, (int)evaluator.evaluate(s)[0]);
	}
	
	private Integer[] mirrorArray(Integer[] array)
	{
		Integer[] result = new Integer[array.length];
		
		for(int i=0;i<result.length;i++)
			result[i] = array[array.length-i-1];
		
		return result;
	}
	
	private Integer[] shiftArray(Integer[] array, int offset)
	{
		Integer[] result = new Integer[array.length];
		
		for(int i=0;i<result.length;i++)
			result[i] = array[(i+offset)%array.length];
		
		return result;
	}
	
	private void printArray(Integer[] array)
	{			
		for(int i=0;i<array.length;i++)
			System.out.print(array[i]+" ");
		
		System.out.println();
	}

	@Test
	public void testCompare() {
		fail("Not yet implemented");
	}

}
