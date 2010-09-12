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

import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
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
        for (ClassInfo ci : ClassFinder.searchForClasses(IProblemProvider.class, "seage.problem")) {

            System.out.println(ci.getClassName());
            IProblemProvider pp = (IProblemProvider) Class.forName(ci.getClassName()).newInstance();
            for (DataNode alg : pp.getProblemInfo().getDataNode("Algorithms").getDataNodes()) {
                try {
                    String factoryName = alg.getValueStr("name");
                    System.out.println("\t" + factoryName);

                    IAlgorithmFactory f = (IAlgorithmFactory) Class.forName(factoryName).newInstance();
                    f.createAlgorithm(alg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }
}
