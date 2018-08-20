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
package org.seage.problem.jssp.genetics;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

/**
 * Summary description for JsspOperator.
 */
public class JsspGeneticOperator extends BasicGeneticOperator<Subject<Integer>, Integer>
{
    private int[] _occurrence;
    private int[] _occurrence2;

    public JsspGeneticOperator(int numOper)
    {
        _occurrence = new int[numOper];
        _occurrence2 = new int[numOper];
    }

    @Override
    public List<Subject<Integer>> crossOver(Subject<Integer> parent1, Subject<Integer> parent2) throws Exception
    {
        Subject<Integer> offsprings1 = null;
        Subject<Integer> offsprings2 = null;
        int chromLength = 0;
        int crossLength = 0;
        try
        {
            offsprings1 = parent1.clone();
            offsprings2 = parent2.clone();

            chromLength = offsprings1.getChromosome().getLength();

            crossLength = /*(int)(chromLength * _crossLengthPct);/*/Math
                    .max(_random.nextInt((int) (chromLength * _crossLengthCoef) + 1) - 2, 2);
            int crossBegin = _random.nextInt(chromLength - crossLength);
            int crossEnd = crossBegin + crossLength;

            for (int i = 0; i < _occurrence.length; i++)
                _occurrence[i] = 0;
            for (int i = 0; i < _occurrence2.length; i++)
                _occurrence2[i] = 0;

            for (int i = crossBegin; i < crossEnd; i++)
            {
                //if (i >= crossBegin && i < crossEnd)
                {
                    int par1GeneVal = parent1.getChromosome().getGene(i);
                    int par2GeneVal = parent2.getChromosome().getGene(i);

                    if (par1GeneVal == par2GeneVal)
                        continue;

                    _occurrence[par1GeneVal - 1]++;
                    _occurrence[par2GeneVal - 1]--;

                    _occurrence2[par2GeneVal - 1]++;
                    _occurrence2[par1GeneVal - 1]--;

                    offsprings1.getChromosome().setGene(i, parent2.getChromosome().getGene(i));
                    offsprings2.getChromosome().setGene(i, parent1.getChromosome().getGene(i));
                    //offsprings[1].Chromosome.Genes[i].Value = parent1.Chromosome.Genes[i].Value;
                }
                //else
                {
                    //offsprings[0].getChromosome().getGene(i).setValue(0, ((GSSolution)parent1).getChromosome().getGene(i).getValue(0));
                    //offsprings[1].getChromosome().getGene(i).setValue(0, ((GSSolution)parent2).getChromosome().getGene(i).getValue(0));

                    //offspring1.Chromosome.Genes[i].Value = parent1.Chromosome.Genes[i].Value;
                    //offspring2.Chromosome.Genes[i].Value = parent2.Chromosome.Genes[i].Value;
                }

            }

            // pouziva se a predava clenska promenna, aby se pole m_occurrence alokovalo pouze jednou
            medicateSubject(crossBegin, _occurrence, offsprings2);
            medicateSubject(crossBegin, _occurrence2, offsprings1);

            int p1 = getSubjectSum(parent1);
            int p2 = getSubjectSum(parent2);

            int o1 = getSubjectSum(offsprings1);
            int o2 = getSubjectSum(offsprings2);

            if (!((p1 == p2) && (p1 == o1) && (p1 == o2)))
                throw new Exception("checksum failed");
        }
        catch (Exception e)
        {
            throw new Exception("crossover: " + e);
        }
        
        ArrayList<Subject<Integer>> result = new ArrayList<>();
        result.add(offsprings1); result.add(offsprings2);
        return result;
    }

    protected void medicateSubject(int crossBegin, int[] occurrence, Subject<Integer> subject) throws Exception
    {
        try
        {
            int /*indexExcessP1 = 0,*/ indexMinus = -1, indexMinusPrev = 0;

            for (int indexPlus = 0; indexPlus < occurrence.length; indexPlus++)
            {
                if (occurrence[indexPlus] > 0)
                {
                    while (true)
                    {
                        // najdu index se zapornou hodnotou - ten prvek chybi
                        if (indexMinus == -1)
                        {
                            for (int j = indexMinusPrev; j < occurrence.length; j++)
                                if (occurrence[j] < 0)
                                {
                                    indexMinus = j;
                                    indexMinusPrev = indexMinus;
                                    break;
                                }
                        }
                        /*val1 = m_occurrence[indexPlus];
                        val2 = -m_occurrence[indexMinus];*/

                        // pokud pocet chybejicich prvku "indexMinus" prevysuje "indexPlus"
                        if (occurrence[indexPlus] <= -occurrence[indexMinus])
                        {

                            int jj = crossBegin;
                            while (occurrence[indexPlus] > 0)
                            {
                                if ((Integer) subject.getChromosome().getGene(jj) == indexPlus + 1)
                                {
                                    subject.getChromosome().setGene(jj, indexMinus + 1);

                                    occurrence[indexPlus]--;
                                    occurrence[indexMinus]++;
                                }
                                jj++;
                                //jj = (jj+1) % subject.Chromosome.Length ;
                            }

                            //m_occurrence[indexMinus] += m_occurrence[indexPlus];								
                            //m_occurrence[indexPlus] = 0;
                            if (occurrence[indexPlus] == -occurrence[indexMinus])
                                indexMinus = -1;
                            break;

                        }
                        else
                        {
                            // parent1 ma navic "m_occurrence[i]" jobu s id "i"
                            // mam k dispozici id "indexPlus" o poctu "m_occurrence[indexMinus]"
                            int jj = crossBegin;
                            while (occurrence[indexMinus] < 0)
                            {
                                if ((Integer) subject.getChromosome().getGene(jj) == indexPlus + 1)
                                {
                                    subject.getChromosome().setGene(jj, indexMinus + 1);

                                    occurrence[indexPlus]--;
                                    occurrence[indexMinus]++;
                                }
                                jj++;
                                //jj = (jj+1) % subject.Chromosome.Length ;
                            }
                            /*m_occurrence[i] += m_occurrence[indexExcessP2];
                            m_occurrence[indexExcessP2] = 0;*/
                            indexMinus = -1;
                        }

                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new Exception("medicate: " + e);
        }
    }

    private int getSubjectSum(Subject<Integer> sol)
    {
        Subject<Integer> s = sol;
        int sum = 0;
        for (int i = 0; i < s.getChromosome().getLength(); i++)
        {
            sum += s.getChromosome().getGene(i);
        }
        return sum;
    }

}
