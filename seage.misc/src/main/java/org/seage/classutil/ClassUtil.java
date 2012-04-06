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
    private static List<String> paths = new ClassPathAnalyzer("seage.problem").analyzeClassPath();
    
    public static ClassInfo[] searchForClasses(Class classObj, String pkgName) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();
        for(String p : paths)
        {
            if(!p.contains(".class"))
                continue;
            
            String className = p.replace(".class", "").replace('/', '.');
            
            if(!className.startsWith(pkgName))
                continue;
            
            Class c = Class.forName(className);
                
            if(searchForParent(classObj, c))
                //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                result.add(new ClassInfo(c.getCanonicalName(), null));
        }
        return result.toArray(new ClassInfo[0]);
    }
    
    
//    public static ClassInfo[] searchForClasses(Class classObj, String pkgPrefix) throws Exception
//    {
//        return searchForClasses(classObj, searchForJars(".", pkgPrefix));
//    }

//    public static ClassInfo[] searchForClasses(Class classObj, String rootDir, String pkgPrefix) throws Exception
//    {
//        return searchForClasses(classObj, searchForJars(rootDir, pkgPrefix));
//    }

//    public static ClassInfo[] searchForClasses(Class classObj, String pkgName) throws Exception
//    {
//        return searchForClasses(classObj, new File[]{new File(getJarPath(pkgName))});
//    }

//    private static ClassInfo[] searchForClasses(Class classObj, File[] jars) throws Exception
//    {
//        List<ClassInfo> result = new ArrayList<ClassInfo>();
//
//        for(File f : jars)
//        {
//           JarFile jarFile = new JarFile(f);
//
//           //URLClassLoader classLoader = createClassLoader(jarFile, f.getCanonicalPath());
//           //classLoader.findClass();
//
//           //Enumeration<JarEntry> en = jarFile.entries();
//           //while (en.hasMoreElements()) {
//           for(JarEntry entry : Collections.list(jarFile.entries())){
//             //JarEntry entry = en.nextElement();
//             if( entry.getName().endsWith(".class"))
//             {
//                String s = entry.getName();
//                String className = s.substring(0, s.indexOf(".class")).replace("/", ".");
//                try
//                {
//                    //System.out.println(className);
//                    //Class c = Class.forName(className, false, classLoader);
//                    Class c = Class.forName(className);
//                
//                    if(searchForParent(classObj, c))
//                        //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
//                        result.add(new ClassInfo(c.getCanonicalName(), null));
//
//                }
//                catch(Exception ex)
//                {
//                    System.err.println(f.getCanonicalPath()+" - "+s +" - " + ex.toString());
//                }
//                catch(Error er)
//                {
//                    System.err.println(f.getCanonicalPath()+" - "+s +" - " + er.toString());
////                    for(URL u : classLoader.getURLs())
////                        System.err.println("\t"+u.toString());
//                    //er.printStackTrace();
//                    //System.err.println();
//                }
//             }
//           }
//        }
//
//        return result.toArray(new ClassInfo[0]);
//    }

//    public static ClassInfo[] searchForClassesInJar(Class targetClass, Class sourceClass) throws Exception
//    {
//        //File jarFile = new File (sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI());
//        return searchForClasses(targetClass, new File[]{jarFile});
//    }

//    public static String[] searchForInstancesInJar(final String instanceDir, Class sourceClass) throws Exception
//    {
//        ArrayList<String> result = new ArrayList<String>();
//        File file = new File (sourceClass.getProtectionDomain().getCodeSource().getLocation().toURI());
//        JarFile jarFile = new JarFile(file);
//        JarEntry je = jarFile.getJarEntry(instanceDir);
//        for(JarEntry entry : Collections.list(jarFile.entries())){
//            String name = entry.getName();            
//            if(name.contains(instanceDir) && !entry.isDirectory())
//                result.add("/"+name);
//        }
//
//        return result.toArray(new String[0]);
//    }
    
    public static String[] searchForInstancesInJar(final String instanceDir, String pkgName) throws Exception
    {
        List<String> result = new ArrayList<String>();
        pkgName = pkgName.replace('.', '/');
        for(String resName : paths)
        {
            if(resName.contains(".class"))
                continue;  
            
            if(resName.startsWith(pkgName) && resName.contains(instanceDir) && !resName.endsWith(instanceDir+"/"))
            {
                if(!resName.startsWith("/"))
                    resName="/"+resName ;
                
                result.add(resName);
            }
                
            
            //Class c = Class.forName(className);
                
            //if(searchForParent(classObj, c))
                //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                
        }
        return result.toArray(new String[0]);
    }
    
//    private static File[] searchForJars(String rootDir, String jarPrefix) throws Exception
//    {
//        List<File> result = new ArrayList<File>();
//        
//        String cp = System.getProperty("java.class.path");
//        String delim = System.getProperty("path.separator");
//        
//        String[] paths = cp.split(delim);
//        
//        for(String s : paths)
//        {
//            if(!s.contains(jarPrefix))
//                continue;
//            File f = new File(s);
//            if(f.isFile())
//                result.add(f);
//        }
//        
//        return result.toArray(new File[0]);
//    }

//    private static List<File> searchForJarsInDir(File dir, final String jarPrefix)
//    {
//
//        List<File> result = new ArrayList<File>();
//        try
//        {
//            File[] searchResults = dir.listFiles(new FilenameFilter() {
//
//                public boolean accept(File file, String name) {
//                     return name.startsWith(jarPrefix) && name.endsWith(".jar");
//                }
//            });
//
//            for(File f : searchResults)
//                result.add(f);
//
//            for(File d : dir.listFiles(new FileFilter() {
//                public boolean accept(File file) {
//                    return file.isDirectory();
//                }
//            }))
//            {
//                result.addAll( searchForJarsInDir(d, jarPrefix));
//            }
//        }
//        catch(Exception ex)
//        {
//            System.err.println("Unable to read "+dir);
//        }
//        return result;
//    }


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


}
