/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Richard Malek (original)
 * @param <GeneType>
 */
public class BasicGeneticOperator<S extends Subject<GeneType>, GeneType> extends GeneticOperator<S>
{
	protected Random _random;
	protected double _crossLengthPct;
	//protected double _mutatePct;
	protected double _mutateLengthPct;

	public BasicGeneticOperator()
	{
		_random = new Random();

		_crossLengthPct = 0.45;
		_mutateLengthPct = 0.3;
	}

	// / <summary>
	// / Vybira dva subjekty, kteri se budou krizit
	// / </summary>
	// / <param name="population">Aktualni populace - podle jejiz parametru se
	// vyber provadi</param>
	// / <returns>Vraci indexy vybranych subjectu</returns>
	@Override
	public int[] select(ArrayList<S> subjects)
	{
		int count = subjects.size();
		int[] result = new int[2];
		double[] table = new double[count];
		table[0] = distributionF(1, count);

		for (int i = 1; i < count; i++)
		{
			table[i] = table[i - 1] + distributionF(i + 1, count);
		}

		double r = _random.nextDouble();

		for (int i = 0; i < count; i++)
		{
			if (r < table[i])
			{
				result[0] = result[1] = i;
				;
				break;
			}
		}
		while (result[0] == result[1])
		{
			r = _random.nextDouble();

			for (int i = 0; i < count; i++)
			{
				if (r < table[i])
				{
					result[1] = i;
					break;
				}
			}
		}

		return result;
	}

	private double distributionF(int i, int n)
	{
		return 2 * (n + 1.0 - i) / (n * (n + 1.0));
	}

	// / <summary>
	// / Krizi dva subjekty
	// / </summary>
	// / <param name="parent1">Prvni rodic</param>
	// / <param name="parent2">Druhy rodic</param>
	// / <returns>Vraci mnozinu potomku</returns>
	@SuppressWarnings("unchecked")
	@Override
	public List<S> crossOver(S parent1, S parent2) throws Exception
	{		
		int length = parent1.getChromosome().getLength();
		int ix = _random.nextInt(length);
		int crossLength = 1+(int) (length * _random.nextDouble() * _crossLengthPct);

		if (ix > length - crossLength)
			ix = length - crossLength;

		S child1 = (S) parent1.clone();
		S child2 = (S) parent2.clone();

		for (int i = ix; i < ix + crossLength; i++)
		{
			GeneType x1 = child1.getChromosome().getGene(i);
			GeneType x2 = child2.getChromosome().getGene(i);
			child1.getChromosome().setGene(i, x2);
			child2.getChromosome().setGene(i, x1);
		}
		
		List<S> result = new ArrayList<S>();
		result.add(child1);result.add(child2);
		return result;
	}

	// / <summary>
	// / Mutuje subjekt
	// / </summary>
	// / <param name="subject">Subjekt, ktery je mutovan</param>
	// / <returns>Vraci zmutovanehy Subject</returns>
	@SuppressWarnings("unchecked")
	@Override
	public S mutate(S subject) throws Exception
	{

		int length = subject.getChromosome().getLength();
		int count = /* (int)(length * _mutatePct);/ */_random.nextInt((int) (length * _mutateLengthPct) + 1);
		S mutant = (S)subject.clone();

		for (int i = 0; i < count; i++)
		{
			int ix1 = _random.nextInt(length);
			int ix2 = _random.nextInt(length);

			mutant.getChromosome().swapGenes(ix1, ix2);
		}
		return mutant;

	}

	@SuppressWarnings("unchecked")
	@Override
	public S randomize(S subject) throws Exception
	{
		int length = subject.getChromosome().getLength();

		S random = (S)subject.clone();

		random.getChromosome().swapGenes(1, 2);

		for (int i = 0; i < length * 10; i++)
		{
			int ix1 = _random.nextInt(length);
			int ix2 = _random.nextInt(length);

			random.getChromosome().swapGenes(ix1, ix2);
		}
		return random;

	}

	public void setCrossLengthPct(double pct)
	{
		_crossLengthPct = pct;
	}

	public void setMutateLengthPct(double pct)
	{
		_mutateLengthPct = pct;
	}
}
