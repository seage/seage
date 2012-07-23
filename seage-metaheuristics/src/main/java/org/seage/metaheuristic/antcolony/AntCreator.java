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
