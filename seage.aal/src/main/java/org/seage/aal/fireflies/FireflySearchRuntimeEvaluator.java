/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.fireflies;
import org.seage.aal.fireflies.*;
import org.seage.aal.reporting.AlgorithmReport;
import org.seage.aal.reporting.AlgorithmReportEvaluator;


/**
 *
 * @author rick
 */
public class FireflySearchRuntimeEvaluator extends AlgorithmReportEvaluator
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
