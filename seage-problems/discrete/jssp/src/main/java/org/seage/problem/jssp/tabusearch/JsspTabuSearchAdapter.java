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
package org.seage.problem.jssp.tabusearch;

import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.data.ds.DataStore;
import org.seage.data.ds.DataTable;
import org.seage.metaheuristic.tabusearch.LongTermMemory;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
/**
 * Summary description for JsspTabuSearchAdapter.
 */
public class JsspTabuSearchAdapter extends TabuSearchAdapter
{
	public JsspTabuSearchAdapter(MoveManager moveManager,
								 ObjectiveFunction objectiveFunction,
								 LongTermMemory longTermMemory,
								 boolean maximizing,
								 String searchID)
	{
		super(moveManager, objectiveFunction, searchID);
	}

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

//	public Object[] loadSolution() throws Exception
//	{
//		try
//		{
//			DataTable dt = DataStore.getInstance().getDataTable("solutions");
//			JsspSolution[] results = new JsspSolution[dt.getRowCount()];
//
//			for (int i = 0; i < results.length; i++)
//			{
//				int numOper = dt.getRow(i).getCellCount();
//				int[] jobArray = new int[numOper];
//
//				for (int j = 0; j < numOper; j++)
//				{
//					jobArray[j] = ((Integer)dt.getRow(i).getCell(j).getCellProperty()).intValue();
//				}
//				results[i] = new JsspSolution(jobArray);
//				results[i].setObjectiveValue((double[])dt.getRow(i).getRowProperty());
//			}
//			return results;
//		}
//		catch (Exception ex)
//		{
//			throw ex;
//		}
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
//				JsspSolution sol = (JsspSolution)solutions[i];
//				dt.getRow(i).setRowProperty(sol.getObjectiveValue());
//				for (int j = 0; j < cellCount; j++)
//				{
//					Integer cellValue = new Integer(sol.getJobArray()[j]);
//					dt.getRow(i).getCell(j).setCellProperty(cellValue);
//				}
//			}
//		}
//		catch (Exception ex)
//		{
//			throw ex;
//		}
//	}
}
