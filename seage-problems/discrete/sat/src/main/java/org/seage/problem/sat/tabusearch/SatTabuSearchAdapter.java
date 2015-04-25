package sat.algorithm;

import ailibrary.data.*;
import ailibrary.algorithm.*;
import ailibrary.algorithm.tabusearch.*;
import sat.algorithm.tabusearch.*;
/**
 * Summary description for SatTabuSearchProxy.
 */
public class SatTabuSearchAdapter extends TabuSearchAdapter
{
	public SatTabuSearchAdapter(MoveManager moveManager,
								ObjectiveFunction objectiveFunction,
								LongTermMemory longTermMemory,
								boolean maximizing,
								String searchID)
	{
		super(moveManager, objectiveFunction, longTermMemory, maximizing, searchID);
	}

	public Object[] loadSolution()
	{
		DataTable dt = DataStore.getInstance().getDataTable("solutions");
		int numSolutions =  dt.getRowCount();

		SatSolution[] results = new SatSolution[numSolutions];

		for (int i = 0; i < numSolutions; i++)
		{
			DataRow dr = dt.getRow(i);
			results[i] = new SatSolution(dr.getCellCount());
			results[i].setObjectiveValue((double[])dr.getRowProperty());

			for (int j = 0; j < dr.getCellCount(); j++)
			{
				results[i].getLiteralValues()[j] = ((Integer)dr.getCell(j).getCellProperty()).intValue() == 1 ? true : false;
			}			
		}
		return results;
	}

	public void saveSolution(Object[] solutions)
	{
		DataStore.getInstance().removeDataTable("solutions");

		int numSolutions = solutions.length;
		DataTable dt = new DataTable();
		dt.initDataRows(numSolutions);

		for (int i = 0; i < numSolutions; i++)
		{
			DataRow dr = dt.getRow(i);
			SatSolution sol = (SatSolution)solutions[i];
			dr.setRowProperty(sol.getObjectiveValue());

			int numCells = sol.getLiteralValues().length;
			dr.initDataCells(numCells);

			for (int j = 0; j < numCells; j++)
			{
				int val = sol.getLiteralValues()[j] == true ? 1 : 0;
				dr.getCell(j).setCellProperty(new Integer(val));
			}
		}
		DataStore.getInstance().insertDataTable("solutions", dt);
	}

}
