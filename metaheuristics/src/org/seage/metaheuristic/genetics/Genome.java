/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
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
	Chromosome[] _chromosomes;

	private Genome()
	{ }

	public Genome(int numChrom, int numGenes)
	{
            _chromosomes = new Chromosome[numChrom];
            for (int i = 0; i < _chromosomes.length; i++)
            {
                    //if (genome._chromosomes[i] != null)
                    {
                            _chromosomes[i] = new Chromosome(numGenes);//(Chromosome)genome._chromosomes[i].clone();
                            for (int j = 0; j < numGenes; j++)
                            {
                                    _chromosomes[i].setGene(j, new Gene(0));
                            }
                    }
            }
	}

	public Genome(Genome genome)
	{
            //this(genome._chromosomes.length, genome._chromosomes[0].getLength());
            _chromosomes = (Chromosome[])genome._chromosomes.clone();
            for (int i = 0; i < _chromosomes.length; i++)
            {
                    _chromosomes[i] = (Chromosome)genome._chromosomes[i].clone();
            }
	}

	public Object clone()
	{
            Genome newGenome = new Genome();
            newGenome._chromosomes = (Chromosome[])this._chromosomes.clone();
            for (int i = 0; i < _chromosomes.length; i++)
                    newGenome._chromosomes[i] = (Chromosome)this._chromosomes[i].clone();
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
            if(_chromosomes[index] != null) _chromosomes[index] = null;
            _chromosomes[index] = chromosome;
	}
}
