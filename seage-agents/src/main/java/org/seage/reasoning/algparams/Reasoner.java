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

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReportEvaluator;

/**
 * Reasoner interface hides an implementation of policy evolution
 * @author Richard Malek
 */
public abstract class Reasoner
{
    protected Policy.Attribute[] _attributes;
    protected AlgorithmReportEvaluator _runtimeEvaluator;

    public Reasoner(Policy.Attribute[] atts, AlgorithmReportEvaluator re)
    {
        _attributes = atts;
        _runtimeEvaluator = re;
    }

    /**
     * Selects one policy from the policy pool
     * @param none
     * @return Proposed Policy
     * @throws Exception
     */
    public abstract Policy getPolicy() throws Exception;

    /**
     * Puts report on policy quality
     * @param report Report on algorithm run based on policy
     * @throws Exception
     */
    public abstract void putPolicyReport(AlgorithmReport report) throws Exception;
}
