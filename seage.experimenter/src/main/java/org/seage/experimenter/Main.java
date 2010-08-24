/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

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
            for(ClassInfo ci : ClassFinder.searchForClasses("..", "seage.problem", "org.seage.problem", IProblemProvider.class))
            {
                System.out.println(ci.getClassName() );
//                IProblemProvider pp = (IProblemProvider)ci.getClassObj().newInstance();
//                for(DataNode alg : pp.getProblemInfo().getDataNode("Algorithms").getDataNodes())
//                    System.out.println("\t"+alg.getValue("name").toString());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
