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
 *     Martin Zaloga
 *     - Reimplementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

    protected Graph _graph;
    protected Node _startPosition;
    protected Node _currentPosition;
    protected double _distanceTravelled;
    protected HashSet<Node> _visited;
    protected Vector<Edge> _path;
    protected AntBrain _brain;
    private double _qantumPheromone;

    public Ant(AntBrain brain, Graph graph , double qantumPheromone)
    {
        _brain = brain;
        _graph = graph;
        
        _qantumPheromone = qantumPheromone;        
    }

    /**
     * Ant passage through the graph
     * @return - ants path
     */
    protected Vector<Edge> explore(Node startingNode)
    {
        _visited = new HashSet<Node>();
        _path = new Vector<Edge>();
        _brain._startingNode = startingNode;
        _currentPosition = startingNode;
        _visited.add(startingNode);

        List<Edge> edges = _brain.getAvailableEdges(_currentPosition, _visited);
        while (edges != null && edges.size() > 0) {
            Edge nextEdge = _brain.selectNextEdge(edges, _visited);
            updatePosition(nextEdge);
            edges = _brain.getAvailableEdges(_currentPosition, _visited);
        }        

        //_distanceTravelled = _brain.pathCost(_path);
        leavePheromone();
        return _path; // Report
    }

    /**
     * Update ants position
     * @param selectedEdge - Actual selected edge
     */
    protected void updatePosition(Edge arcChoice) {
        Node choiceNode;
        if (arcChoice.getNode1().equals(_currentPosition)) {
            choiceNode = (arcChoice.getNode2());
        } else {
            choiceNode = (arcChoice.getNode1());
        }
        _distanceTravelled += arcChoice.getEdgePrice();
        _path.add(arcChoice);
        _visited.add(choiceNode);
        _currentPosition = choiceNode;
    }

    /**
     * Pheromone leaving
     */
    protected void leavePheromone() {
        for (Edge edge : _path) {
            edge.addLocalPheromone(_qantumPheromone / (_distanceTravelled));
        }
    }
}
