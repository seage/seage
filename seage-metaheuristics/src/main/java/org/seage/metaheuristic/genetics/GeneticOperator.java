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

import java.util.Random;

/**
 * @author Richard Malek (original)
 */
public class GeneticOperator
{
	protected Random _random;
	protected double _crossLengthPct;
	//protected double _mutatePct;
	protected double _mutateLengthPct;

	public GeneticOperator()
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
	public int[] select(Subject[] subjects)
	{
		int count = subjects.length;
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
	public Subject[] crossOver(Subject parent1, Subject parent2) throws Exception
	{
		int length = parent1.getGenome().getChromosome(0).getLength();
		int ix = _random.nextInt(length);
		int crossLength = (int) (length * _random.nextDouble() * _crossLengthPct);

		if (ix > length - crossLength)
			ix = length - crossLength;

		Subject child1 = (Subject) parent1.clone();
		Subject child2 = (Subject) parent2.clone();

		for (int i = ix; i < ix + crossLength; i++)
		{
			Object x1 = child1.getGenome().getChromosome(0).getGene(i).getValue();
			Object x2 = child2.getGenome().getChromosome(0).getGene(i).getValue();
			child1.getGenome().getChromosome(0).getGene(i).setValue(x2);
			child2.getGenome().getChromosome(0).getGene(i).setValue(x1);
		}
		return new Subject[] { child1, child2 };
		// return new Subject[] { parent1, parent2 };
	}

	// / <summary>
	// / Mutuje subjekt
	// / </summary>
	// / <param name="subject">Subjekt, ktery je mutovan</param>
	// / <returns>Vraci zmutovanehy Subject</returns>
	public Subject mutate(Subject subject) throws Exception
	{

		int length = subject.getGenome().getChromosome(0).getLength();
		int count = /* (int)(length * _mutatePct);/ */_random.nextInt((int) (length * _mutateLengthPct) + 1);
		Subject mutant = (Subject) subject.clone();

		for (int i = 0; i < count; i++)
		{
			int ix1 = _random.nextInt(length);
			int ix2 = _random.nextInt(length);
			// int x =
			// mutant.getGenome().getChromosome(0).getGene(ix).getValue();
			mutant.getGenome().getChromosome(0).swapGenes(ix1, ix2);
		}
		return mutant;

	}

	public Subject randomize(Subject subject) throws Exception
	{
		int length = subject.getGenome().getChromosome(0).getLength();

		Subject random = (Subject) subject.clone();

		random.getGenome().getChromosome(0).swapGenes(1, 2);

		for (int i = 0; i < length * 10; i++)
		{
			int ix1 = _random.nextInt(length);
			int ix2 = _random.nextInt(length);
			// int x =
			// mutant.getGenome().getChromosome(0).getGene(ix).getValue();
			random.getGenome().getChromosome(0).swapGenes(ix1, ix2);
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
