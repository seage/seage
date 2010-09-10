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

import aglobe.platform.Platform;
import org.seage.aal.IProblemProvider;
import org.seage.classutil.ClassFinder;
import org.seage.classutil.ClassInfo;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class Experimenter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            if(args.length > 0 && args[0].equals("-agents"))
            {
                if(args.length<2){
                    System.out.println("Usage: Experimenter -agents path-to-agent-config-xml");
                    return;
                }

                Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config="+args[1], "ProblemAgent:org.seage.ael.agent.ProblemAgent" });
            }
            else
            {
                System.out.println("List of implemented problems and algorithms:");
                System.out.println("--------------------------------------------");
                for(ClassInfo ci : ClassFinder.searchForClasses(IProblemProvider.class))
                {
                    System.out.println(ci.getClassName() );
                    IProblemProvider pp = (IProblemProvider)Class.forName(ci.getClassName()).newInstance();
                    for(DataNode alg : pp.getProblemInfo().getDataNode("Algorithms").getDataNodes())
                        System.out.println("\t"+alg.getValue("name").toString());
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
