/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams.dummy;

import org.seage.aal.reporting.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy;

/**
 * Dummy hypotheses generates random parameters at the beginning (in constructor, if desired)
 * and the parameters are not changed during suggestions.
 * @author rick
 */
public class DummyPolicy extends Policy
{
    protected int _id;

    public DummyPolicy()
    {
        super(null, null);
        _id = hashCode();

    }

    @Override
    public DataNode suggest(AlgorithmReport report) throws Exception
    {
        super.suggest(report);

        DataNode result = report.getDataNode("parameters");

        result.putValue("continue", false);
        
        return result;
    }

    public int getID()
    {
        return _id;
    }
}
