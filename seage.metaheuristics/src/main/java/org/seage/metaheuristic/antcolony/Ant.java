/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.antcolony;

import java.util.*;

/**
 *
 * @author Richard Malek (original)
 */
public class Ant {

    protected Graph _graph;
    protected Node _startPosition;
    protected Node _currentPosition;
    protected double _distanceTravelled;
    protected double _qantumPheromone;
    protected Vector<Node> _visited = new Vector<Node>();
    protected Vector<Edge> _path = new Vector<Edge>();
    protected AntBrain _brain;

    public Ant(Graph graph, double qantumPheromone, AntBrain brain) {
        _graph = graph;
        _brain = brain;
        _qantumPheromone = qantumPheromone;
    }

    public Vector<Edge> explore() {
        return null;
    }

    protected void updatePosition(Edge arcChoice) {
    }

    public void leavePheromone() {
    }
}
