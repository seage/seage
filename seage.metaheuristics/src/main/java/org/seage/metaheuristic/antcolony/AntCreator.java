/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.metaheuristic.antcolony;

/**
 *
 * @author Zagy
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

    public Ant[] createAnts() {
        return null;
    }

    public int getnumAnts(){
        return _numAnts;
    }
}
