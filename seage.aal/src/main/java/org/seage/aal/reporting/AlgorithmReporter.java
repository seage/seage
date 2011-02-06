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
package org.seage.aal.reporting;

import org.seage.data.DataNode;

/**
 * Reports on runtime information
 *
 * @author Richard Malek
 */
public class AlgorithmReporter
{
    private AlgorithmReport _report;

    public AlgorithmReporter(String searchID) throws Exception
    {
        _report = new AlgorithmReport("running");
        _report.putValue("id", searchID);
        _report.putDataNode(new DataNode("parameters"));
        _report.putDataNode(new DataNode("minutes"));
        _report.putDataNode(new DataNode("statistics"));
    }

    public void putParameters(DataNode params) throws Exception
    {
        DataNode paramNode = _report.getDataNode("parameters");
        for(String paramName : params.getNames())
            paramNode.putValue(paramName, params.getValue(paramName));
    }
    public void putNewSolution(long time, long iterNumber, double objVal, String solution) throws  Exception
    {
        DataNode newSol = new DataNode("newSolution");
        newSol.putValue("time", time);
        newSol.putValue("iterNumber", iterNumber);
        newSol.putValue("objVal", objVal);
        newSol.putValue("solution", solution);

        _report.getDataNode("minutes").putDataNode(newSol);
    }

    public void putStatistics(
            long numberOfIter, long numberOfNewSolutions, long lastIterNumberNewSol,
            double initObjVal, double avgObjVal, double bestObjVal) throws Exception
    {
        DataNode stats = _report.getDataNode("statistics");
        stats.putValue("numberOfIter", numberOfIter);
        stats.putValue("numberOfNewSolutions", numberOfNewSolutions);
        stats.putValue("lastIterNumberNewSol", lastIterNumberNewSol);
        stats.putValue("initObjVal", initObjVal);
        stats.putValue("avgObjVal", avgObjVal);
        stats.putValue("bestObjVal", bestObjVal);
    }

    public AlgorithmReport getReport(){return (AlgorithmReport)_report.clone();}
}
