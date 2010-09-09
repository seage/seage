/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            if(args[0].equals("-agents"))
            {
                //org.seage.ael.Main.main(args);
                Platform.main(new String[]{"-name", "SEAGE", "-topics", "-gui", "-p", "config="+args[1], "ProblemAgent:org.seage.ael.agent.ProblemAgent" });
            }
            else
            {
                for(ClassInfo ci : ClassFinder.searchForClasses(IProblemProvider.class))
                {
                    System.out.println("+++"+ci.getClassName() );
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
