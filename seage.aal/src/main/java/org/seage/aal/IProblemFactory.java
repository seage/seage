package org.seage.aal;

import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public interface IProblemFactory
{
    IProblemProvider createProblemProvider(DataNode parameters) throws Exception;
    IPhenotypeEvaluator createPhenotypeEvaluator(DataNode parameters) throws Exception;
}
