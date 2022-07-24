/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */
package org.seage.reasoning.algparams.dummyrandom;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.data.DataNode;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import org.seage.reasoning.algparams.Policy;
import java.util.HashMap;

/**
 * @author Richard Malek
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
