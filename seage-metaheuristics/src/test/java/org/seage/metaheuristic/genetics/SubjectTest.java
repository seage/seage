package org.seage.metaheuristic.genetics;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class SubjectTest
{
	private static Subject<Integer> s;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		s = new Subject<Integer>(new Integer[]{1,2,3,4});
		s.setObjectiveValue(new double[]{100});
	}

	@Test
	public void testClone()
	{
		assertNotNull(s);
		Subject<Integer> clone = s.clone();
		assertNotNull(clone);
		assertNotSame(s, clone);
		assertTrue(s.hashCode() == clone.hashCode());
		assertArrayEquals(s.getObjectiveValue(), clone.getObjectiveValue(), 0);
		
		for(int i=0;i<s.getChromosome().getLength();i++)
		{
			assertTrue(s.getChromosome().getGene(i).equals(clone.getChromosome().getGene(i)));
		}
		
		s.getChromosome().setGene(0, 5);
		s.computeHash();
		assertFalse(s.hashCode() == clone.hashCode());
	}

}
