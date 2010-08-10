/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.classpath;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author rick
 */
public class ClassFinder
{
    public static Class[] searchForClasses(String packagePrefix, Class classObj) throws Exception
    {
        List<Class> result = new ArrayList<Class>();

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
                if(c.getSuperclass().getName().equals(classObj.getName()))
                {
                    result.add(c);
                    continue;
                }

                for(Class i : c.getInterfaces())
                {
                    //System.out.println(i.getName());
                    if(i.getName().equals(classObj.getName()))
                        result.add(c);
                }
                //IInterface nc = (IInterface)c.newInstance();
                //nc.foo();
             }
           }
        }
        //    result.add(f.getName());

        return result.toArray(new Class[0]);
    }

    private static File[] searchForJars() throws Exception
    {
        List<File> result = new ArrayList<File>();

        searchForJarsInDir(new File("."), result);

        //result.add(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
        //ClassLoader loader = this.getClass().getClassLoader();
        //System.out.println(loader.getResource(this.getClass().getCanonicalName().replace(".", "/")+".class"));

        return result.toArray(new File[0]);
    }

    private static void searchForJarsInDir(File dir, List<File> result)
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
