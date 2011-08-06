/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.aal.algorithm;

import org.seage.aal.algorithm.ProblemInstance;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Rick
 */
public interface IPhenotypeEvaluator extends Comparator<double[]>, Serializable
{
    double[] evaluate(Object[] phenotypeSubject, ProblemInstance instance) throws Exception;
}