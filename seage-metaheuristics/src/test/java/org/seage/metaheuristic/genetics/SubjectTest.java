package org.seage.metaheuristic.genetics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SubjectTest
{
    private static Subject<Integer> s;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception
    {
        s = new Subject<Integer>(new Integer[] { 1, 2, 3, 4 });
        s.setObjectiveValue(new double[] { 100 });
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

        for (int i = 0; i < s.getChromosome().getLength(); i++)
        {
            assertTrue(s.getChromosome().getGene(i).equals(clone.getChromosome().getGene(i)));
        }

        s.getChromosome().setGene(0, 5);
        s.computeHash();
        assertFalse(s.hashCode() == clone.hashCode());
    }

}
