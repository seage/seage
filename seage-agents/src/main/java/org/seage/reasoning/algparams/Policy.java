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
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.reasoning.algparams;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReportEvaluator;
import org.seage.data.DataNode;
import java.io.Serializable;

/**
 * @author rick
 * Functor class for generating the algorithm parameters according to the Policy
 */
public abstract class Policy implements Serializable
{
    protected Attribute[] _attrAttributes;
    protected AlgorithmReportEvaluator _runtimeEvaluator;
    
    public Policy(Attribute[] attributes, AlgorithmReportEvaluator runtimeEvaluator)
    {
        _attrAttributes = attributes;
        _runtimeEvaluator = runtimeEvaluator;
    }
    /**
     * @param report Report on previous algorithm run
     * @return Return a ew suggestion according the given policy
     * @throws java.lang.Exception
     */
    public DataNode suggest(AlgorithmReport report) throws Exception
    {
        // TODO: C - Add DataNode validation against the XSD schema
        return null;
    }

    /**
     * @return Returns id of the given policy
     */
    public abstract int getID();

    /**
     * Nested class in Policy class. Dataholder for Policy attributes.
     */
    public static class Attribute implements Serializable
    {

        public String Name;
        public Double MinValue;
        public Double MaxValue;
        public Double InitValue;

        public Attribute(String Name, Double MinValue, Double MaxValue) {
            this.Name = Name;
            this.MinValue = MinValue;
            this.MaxValue = MaxValue;
        }  
    }
}
