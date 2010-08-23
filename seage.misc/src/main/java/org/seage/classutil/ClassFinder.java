/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.classutil;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author rick
 */
public class ClassFinder
{
    public static ClassInfo[] searchForClasses(String rootDir, String jarPrefix, String packagePrefix, Class classObj) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();

        for(File f : searchForJars(rootDir, jarPrefix))
        {
           JarFile jarFile = new JarFile(f);
           Enumeration en = jarFile.entries();
           while (en.hasMoreElements()) {
             JarEntry entry = (JarEntry)en.nextElement();
             if(entry.getName().startsWith(packagePrefix.replace(".", "/")) && entry.getName().endsWith(".class"))
             {
                String s = entry.getName();
                Class c = Class.forName(s.substring(0, s.indexOf(".class")).replace("/", "."));

                if(searchForParent(classObj, c))
                {
                    result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
//                    jarFile.getManifest().getAttributes("Class-Path").
                }
             }
           }
        }

        return result.toArray(new ClassInfo[0]);
    }

    private static File[] searchForJars(String rootDir, String jarPrefix) throws Exception
    {
        List<File> result = new ArrayList<File>();

        searchForJarsInDir(new File(rootDir), jarPrefix, result);

        //result.add(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
        //ClassLoader loader = this.getClass().getClassLoader();
        //System.out.println(loader.getResource(this.getClass().getCanonicalName().replace(".", "/")+".class"));

        return result.toArray(new File[0]);
    }

    private static void searchForJarsInDir(File dir, final String jarPrefix, List<File> result)
    {
        File[] searchResults = dir.listFiles(new FilenameFilter() {

            public boolean accept(File file, String name) {
                 return name.startsWith(jarPrefix) && name.endsWith(".jar");
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
            searchForJarsInDir(d, jarPrefix, result);
        }
    }

    private static boolean searchForParent(Class pattern, Class current)
    {
        ArrayList<Class> cls = new ArrayList<Class>();

        //if(pattern.isInterface())
        for(Class c : current.getInterfaces())
            cls.add(c);
        //else
        if(current.getSuperclass() != null)
            cls.add(current.getSuperclass());

        if(cls.isEmpty())
            return false;
        else
            for(Class c : cls)
            {
                if(c.getName().equals(pattern.getName()))
                    return true;
                else
                    return searchForParent(pattern, c);
            }
        return false;
    }

    private static ClassInfo createClassInfo(JarFile jar, String jarPath, Class cls) throws IOException
    {
        String[] paths = jar.getManifest().getMainAttributes().getValue("Class-Path").split(" ");

        for(int i = 0;i< paths.length;i++)
            if(paths[i].charAt(0) != '/' && paths[i].charAt(1) != ':')
                paths[i] = new File(jarPath).getParent() + "/" + paths[i];

        ClassInfo result = new ClassInfo(jarPath, cls, paths);

        return result;
    }
}
