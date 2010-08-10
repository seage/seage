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
package org.seage.aal;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.seage.data.DataNode;

/**
 * Implementation of IProblemProvider interface
 *
 * @author Richard Malek
 */
public class ProblemProvider implements IProblemProvider
{

    @Override
    public Object[][] generateInitialSolutions(int numSolutions) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataNode getProblemInfo() throws Exception
    {
        DataNode result = new DataNode("ProblemInfo");

        result.putDataNode(new DataNode("Instances"));
        
        DataNode alg = new DataNode("Algorithms");

        String pkg = this.getClass().getPackage().getName();
        alg.putValue("package", pkg);

        for(String s : searchForAlgorithms(pkg))
        {
            DataNode algDn = new DataNode("Algorithm");
            algDn.putValue("name", s);
            alg.putDataNode(algDn);
        }
        result.putDataNode(alg);

        return result;
    }

    @Override
    public IAlgorithmAdapter initAlgorithm(DataNode params)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IPhenotypeEvaluator initPhenotypeEvaluator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initProblemInstance(DataNode params) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visualizeSolution(Object[] solution) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    ///////////////////////////////////////////////////////////////////////////

    private String[] searchForAlgorithms(String packagePrefix) throws Exception
    {
        List<String> result = new ArrayList<String>();

        for(File f : searchForJars())
        {
           JarFile jarFile = new JarFile(f);
           Enumeration en = jarFile.entries();
           while (en.hasMoreElements()) {
             JarEntry entry = (JarEntry)en.nextElement();
             if(entry.getName().startsWith(packagePrefix.replace(".", "/")) && entry.getName().endsWith(".class"))
             {
                

                String s = entry.getName();
                Class c = Class.forName(s.substring(0, s.indexOf(".class")).replace("/", "."));
                //if(c.isInstance(IAlgorithmFactory.class))
                //    result.add(c.getCanonicalName());
                for(Class i : c.getInterfaces())
                {
                    //System.out.println(i.getName());
                    if(i.getName().equals(IAlgorithmFactory.class.getName()))
                        result.add(c.getCanonicalName());
                }
                //IInterface nc = (IInterface)c.newInstance();
                //nc.foo();
             }
           }
        }
        //    result.add(f.getName());

        return result.toArray(new String[0]);
    }

    private File[] searchForJars() throws Exception
    {
        List<File> result = new ArrayList<File>();
        
        searchForJarsInDir(new File("."), result);

        //result.add(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
        //ClassLoader loader = this.getClass().getClassLoader();
        //System.out.println(loader.getResource(this.getClass().getCanonicalName().replace(".", "/")+".class"));

        return result.toArray(new File[0]);
    }

    private void searchForJarsInDir(File dir, List<File> result)
    {
        File[] searchResults = dir.listFiles(new FilenameFilter() {

            public boolean accept(File file, String name) {
                 return name.startsWith("seage.problem") && name.endsWith(".jar");
            }
        });

        for(File f : searchResults)
            result.add(f);

        for(File d : dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        }))
        {
            searchForJarsInDir(d, result);
        }
    }

}
