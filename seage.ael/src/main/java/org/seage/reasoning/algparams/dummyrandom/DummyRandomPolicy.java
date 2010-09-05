package org.seage.reasoning.algparams.dummyrandom;

import org.seage.aal.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.AlgorithmReportEvaluator;
import org.seage.reasoning.algparams.Policy;
import java.util.HashMap;

/**
 * @author rick
 */
public class DummyRandomPolicy extends Policy
{
    protected int _noChange = 0;
    protected HashMap<Attribute, Double> _initValues;
    protected int _id;

    public DummyRandomPolicy(Attribute[] attributes, AlgorithmReportEvaluator re)
    {
        super(attributes, re);

        _noChange = 0;
        _initValues = new HashMap<Attribute, Double>();
        for(Attribute a : attributes)
            _initValues.put(a, generate(a));
    }

    @Override
    public DataNode suggest(AlgorithmReport report) throws Exception
    {
        DataNode result = (DataNode)report.getDataNode("parameters").clone();
        boolean cont = false;

        if(_runtimeEvaluator.evaluate(report) == 0)
            _noChange++;

        if(_noChange > 5)
            cont = false;

        result.putValue("continue", cont);

        if(cont && _initValues != null)
            return setSelectedParameters(result);

        return result;
    }

    @Override
    public int getID()
    {
        return _id;
    }

    protected DataNode setSelectedParameters(DataNode params)
    {
        for(Attribute a : _attrAttributes)
        {
            double val = _initValues.get(a);
            params.putValue(a.Name, val);
        }
        return params;
    }

    protected Double generate(Attribute a)
    {
        return a.MinValue + (Math.random() * (a.MaxValue - a.MinValue));
    }
    

}
