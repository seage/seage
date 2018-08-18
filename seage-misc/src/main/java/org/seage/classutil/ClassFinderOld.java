/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.classutil;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author Richard Malek
 */
@Deprecated
@SuppressWarnings(value = { "all" })
public class ClassFinderOld
{
    public static ClassInfo[] searchForClasses(String rootDir, String jarPrefix, String packagePrefix, Class classObj)
            throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();

        for (File f : searchForJars(rootDir, jarPrefix))
        {
            JarFile jarFile = new JarFile(f);

            URLClassLoader classLoader = createClassLoader(jarFile, f.getCanonicalPath());
            //classLoader.findClass();

            Enumeration en = jarFile.entries();
            while (en.hasMoreElements())
            {
                JarEntry entry = (JarEntry) en.nextElement();
                if (entry.getName().startsWith(packagePrefix.replace(".", "/")) && entry.getName().endsWith(".class"))
                {
                    String s = entry.getName();
                    String className = s.substring(0, s.indexOf(".class")).replace("/", ".");
                    try
                    {
                        System.out.println(className);
                        Class c = Class.forName(className, false, classLoader);
                        //Class c = classLoader.loadClass(className);

                        if (checkForParent(classObj, c))
                            //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                            result.add(new ClassInfo(c.getCanonicalName(), classLoader.getURLs()));

                    }
                    catch (Error er)
                    {
                        System.err.println(f.getCanonicalPath() + " - " + s + " - " + er.toString());
                        for (URL u : classLoader.getURLs())
                            System.err.println("\t" + u.toString());
                        //er.printStackTrace();
                        //System.err.println();
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
        File[] searchResults = dir.listFiles(new FilenameFilter()
        {

            @Override
            public boolean accept(File file, String name)
            {
                return name.startsWith(jarPrefix) && name.endsWith(".jar");
            }
        });

        for (File f : searchResults)
            result.add(f);

        for (File d : dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                return file.isDirectory();
            }
        }))
        {
            searchForJarsInDir(d, jarPrefix, result);
        }
    }

    public static boolean checkForParent(Class current, Class pattern)
    {
        ArrayList<Class> cls = new ArrayList<Class>();

        //if(pattern.isInterface())
        for (Class c : current.getInterfaces())
            cls.add(c);
        //else
        if (current.getSuperclass() != null)
            cls.add(current.getSuperclass());

        if (cls.isEmpty())
            return false;
        else
            for (Class c : cls)
            {
                if (c.getName().equals(pattern.getName()))
                    return true;
                else
                    return checkForParent(pattern, c);
            }
        return false;
    }

    private static URLClassLoader createClassLoader(JarFile jarFile, String jarPath) throws Exception
    {
        String[] paths = jarFile.getManifest().getMainAttributes().getValue("Class-Path").split(" ");
        URL[] urls = new URL[1/*paths.length*/];

        urls[0] = new URL("file://" + jarPath);
        URLClassLoader result = new URLClassLoader(urls);

        Class[] parameters = new Class[] { URL.class };
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

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class[] getClassesInPackage(String packageName) throws ClassNotFoundException, IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements())
        {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs)
        {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
        {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files)
        {
            if (file.isDirectory())
            {
                assert!file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }
            else if (file.getName().endsWith(".class"))
            {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static String getJarPath(String pkgName)
    {
        pkgName = pkgName.replace('.', '/');
        URL u = Thread.currentThread().getContextClassLoader().getResource(pkgName);
        String jar = u.getFile();
        if (jar.contains(".jar"))
            return jar.split("!")[0].split("file:")[1];
        else
            return null;
    }
}
