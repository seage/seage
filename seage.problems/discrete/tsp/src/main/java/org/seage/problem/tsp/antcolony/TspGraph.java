/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.antcolony;

import java.util.ArrayList;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    private ArrayList<TspNode> _tspNodeList = new ArrayList<TspNode>();
    private ArrayList<TspEdge> _tspEdgeList = new ArrayList<TspEdge>();

    public TspGraph(City[] cities) {
        //super();
        fillNodeMap(cities);
        fillEdgeMap();
        //setEdgesLenght();
    }

//    public void setEdgesLenght(){
//    }

    public void fillNodeMap(City[] cities){
        for (City c : cities) {
            addCity(c);
        }
    }

    public void addCity(City c) {
        _id = _tspNodeList.size();
        _tspNodeList.add(new TspNode(_id, c.X, c.Y));
        _nodeList.add(new Node(_id));
    }

    public void fillEdgeMap() {
        boolean same = false; //stejne
        for (TspNode i : _tspNodeList) {
            for (TspNode j : _tspNodeList) {
                if (i.getId() != j.getId()) {
                    TspEdge theEdge = new TspEdge(i, j);
                    for (TspEdge k : _tspEdgeList) {
                        if (k.getOriginator().equals(j) && k.getDestination().equals(i)) {
                            same = true;
                        }
                    }
                    if (!same) {
                        _tspEdgeList.add(theEdge);
                        _edgeList.add(theEdge);
                    }
                }
                same = false;
            }
        }
        for (Node i : _tspNodeList) {
            for (Edge j : _tspEdgeList) {
                if (j.getOriginator().equals(i) || j.getDestination().equals(i)) {
                    i.addConectionEdge(j);
                }
            }
        }
    }
}
