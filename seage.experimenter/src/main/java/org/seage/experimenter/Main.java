/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemProvider;
import org.seage.classpath.ClassFinder;
import org.seage.data.DataNode;
import org.seage.problem.qap.QapProblemProvider;
import org.seage.problem.sat.SatProblemProvider;
import org.seage.problem.tsp.TspProblemProvider;

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
//            IProblemProvider[] providers = new IProblemProvider[]
//            {new TspProblemProvider(), new SatProblemProvider(), new QapProblemProvider()};

//            for(IProblemProvider pp : providers)
//            {
//                System.out.println(pp.getClass().getName());
//                for(DataNode alg : pp.getProblemInfo().getDataNode("Algorithms").getDataNodes())
//                    System.out.println("\t"+alg.getValue("name").toString());
//            }

            for(Class c : ClassFinder.searchForClasses("org.seage.problem", ProblemProvider.class))
            {
                System.out.println(c.getName());
                IProblemProvider pp = (IProblemProvider)c.newInstance();
                for(DataNode alg : pp.getProblemInfo().getDataNode("Algorithms").getDataNodes())
                    System.out.println("\t"+alg.getValue("name").toString());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
