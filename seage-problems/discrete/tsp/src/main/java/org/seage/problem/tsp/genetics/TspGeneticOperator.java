/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.genetics;

import org.seage.metaheuristic.genetics.*;
import java.util.HashMap;

/**
 *  @author Richard Malek
 */
public class TspGeneticOperator extends GeneticOperator
{
	public TspGeneticOperator()
	{
	}

	//public int[] select(Subject[] subjects)
	//{
	//    return new int[] { _random.nextInt(subjects.length), _random.nextInt(subjects.length) };
	//}

	public Subject[] crossOver2(Subject parent1, Subject parent2) throws Exception
	{
		try
		{
			int length = parent1.getGenome().getChromosome(0).getLength();

			int[] occur1 = new int[length];
			int[] occur2 = new int[length];
			int[] cross1 = new int[length];
			int[] cross2 = new int[length];
			for (int i = 0; i < length; i++)
				cross1[i] = cross2[i] = -1;


			int ix = _random.nextInt(length);
			int crossLength = (int)(length * _random.nextDouble() * _crossLengthPct) + 1;

			if (ix > length - crossLength)
				ix = length - crossLength;

			Subject child1 = (Subject)parent1.clone();
			Subject child2 = (Subject)parent2.clone();

			for (int i = ix; i < ix + crossLength; i++)
			{
				int x1 = child1.getGenome().getChromosome(0).getGene(i).getValue();
				int x2 = child2.getGenome().getChromosome(0).getGene(i).getValue();

				child1.getGenome().getChromosome(0).getGene(i).setValue(x2);
				child2.getGenome().getChromosome(0).getGene(i).setValue(x1);

				cross1[x2] = x1;
				cross2[x1] = x2;
			}
			Chromosome ch1 = child1.getGenome().getChromosome(0);
			Chromosome ch2 = child2.getGenome().getChromosome(0);

			for (int i = 0; i < length; i++)
			{
				if (i < ix || i >= ix + crossLength)
				{
					int c1 = 0, c2 = 0;
					while (c1 != -1)
					{
						c1 = cross1[ch1.getGene(i).getValue()];
						if (c1 != -1)
						{
							ch1.getGene(i).setValue(c1);
						}
					}
					while (c2 != -1)
					{
						c2 = cross2[ch2.getGene(i).getValue()];
						if (c2 != -1)
						{
							ch2.getGene(i).setValue(c2);
						}
					}
				}
				occur1[ch1.getGene(i).getValue()]++;
				occur2[ch2.getGene(i).getValue()]++;
			}

			for (int i = 0; i < length; i++)
			{
				if (occur1[i] > 1 || occur2[i] > 1)
					throw new Exception("Invalid chromosome");
			}

			return new Subject[] { child1, child2 };

			////////////////////////////////////////////////////////////////////////////



			//Chromosome ch1 = children[0].getGenome().getChromosome(0);
			//Chromosome ch2 = children[1].getGenome().getChromosome(0);

			//for (int i = 0; i < occur1.length; i++)
			//{
			//    occur1[ch1.getGene(i).getValue()]++;
			//    occur2[ch2.getGene(i).getValue()]++;
			//}

			//for (int i = 0; i < occur1.length; i++)
			//{
			//    if (occur1[i] > 1)
			//    {
			//        for (int j = i; j < occur1.length; j++)
			//        {
			//            if (occur1[i] < 1)
			//            {

			//            }
			//        }
			//    }
			//}

			//return children;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

        @Override
	public Subject[] crossOver(Subject parent1, Subject parent2) throws Exception
	{
		try
		{
			int length = parent1.getGenome().getChromosome(0).getLength();

			int ix = _random.nextInt(length);
			int crossLength = (int)(length * _random.nextDouble() * _crossLengthPct) + 1;

			if (ix > length - crossLength)
				ix = length - crossLength;

			Subject child1 = (Subject)parent1.clone();
			Subject child2 = (Subject)parent2.clone();
			Chromosome child1Chrom = child1.getGenome().getChromosome(0);
                        Chromosome child2Chrom = child2.getGenome().getChromosome(0);

                        HashMap<Integer, Integer> chromVals1 = new HashMap<Integer, Integer>();
                        HashMap<Integer, Integer> chromVals2 = new HashMap<Integer, Integer>();
                        
                        for(int i=0;i<length;i++)
                        {
                            chromVals1.put(i, child1Chrom.getGene(i).getValue());
                            chromVals2.put(i, child2Chrom.getGene(i).getValue());
                        }
                        
			for (int i = ix; i < ix + crossLength; i++)
			{				

				int x1 = child1Chrom.getGene(i).getValue();
				int x2 = child2Chrom.getGene(i).getValue();

				if (x1 == x2)
					continue;

//				int y1 = findGeneValueIndex(x2, child1Chrom);
//				int y2 = findGeneValueIndex(x1, child2Chrom);
                                int y1 = chromVals1.get(x2);
				int y2 = chromVals2.get(x1);

				child1Chrom.swapGenes(i, y1);
				child1Chrom.swapGenes(i, y2);
			}

			return new Subject[] { child1, child2 };
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
	}

}
