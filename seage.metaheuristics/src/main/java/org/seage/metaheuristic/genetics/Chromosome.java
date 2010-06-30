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
public class Chromosome implements Serializable
{
	//public Gene[] m_gene;
	private Gene[] _genes;

	private Chromosome()
	{ }

	public Chromosome(int length)
	{		
		_genes = new Gene[length];
		//m_gene = new int[length];
	}

	public Chromosome(Gene[] genes)
	{		
		_genes = genes;
		//m_gene = new int[length];
	}
	
	public int getLength()
	{
		return _genes.length;
	}
	
	public Gene getGene(int index)
	{
		return _genes[index];
	}
        
        public Integer[] getGeneArray()
        {
            Integer[] result = new Integer[_genes.length];
            for(int i =0;i<result.length;i++) result[i] = _genes[i]._value;
            return result;
        }
        
        public void setGeneArray(Integer[] geneArray)
        {
            for(int i =0;i<_genes.length;i++) 
                _genes[i]._value = geneArray[i];
        }

	public void setGene(int index, Gene gene)
	{
		_genes[index] = gene;//(Gene)gene.clone();
	}

	public Object clone()
	{
		Chromosome newChrom = new Chromosome();

		Gene[] genes = new Gene[_genes.length];
		for (int i = 0; i < genes.length; i++)
			genes[i] = (Gene)_genes[i].clone();

		newChrom._genes = genes;
		return  newChrom;
	}

	public void swapGenes(int index1, int index2)
	{
		Gene tmpGene = _genes[index1];
		setGene(index1, getGene(index2));
		setGene(index2, tmpGene);

		/*int tmpGene = _genes[index1].getValue(0);
		_genes[index1].setValue(0, _genes[index2].getValue(0));
		_genes[index2].setValue(0, tmpGene);*/
	}

	public String toString()
	{
		String tmp = "";
		for(int i=0;i<_genes.length;i++)
		{
			tmp += _genes[i].toString() + " ";
		}
		return tmp;
	}
}
