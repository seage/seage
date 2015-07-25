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
package org.seage.problem.sat.antcolony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Zagy
 */
public class SatGraph extends Graph implements java.lang.Cloneable 
{
	private Formula _formula;
	
    public SatGraph(Formula formula) throws Exception 
    {
        super();
        _formula = formula;
        //   /~  1 ~  2 ~  3 ~ ...  n 
        // 0                       
        //   \~ -1 ~ -2 ~ -3 ~ ... -n 
        _nodes.put(new Integer(0), new Node(0));
        
        for (int i = 1; i <= formula.getLiteralCount(); i++) 
        {
            _nodes.put(new Integer(i), new Node(i));
            _nodes.put(new Integer(-i), new Node(-i));
        }        
    }
    
	@Override
	public List<Node> getAvailableNodes(Node startingNode, Node currentNode, HashSet<Node> visited) 
	{		
		int nextID = Math.abs(currentNode.getID())+1;
		if(!getNodes().containsKey(nextID))
			return null;
		ArrayList<Node> result = new ArrayList<Node>();
		result.add(getNodes().get(nextID));
		result.add(getNodes().get(-nextID));
		return result;
	}

	@Override
	public double getNodesDistance(Node n1, Node n2)
	{
		return FormulaEvaluator.evaluate(_formula, Math.abs(n2.getID())-1, n2.getID()>0);
	}
}
