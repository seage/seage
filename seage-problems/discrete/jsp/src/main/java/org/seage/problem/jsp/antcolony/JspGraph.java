/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation David Omrai - Jsp implementation
 */

package org.seage.problem.jsp.antcolony;

import java.util.ArrayList;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author Zagy
 * @author David Omrai
 */
public class JspGraph extends Graph {
  private static final Logger log = LoggerFactory.getLogger(JspGraph.class.getName());

  JspPhenotypeEvaluator evaluator;

  private int factor;

  private JspJobsDefinition jobs;

  public JspGraph(JspJobsDefinition jobArary, JspPhenotypeEvaluator evaluator) throws Exception {
    super();
    this.evaluator = evaluator;
    this.jobs = jobArary;
    int allOperCount = 0;
    for (int i = 0; i < this.jobs.getJobsCount(); i++) {
      allOperCount += this.jobs.getJobInfos()[i].getOperationInfos().length;
    }

    this.factor = (int) Math.pow(10, Math.ceil(Math.log10(allOperCount)) + 1);

    // Add the first starting node
    //   /- 1 - 2 - 3 - ... - job1-oper '|
    //  / - 1 - 2 - 3 - ... - job2-oper |
    // s  - 1 - 2 - 3 - ... - job3-oper | n-jobs
    //  \ - 1 - 2 - 3 - ... - job4-oper |
    //   \- 1 - 2 - 3 - ... - job5-oper ,|
    int id = 0;
    // Starting node
    _nodes.put(id, new Node(id));

    for (int idJob = 1; idJob <= jobs.getJobsCount(); idJob++) {
      for (int idOper =
          1; idOper <= jobs.getJobInfos()[idJob - 1].getOperationInfos().length; idOper++) {
        id = idJob * getFactor() + idOper;
        _nodes.put(id, new Node(id));
      }
    }
  }

  public int nodeToJobID(Node node) {
    return node.getID() / getFactor();
  }

  public int nodeToOperID(Node node) {
    return node.getID() % getFactor();
  }

  public int getFactor() {
    return this.factor;
  }

  public void prune(long iteration) {
    if (iteration % 100 != 0) {
      return;
    }
    _edges.sort((e1, e2) -> {
      double a = e1.getLocalPheromone() / e1.getEdgePrice();
      double b = e2.getLocalPheromone() / e2.getEdgePrice();
      if (a > b) {
        return 1;
      }
      if (a < b) {
        return -1;
      }
      return 0;
    });

    try {
      ArrayList<Edge> edgesToRemove = new ArrayList<>(_edges.subList(0, _edges.size() * 1 / 10));

      for (Edge e2 : edgesToRemove) {
        removeEdge(e2);
      }
      int a = 0;
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }
}
