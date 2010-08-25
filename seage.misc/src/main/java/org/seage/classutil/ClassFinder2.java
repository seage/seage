/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.classutil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author rick
 */
public class ClassFinder2
{
    public static ClassInfo[] searchForClasses(Class classObj) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();

        for(File f : searchForJars())
        {
           JarFile jarFile = new JarFile(f);

           //URLClassLoader classLoader = createClassLoader(jarFile, f.getCanonicalPath());
           //classLoader.findClass();

           Enumeration en = jarFile.entries();
           while (en.hasMoreElements()) {
             JarEntry entry = (JarEntry)en.nextElement();
             if( entry.getName().endsWith(".class"))
             {
                String s = entry.getName();
                String className = s.substring(0, s.indexOf(".class")).replace("/", ".");
                try
                {
                    System.out.println(className);
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

    private static File[] searchForJars() throws Exception
    {
        List<File> result = new ArrayList<File>();

        //searchForJarsInDir(new File(rootDir), jarPrefix, result);

        String classPath = System.getProperty("java.class.path",".");
        StringTokenizer tokenizer =
            new StringTokenizer(classPath, File.pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            File f = new File(token);

            if (f.isFile()) {
                String name = f.getName().toLowerCase();
                if (name.endsWith(".zip") || name.endsWith(".jar")) {
                    result.add(f);
                }
            }
        }

        //result.add(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
        //ClassLoader loader = this.getClass().getClassLoader();
        //System.out.println(loader.getResource(this.getClass().getCanonicalName().replace(".", "/")+".class"));

        return result.toArray(new File[0]);
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
}
