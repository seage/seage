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
    private ArrayList<Edge> _connectionMap;

    public Node(int id) {
        _id = id;
        _connectionMap = new ArrayList<Edge>();
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
        _connectionMap.add(edge);
    }

    /**
     * List all edges which are joined with actual node
     * @return - List edges
     */
    public ArrayList<Edge> getConnectionMap() {
        return _connectionMap;
    }
}
