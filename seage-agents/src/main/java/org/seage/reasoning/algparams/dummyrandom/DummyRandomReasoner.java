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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams.dummyrandom;

import org.seage.reasoning.algparams.Policy;
import org.seage.reasoning.algparams.Policy.Attribute;
import org.seage.reasoning.algparams.Reasoner;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReportEvaluator;

/**
 *
 * @author rick
 */
public class DummyRandomReasoner extends  Reasoner
{

    public DummyRandomReasoner(Attribute[] atts, AlgorithmReportEvaluator re)
    {
        super(atts, re);
        _attributes = atts;
        _runtimeEvaluator = re;
    }

    public Policy getPolicy()
    {
        return new DummyRandomPolicy(_attributes, _runtimeEvaluator);
    }

    public void putPolicyReport(AlgorithmReport report)
    {
        // throw new UnsupportedOperationException("This method should not have been invoked.");
    }

}
