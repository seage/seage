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
public class Node {

    private int _id;
    private ArrayList<Edge> connectionMap = new ArrayList<Edge>();

    public Node(int id) {
        _id = id;
    }

    /**
     * @return the ID of this Vertice
     */
    public int getId() {
        return _id;
    }

    protected void addConectionEdge(Edge edge) {
        connectionMap.add(edge);
    }

    public ArrayList<Edge> getConnectionMap() {
        return connectionMap;
    }
}
