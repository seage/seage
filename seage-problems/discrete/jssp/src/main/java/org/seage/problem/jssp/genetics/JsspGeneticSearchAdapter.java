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

import javax.security.auth.Subject;

import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.data.ds.DataStore;
import org.seage.data.ds.DataTable;
import org.seage.metaheuristic.genetics.Gene;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 * Summary description for JsspGeneticSearchAdapter.
 */
public class JsspGeneticSearchAdapter extends GeneticAlgorithmAdapter
{
	public JsspGeneticSearchAdapter(GeneticOperator operator,
								SubjectEvaluator evaluator,
								boolean maximizing,
								String searchID)
	{
		super(operator, evaluator, maximizing, searchID);
	}

//	public Object[] loadSolution() throws Exception
//	{
//		DataTable dt = DataStore.getInstance().getDataTable("solutions");
//		Subject[] results = new Subject[dt.getRowCount()];
//		
//		for (int i = 0; i < results.length; i++)
//		{ 
//			int numOper = dt.getRow(i).getCellCount();
//			Subject subject = new Subject(new Genome(1, numOper));
//			for (int j = 0; j < numOper; j++)
//			{
//				int geneVal = ((Integer)dt.getRow(i).getCell(j).getCellProperty()).intValue();
//				subject.getChromosome().setGene(j, new Gene(geneVal));
//			}
//			results[i] = subject;
//			results[i].setObjectiveValue((double[])dt.getRow(i).getRowProperty());
//		}
//		return results;
//	}
//
//	public void saveSolution(Object[] solutions) throws Exception
//	{
//		try
//		{
//			DataTable dt = DataStore.getInstance().getDataTable("solutions");
//			
//			int cellCount = dt.getRow(0).getCellCount();
//
//			dt.initDataRows(solutions.length);
//			for (int i = 0; i < solutions.length; i++)
//			{
//				dt.getRow(i).initDataCells(cellCount);
//				Subject subject = (Subject)solutions[solutions.length -i-1];
//				dt.getRow(i).setRowProperty(subject.getObjectiveValue());
//				for (int j = 0; j < cellCount; j++)
//				{
//					Integer cellValue = new Integer(subject.getChromosome().getGene(j).getValue());
//					dt.getRow(i).getCell(j).setCellProperty(cellValue);
//				}
//			}
//		}
//		catch (Exception ex)
//		{
//			throw ex;
//		}
//		
//	}

	@Override
	public void solutionsFromPhenotype(Object[][] source) throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[][] solutionsToPhenotype() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}
}
