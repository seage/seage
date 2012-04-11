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

/**
 *
 * @author Martin Zaloga
 */
public class AntCreator {

    protected Graph _graph;
    protected AntBrain _brain;
    protected int _numAnts;
    protected double _qantumPheromone;

    public AntCreator(Graph graph, AntBrain brain, int numAnts, double qantumPheromone){
        _graph = graph;
        _brain = brain;
        _numAnts = numAnts;
        _qantumPheromone = qantumPheromone;
    }

    /**
     * Making ants
     * @return - Ants
     */
    protected Ant[] createAnts() {
        return null;
    }

    /**
     * Nuber ants finding
     * @return Number ants
     */
    public int getnumAnts(){
        return _numAnts;
    }
}
