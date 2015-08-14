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
package org.seage.metaheuristic.antcolony;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Martin Zaloga
 */
public class Node
{

    private int _id;
    private HashMap<Node, Edge> _edges;
    private HashMap<Integer, Node> _nodes;

    public Node(int id)
    {
        _id = id;
        _edges = new HashMap<Node, Edge>();
        _nodes = new HashMap<Integer, Node>();
    }

    /**
     * My implementation function equals
     * @param node - Compared node
     * @return - if compared nodes are some
     */
    public boolean equals(Node node)
    {
        if (_id == node._id)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Identification number
     * @return - Number id
     */
    public int getID()
    {
        return _id;
    }

    /**
     * Edge in edge-list adding
     * @param edge - Edge for add
     * @throws Exception 
     */
    public void addEdge(Edge edge) throws Exception
    {
        Node node = edge.getNode1();
        Node node2 = edge.getNode2();

        if (node.equals(node2))
            throw new Exception("Edge with both nodes the same.");
        if (!(node.equals(this) || node2.equals(this)))
            throw new Exception("The adding edge is not related to the current node.");

        if (node.equals(this))
            node = node2;

        if (!_edges.containsValue(edge))
            _edges.put(node, edge);

        _nodes.put(node.getID(), node);

    }

    /**
     * List all edges which are joined with actual node
     * @return - List edges
     */
    public Collection<Edge> getEdges()
    {
        return _edges.values();
    }

    public HashMap<Node, Edge> getEdgeMap()
    {
        return _edges;
    }

    @Override
    public String toString()
    {
        return new Integer(getID()).toString();
    }
}
