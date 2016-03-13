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
package org.seage.problem.tsp.antcolony;

import java.util.HashSet;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspAntBrain extends AntBrain
{
    public TspAntBrain(Graph graph)
    {
        super(graph);
    }

    @Override
    protected HashSet<Node> getAvailableNodes(Node startingNode, Node currentNode)
    {
        HashSet<Node> result = super.getAvailableNodes(startingNode, currentNode);
        if (currentNode != startingNode && result.size() == 0)
        {
            result.add(startingNode);
        }
        return result;
    }
}
