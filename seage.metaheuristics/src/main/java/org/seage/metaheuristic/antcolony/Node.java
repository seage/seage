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

    private int _id = 0;
    private ArrayList<Edge> _connectionMap = new ArrayList<Edge>();

    public Node(int id) {
        _id = id;
    }

    public boolean equals(Node node) {
        if(_id == node._id){
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return _id;
    }

    public void addConnection(Edge edge) {
        _connectionMap.add(edge);
    }

    public ArrayList<Edge> getConnectionMap() {
        return _connectionMap;
    }
}
