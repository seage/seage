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
package org.seage.aal.reporter;

import org.seage.aal.data.AlgorithmParams;
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
        _report = new AlgorithmReport("AlgorithmReport");
        //_running.putValue("id", searchID);
        //_running.putDataNode(new DataNode("Parameters"));
        _report.putDataNode(new DataNode("Minutes"));
        _report.putDataNode(new DataNode("Statistics"));
       
    }

    public void putParameters(AlgorithmParams params) throws Exception
    {
        _report.putValue("created", System.currentTimeMillis());
        _report.putValue("algorithmID", params.getValue("id"));
        _report.putValue("problemID", params.getValue("problemID"));
        _report.putValue("instance", params.getValue("instance"));
        _report.putDataNode(params.getDataNode("Parameters"));
//        for(String paramName : params.getDataNode("Parameters").getNames())
//            paramNode.putValue(paramName, params.getDataNode("Parameters").getValue(paramName));

//        DataNode a = new DataNode("a");
//        a.putDataNode(params);
//        _report.putDataNode(a);
    }
    public void putNewSolution(long time, long iterNumber, double objVal, String solution) throws  Exception
    {
        DataNode newSol = new DataNode("newSolution");
        newSol.putValue("time", time);
        newSol.putValue("iterNumber", iterNumber);
        newSol.putValue("objVal", objVal);
        newSol.putValue("solution", solution);

        _report.getDataNode("Minutes").putDataNode(newSol);
    }

    public void putStatistics(
            long numberOfIter, long numberOfNewSolutions, long lastIterNumberNewSol,
            double initObjVal, double avgObjVal, double bestObjVal) throws Exception
    {
        DataNode stats = _report.getDataNode("Statistics");
        stats.putValue("numberOfIter", numberOfIter);
        stats.putValue("numberOfNewSolutions", numberOfNewSolutions);
        stats.putValue("lastIterNumberNewSol", lastIterNumberNewSol);
        stats.putValue("initObjVal", initObjVal);
        stats.putValue("avgObjVal", avgObjVal);
        stats.putValue("bestObjVal", bestObjVal);
    }

    public AlgorithmReport getReport() throws Exception{
        return (AlgorithmReport)_report.clone();
    }
}
