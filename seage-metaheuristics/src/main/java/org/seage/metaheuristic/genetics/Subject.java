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
public class Subject<GeneType>
{
    private Chromosome<GeneType> _chromosome;
    private double[] _fitness;
    private int _hashCode;

    public Subject(GeneType[] geneValues)
    {
        _chromosome = new Chromosome<GeneType>(geneValues);
        computeHash();
    }

    protected Subject(Subject<GeneType> subject)
    {
        _chromosome = subject.getChromosome().clone();
        _fitness = subject._fitness;
        computeHash();
    }

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
        _fitness = objValue;
        computeHash();
    }

    @Override
    public Subject<GeneType> clone()
    {
        return new Subject<GeneType>(this);
    }

    /*
     * Subject's method
     */
    public Chromosome<GeneType> getChromosome()
    {
        return _chromosome;
    }

    public double[] getFitness()
    {
        return _fitness;
    }

    public void computeHash()
    {
        //Chromosome chrom = getGenome().getChromosome(0);
        int hash = 0x1000;
        for (int i = 0; i < _chromosome.getLength(); i++)
        {
            hash = ((hash << 5) ^ (hash >> 27)) ^ (_chromosome.getGene(i).hashCode() + 2) << 1;
        }
        _hashCode = hash;
    }

    @Override
    public int hashCode()
    {
        return _hashCode;
    }

    @Override
    public String toString()
    {
        String result = "";

        if (_fitness == null)
            return "#" + _hashCode;

        for (int i = 0; i < _chromosome.getLength(); i++)
        {
            result += (_chromosome.getGene(i)) + " ";
        }

        return result;
    }
}
