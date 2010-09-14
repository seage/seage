/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.experimenter;

import java.util.Map;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemProvider;
import org.seage.classutil.ClassFinder;
import org.seage.classutil.ClassInfo;
import org.seage.data.DataNode;

/**
 *
 * @author RMalek
 */
public class AlgorithmTester {

    public void test() throws Exception {
        System.out.println("Testing algorithms:");
        System.out.println("-------------------");

        Map<String, IProblemProvider> providers = ProblemProvider.getProblemProviders();

        for(String problemId : providers.keySet())
        {
            try
            {
                IProblemProvider pp = providers.get(problemId);
                DataNode pi = pp.getProblemInfo();
                String name = pi.getValueStr("name");
                System.out.println(name);

                for(DataNode alg : pi.getDataNode("Algorithms").getDataNodes())
                {
                    try {
                        String factoryName = alg.getValueStr("factoryClass");
                        System.out.println("\t" + factoryName);

                        IAlgorithmFactory f = (IAlgorithmFactory) Class.forName(factoryName).newInstance();
                        f.createAlgorithm();
                    } catch (Exception ex) {
                        //ex.printStackTrace();
                        System.err.println(problemId+"/"+alg.getValueStr("id")+": "+ex.toString());
                    }
                    //System.out.println("\t"+alg.getValueStr("id")/*+" ("+alg.getValueStr("id")+")"*/);
                }
            }
            catch(Exception ex)
            {
                System.err.println(problemId+": "+ex.getLocalizedMessage());
            }
        }
    }
}
