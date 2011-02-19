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

package org.seage.aal.algorithm.genetics;
import org.seage.aal.reporting.AlgorithmReport;
import org.seage.aal.reporting.AlgorithmReportEvaluator;


/**
 *
 * @author rick
 */
public class GeneticAlgorithmRuntimeEvaluator extends AlgorithmReportEvaluator
{

    @Override
    public int evaluate(AlgorithmReport report)
    {
        //InputStream is = AlgorithmAdapter.class.getResourceAsStream("reportSchema.xsd");
        try
        {
            return Math.max(report.getDataNode("statistics").getValueInt("numberOfNewSolutions")-5, 0);
        }
        catch(Exception ex)
        {
            return 0;
        }
    }

    @Override
    public int compare(AlgorithmReport o1, AlgorithmReport o2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
