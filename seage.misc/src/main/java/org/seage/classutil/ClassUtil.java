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
package org.seage.classutil;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author rick
 */
public class ClassUtil
{
    public static ClassInfo[] searchForClasses(Class classObj, String pkgPrefix) throws Exception
    {
        return searchForClasses(classObj, searchForJars(".", pkgPrefix));
    }

    public static ClassInfo[] searchForClasses(Class classObj, String rootDir, String pkgPrefix) throws Exception
    {
        return searchForClasses(classObj, searchForJars(rootDir, pkgPrefix));
    }

//    public static ClassInfo[] searchForClasses(Class classObj, String pkgName) throws Exception
//    {
//        return searchForClasses(classObj, new File[]{new File(getJarPath(pkgName))});
//    }

    private static ClassInfo[] searchForClasses(Class classObj, File[] jars) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();

        for(File f : jars)
        {
           JarFile jarFile = new JarFile(f);

           //URLClassLoader classLoader = createClassLoader(jarFile, f.getCanonicalPath());
           //classLoader.findClass();

           //Enumeration<JarEntry> en = jarFile.entries();
           //while (en.hasMoreElements()) {
           for(JarEntry entry : Collections.list(jarFile.entries())){
             //JarEntry entry = en.nextElement();
             if( entry.getName().endsWith(".class"))
             {
                String s = entry.getName();
                String className = s.substring(0, s.indexOf(".class")).replace("/", ".");
                try
                {
                    //System.out.println(className);
                    //Class c = Class.forName(className, false, classLoader);
                    Class c = Class.forName(className);
                
                    if(searchForParent(classObj, c))
                        //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                        result.add(new ClassInfo(c.getCanonicalName(), null));

                }
                catch(Error er)
                {
                    System.err.println(f.getCanonicalPath()+" - "+s +" - " + er.toString());
//                    for(URL u : classLoader.getURLs())
//                        System.err.println("\t"+u.toString());
                    //er.printStackTrace();
                    //System.err.println();
                }
             }
           }
        }

        return result.toArray(new ClassInfo[0]);
    }

    public static ClassInfo[] searchForClassesInJar(Class targetClass, Class sourceClass) throws Exception
    {
        File jarFile = new File (sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI());
        return searchForClasses(targetClass, new File[]{jarFile});
    }

    public static String[] searchForInstancesInJar(final String instanceDir, Class sourceClass) throws Exception
    {
        ArrayList<String> result = new ArrayList<String>();
        File file = new File (sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI());
        JarFile jarFile = new JarFile(file);
        JarEntry je = jarFile.getJarEntry(instanceDir);
        for(JarEntry entry : Collections.list(jarFile.entries())){
            String name = entry.getName();            
            if(name.contains(instanceDir) && !entry.isDirectory())
                result.add(name);
        }

        return result.toArray(new String[0]);
    }

//    private static File[] searchForJars() throws Exception
//    {
//        List<File> result = new ArrayList<File>();
//
//        //searchForJarsInDir(new File(rootDir), jarPrefix, result);
//
//        String classPath = System.getProperty("java.class.path",".");
//        System.out.println(classPath);
//        StringTokenizer tokenizer =
//            new StringTokenizer(classPath, File.pathSeparator);
//        while (tokenizer.hasMoreTokens()) {
//            String token = tokenizer.nextToken();
//            File f = new File(token);
//
//            if (f.isFile()) {
//                String name = f.getName().toLowerCase();
//                if (name.startsWith("seage.problem") && (name.endsWith(".zip") || name.endsWith(".jar"))) {
//                    result.add(f);
//                }
//            }
//        }
//
//        //result.add(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
//        //ClassLoader loader = this.getClass().getClassLoader();
//        //System.out.println(loader.getResource(this.getClass().getCanonicalName().replace(".", "/")+".class"));
//
//        return result.toArray(new File[0]);
//    }

    private static File[] searchForJars(String rootDir, String jarPrefix) throws Exception
    {
        List<File> result = searchForJarsInDir(new File(rootDir), jarPrefix);
        return result.toArray(new File[0]);
    }

    private static List<File> searchForJarsInDir(File dir, final String jarPrefix)
    {
        List<File> result = new ArrayList<File>();
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
            result.addAll( searchForJarsInDir(d, jarPrefix));
        }

        return result;
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

    private static URLClassLoader createClassLoader(JarFile jarFile, String jarPath) throws Exception
    {
        String[] paths = jarFile.getManifest().getMainAttributes().getValue("Class-Path").split(" ");
        URL[] urls = new URL[1/*paths.length*/];

        urls[0] = new URL("file://" +jarPath);
        URLClassLoader result = new URLClassLoader(urls);

        Class[] parameters = new Class[]{URL.class};
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
        method.setAccessible(true);

        //urls[1] = new URL("file:///mirror/rick/Projects/seage/seage.problems/discrete/qap/dist/lib/seage.metaheuristics.jar");

//        for(int i = 0;i< urls.length;i++)
//            if(paths[i].charAt(0) != '/' && paths[i].charAt(1) != ':')
////                urls[i+1] = new URL("file://" +new File(jarPath).getParent() + "/" + paths[i]);
//                method.invoke(result, new Object[]{new URL("file://" +new File(jarPath).getParent() + "/" + paths[i])});

        //urls[paths.length] = new URL("file://" +jarPath);

        return result;//new URLClassLoader(urls/*, ClassLoader.getSystemClassLoader()*/);
    }

//    private static ClassInfo createClassInfo(JarFile jar, String jarPath, Class cls) throws IOException
//    {
//        String[] paths = jar.getManifest().getMainAttributes().getValue("Class-Path").split(" ");
//
//        for(int i = 0;i< paths.length;i++)
//            if(paths[i].charAt(0) != '/' && paths[i].charAt(1) != ':')
//                paths[i] = new File(jarPath).getParent() + "/" + paths[i];
//
//        ClassInfo result = new ClassInfo(cls.getCanonicalName(), null);
//
//        return result;
//    }
//    public static String getJarPath(String pkgName)
//    {
//        pkgName = pkgName.replace('.', '/');
//        URL u = Thread.currentThread().getContextClassLoader().getResource(pkgName);
//        String jar = u.getFile();
//        if(jar.contains(".jar"))
//            return jar.split("!")[0].split("file:")[1];
//        else
//            return null;
//    }
}
