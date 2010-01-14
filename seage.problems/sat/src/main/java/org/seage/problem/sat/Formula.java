package org.seage.problem.sat;

import java.util.*;
/**
 * Summary description for Formula.
 */
public class Formula
{
	private ArrayList _clauses;
	private Clause[] __null;
	public Formula()
	{
		_clauses = new ArrayList();
		__null = new Clause[0];
	}

	public void addClause(Clause clause)
	{
		_clauses.add(clause);
	}
	
	public Clause[] getClauses()
	{
		return (Clause[])_clauses.toArray(__null);
	}

	public int getNumLiteral()
	{
		int max = 0;
		for (int i = 0; i < _clauses.size(); i++)
		{ 
			Clause cl = (Clause)_clauses.get(i);
			Literal[] lit = cl.getLiterals();
			for (int j = 0; j < lit.length; j++)
				if (lit[j].getValue() > max)
					max = lit[j].getValue();
		}
		return max+1;
	}

	public String toString()
	{
		String result = "";
		for (int i = 0; i < _clauses.size(); i++)
		{
			result += _clauses.get(i).toString() + "\n";
		}

		return result;
	}
}
