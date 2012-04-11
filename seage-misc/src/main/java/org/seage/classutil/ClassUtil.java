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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rick
 */
public class ClassUtil
{
    private static List<String> paths = new ClassPathAnalyzer("seage.problem").analyzeClassPath();
    
    public static ClassInfo[] searchForClasses(Class<?> classObj, String pkgName) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();
        for(String p : paths)
        {
            if(!p.contains(".class"))
                continue;
            
            String className = p.replace(".class", "").replace('/', '.');
            
            if(!className.startsWith(pkgName))
                continue;
            
            Class<?> c = Class.forName(className);
                
            if(searchForParent(classObj, c))
                //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                result.add(new ClassInfo(c.getCanonicalName(), null));
        }
        return result.toArray(new ClassInfo[0]);
    }
    
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

    private static boolean searchForParent(Class<?> pattern, Class<?> current)
    {
        ArrayList<Class<?>> cls = new ArrayList<Class<?>>();

        //if(pattern.isInterface())
        for(Class<?> c : current.getInterfaces())
            cls.add(c);
        //else
        if(current.getSuperclass() != null)
            cls.add(current.getSuperclass());

        if(cls.isEmpty())
            return false;
        else
            for(Class<?> c : cls)
            {
                if(c.getName().equals(pattern.getName()))
                    return true;
                else
                    return searchForParent(pattern, c);
            }
        return false;
    }


}
