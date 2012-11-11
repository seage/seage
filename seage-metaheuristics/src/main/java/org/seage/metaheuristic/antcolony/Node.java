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

import java.util.*;

/**
 *
 * @author Martin Zaloga
 */
public class Node {

    private int _id;
    private HashSet<Edge> _connections;

    public Node(int id) {
        _id = id;
        _connections = new HashSet<Edge>();
    }

    /**
     * My implementation function equals
     * @param node - Compared node
     * @return - if compared nodes are some
     */
    public boolean equals(Node node) {
        if(_id == node._id){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Identification number
     * @return - Number id
     */
    public int getId() {
        return _id;
    }

    /**
     * Edge in edge-list adding
     * @param edge - Edge for add
     */
    public void addConnection(Edge edge) {
    	if(!_connections.contains(edge))
    		_connections.add(edge);
    }

    /**
     * List all edges which are joined with actual node
     * @return - List edges
     */
    public HashSet<Edge> getConnectionMap() {
        return _connections;
    }
}
