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
import java.util.Arrays;

/**
 * @author Richard Malek (original)
 */
public class Chromosome<GeneType> implements Serializable, Cloneable
{
    private static final long serialVersionUID = 3050047980641091757L;
    //public Gene[] m_gene;
    private GeneType[] _genes;

    @SuppressWarnings("unused")
    private Chromosome()
    {
    }

    public Chromosome(GeneType[] geneValues)
    {
        _genes = Arrays.copyOf(geneValues, geneValues.length);
    }

    public int getLength()
    {
        return _genes.length;
    }

    public GeneType getGene(int index)
    {
        return _genes[index];
    }

    public GeneType[] getGenes()
    {
        return _genes;
    }

    public void setGene(int index, GeneType gene)
    {
        _genes[index] = gene;
    }

    @Override
    public Chromosome<GeneType> clone()
    {
        return new Chromosome<GeneType>(_genes);
    }

    public void swapGenes(int index1, int index2)
    {
        GeneType tmpGene = _genes[index1];
        setGene(index1, getGene(index2));
        setGene(index2, tmpGene);
    }

    @Override
    public String toString()
    {
        String tmp = "";
        for (int i = 0; i < _genes.length; i++)
        {
            tmp += _genes[i].toString() + " ";
        }
        return tmp;
    }
}
