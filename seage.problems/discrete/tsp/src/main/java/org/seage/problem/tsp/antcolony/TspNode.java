/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.antcolony;

import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Zagy
 */
public class TspNode extends Node {
    private double _x = 0;
    private double _y = 0;

    public TspNode(int id, double x, double y) {
        super(id);
        _x = x;
        _y = y;
    }

    public double getX(){
        return _x;
    }

    public double getY(){
        return _y;
    }
}
