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

/**
 * @author Richard Malek (original)
 */
public class Subject
{
	private static int _maxId = 0;
	private Genome _genome;
	private double[] _fitness;
	private int _id;
	private int _hashCode;

	public Subject(Genome genome)
	{
		_genome = new Genome(genome);
		_fitness = new double[] {};
		_id = ++_maxId;
		computeHash();
	}

	public Subject(Subject subject)
	{
		_genome = new Genome((Genome) subject._genome);
		_fitness = subject._fitness;
		computeHash();
	}

	public int getId()
	{
		return _id;
	}

	// public int compareTo(Object obj)
	// {
	// Subject s1 = this;
	// Subject s2 = (Subject)obj;

	// for(int i=0;i<s1._fitness.length;i++)
	// {
	// boolean d1 = s1._fitness[i] > s2._fitness[i];
	// boolean d2 = s1._fitness[i] < s2._fitness[i];

	// if(d1 == false && d2 == false)
	// continue;
	// else
	// if(d1 == true)
	// return 1;
	// else
	// if (d2 == true)
	// return -1;
	// }

	// return 0;
	// }

	/*
	 * Interface Solution
	 */

	public double[] getObjectiveValue()
	{
		/*
		 * int[] result = {_fitness}; return result;
		 */
		return _fitness;
	}

	public void setObjectiveValue(double[] objValue)
	{
		_fitness = objValue;// (int)objValue[0];
		computeHash();
	}

	public Object clone()
	{
		Object result = new Subject(this);
//		try
//		{
//			 result = (Subject)super.clone();
//			 result._genome = (Genome)this._genome.clone();
//			 if (this._fitness != null)
//			 result._fitness = (double[])this._fitness.clone();
//			 result._hashCode = this._hashCode;
//
//			 ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			 ObjectOutputStream oos = new ObjectOutputStream(baos);
//			 oos.writeObject(this);
//			 ByteArrayInputStream bais = new
//			 ByteArrayInputStream(baos.toByteArray());
//			 ObjectInputStream ois = new ObjectInputStream(bais);
//			 result = ois.readObject();
//
//		}
//		catch (Exception ex)
//		{
//			throw new InternalError(ex.toString());
//		}
		return result;
	}

	/*
	 * Subject's method
	 */
	public Genome getGenome()
	{
		return _genome;
	}

	public double[] getFitness()
	{
		return _fitness;
	}

	public void print()
	{
		System.out.println();
		for (int i = 0; i < _genome.getLength(); i++)
		{
			System.out.print("M" + i + "> ");
			for (int j = 0; j < _genome.getChromosome(i).getLength(); j++)
			{
				String nula, nula2;

				int jobID = 0;// _genome.getChromosome(i).getGene(j).getValue(0);
				int operID = 0;// _genome.getChromosome(i).getGene(j).getValue(1);

				if (jobID < 10)
					nula = "00";
				else if (jobID < 100)
					nula = "0";
				else
					nula = "";

				if (operID < 10)
					nula2 = "00";
				else if (operID < 100)
					nula2 = "0";
				else
					nula2 = "";

				System.out.print(nula + jobID + "-" + nula2 + operID + " ");
				// if(_genome.getChromosome(i).getGene(j).getValue(0) > numJobs)
				// numJobs = _genome.getChromosome(i).getGene(j).getValue(0);
			}
			System.out.println();
		}
	}

	public void computeHash()
	{
		Chromosome chrom = getGenome().getChromosome(0);
		int hash = 0x1000;
		for (int i = 0; i < chrom.getLength(); i++)
		{
			hash = ((hash << 5) ^ (hash >> 27)) ^ (chrom.getGene(i).getValue() + 2) << 1;
		}
		_hashCode = hash;
	}

	public int hashCode()
	{
		return _hashCode;
	}

	public String toString()
	{
		String result = "";

		if (_fitness == null)
			return "null";
		// NumberFormat formatter = new DecimalFormat("0.000", new
		// DecimalFormatSymbols(Locale.US));
		// for (int i = 0; i < _fitness.length; i++)
		// {
		// result += formatter.format(_fitness[i]) + "\t";
		// }
		// result += hashCode() + "\t";
		for (int i = 0; i < _genome.getChromosome(0).getLength(); i++)
		{
			result += (_genome.getChromosome(0).getGene(i)) + " ";
		}

		return result;
	}
	// public String toString()
	// {
	// Integer hash = new Integer(hashCode());
	// return hash.toString();
	// }

}
