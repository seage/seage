package org.seage.problem.sat.genetics;

import org.seage.data.ds.DataRow;
import org.seage.data.ds.DataStore;
import org.seage.data.ds.DataTable;
import org.seage.metaheuristic.genetics.Chromosome;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
/**
 * Summary description for SatGeneticSearchProxy.
 */
public class SatGeneticSearchAdapter extends GeneticSearchAdapter
{
	public SatGeneticSearchAdapter(	GeneticOperator operator, 
									SubjectEvaluator evaluator, 
									boolean maximizing,
									String searchID)
	{
		super(operator, evaluator, maximizing, searchID);
	}

	public Object[] loadSolution()
	{
		DataTable dt = DataStore.getInstance().getDataTable("solutions");
		int numSolutions = dt.getRowCount();

		Subject[] results = new SatSubject[numSolutions];

		for (int i = 0; i < numSolutions; i++)
		{ 
			DataRow dr = dt.getRow(i);
			results[i] = new SatSubject(new Genome(1, dr.getCellCount()));
			results[i].setObjectiveValue((double[])dr.getRowProperty());
			Chromosome chrom = results[i].getGenome().getChromosome(0);
			for (int j = 0; j < dr.getCellCount(); j++)
			{
				chrom.getGene(j).setValue(((Integer)dr.getCell(j).getCellProperty()).intValue());
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
			DataRow dr = dt.getRow(solutions.length-i-1);
			Subject subj = (Subject)solutions[i];
			dr.setRowProperty(subj.getObjectiveValue());
			Chromosome chrom = subj.getGenome().getChromosome(0);
			int numCells = chrom.getLength();
			dr.initDataCells(numCells);

			for (int j = 0; j < numCells; j++)
			{
				dr.getCell(j).setCellProperty(new Integer(chrom.getGene(j).getValue()));
			}
		}
		DataStore.getInstance().insertDataTable("solutions", dt);
	}

}
