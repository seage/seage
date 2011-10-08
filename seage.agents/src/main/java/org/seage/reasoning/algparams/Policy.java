/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.reasoning.algparams;

import org.seage.aal.reporting.AlgorithmReport;
import org.seage.aal.reporting.AlgorithmReportEvaluator;
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
