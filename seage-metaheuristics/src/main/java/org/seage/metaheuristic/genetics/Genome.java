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

import java.io.Serializable;

/**
 * @author Richard Malek (original)
 */
public class Genome implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6409601380364797857L;
	Chromosome[] _chromosomes;

	private Genome()
	{
	}

//	public Genome(int numChrom, int numGenes)
//	{
//		_chromosomes = new Chromosome[numChrom];
//		for (int i = 0; i < _chromosomes.length; i++)
//		{
//			// if (genome._chromosomes[i] != null)
//			{
//				_chromosomes[i] = new Chromosome(numGenes);// (Chromosome)genome._chromosomes[i].clone();
//				for (int j = 0; j < numGenes; j++)
//				{
//					_chromosomes[i].setGene(j, new Gene(0));
//				}
//			}
//		}
//	}

//	public Genome(Genome genome)
//	{
//		this(genome._chromosomes.length, genome._chromosomes[0].getLength());
//		// _chromosomes = (Chromosome[])genome._chromosomes.clone();
//		for (int i = 0; i < _chromosomes.length; i++)
//		{
//			for (int j = 0; j < _chromosomes[i].getLength(); j++)
//				_chromosomes[i].setGene(j, new Gene(genome.getChromosome(i).getGene(j).getValue()));// =
//																									// (Chromosome)genome._chromosomes[i].clone();
//		}
//	}

	public Object clone()
	{
		Genome newGenome = new Genome();
		newGenome._chromosomes = (Chromosome[]) this._chromosomes.clone();
		for (int i = 0; i < _chromosomes.length; i++)
			newGenome._chromosomes[i] = (Chromosome) this._chromosomes[i].clone();
		return newGenome;
	}

	public int getLength()
	{
		return _chromosomes.length;
	}

	public Chromosome getChromosome(int index)
	{
		return _chromosomes[index];
	}

	public void setChromosome(int index, Chromosome chromosome)
	{
		if (_chromosomes[index] != null)
			_chromosomes[index] = null;
		_chromosomes[index] = chromosome;
	}
}
