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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Richard Malek
 */
public class ClassUtil
{
    private static List<String> paths = new ClassPathAnalyzer("seage.problem").analyzeClassPath();

    public static ClassInfo[] searchForClasses(Class<?> classObj, String pkgName) throws Exception
    {
        List<ClassInfo> result = new ArrayList<ClassInfo>();
        for (String p : paths)
        {

            if (!p.contains(".class"))
                continue;

            String className = p.replace(".class", "").replace('/', '.').replace('\\', '.');
            if (!className.startsWith(pkgName))
                continue;

            Class<?> c = Class.forName(className);
            if (searchForParent(classObj, c))
                //result.add(createClassInfo(jarFile, f.getCanonicalPath(), c));
                result.add(new ClassInfo(c.getCanonicalName(), null));
        }
        return result.toArray(new ClassInfo[0]);
    }

    public static String[] searchForInstancesInJar(final String instanceDir, String pkgName) throws Exception
    {
        List<String> result = new ArrayList<String>();
        pkgName = pkgName.replace('.', '/');
        for (String resName : paths)
        {
            resName = resName.replace(System.getProperty("file.separator"), "/");

            if (resName.contains(".class"))
                continue;

            if (resName.startsWith(pkgName) && resName.contains(instanceDir) && !resName.endsWith(instanceDir + "/"))
            {
                if (!resName.startsWith("/"))
                    resName = "/" + resName;

                result.add(resName);
            }

        }
        return result.toArray(new String[0]);
    }

    private static boolean searchForParent(Class<?> pattern, Class<?> current)
    {
        ArrayList<Class<?>> cls = new ArrayList<Class<?>>();

        //if(pattern.isInterface())
        for (Class<?> c : current.getInterfaces())
            cls.add(c);
        //else
        if (current.getSuperclass() != null)
            cls.add(current.getSuperclass());

        if (cls.isEmpty())
            return false;
        else
            for (Class<?> c : cls)
            {
                if (c.getName().equals(pattern.getName()))
                    return true;
                else
                    return searchForParent(pattern, c);
            }
        return false;
    }

}
