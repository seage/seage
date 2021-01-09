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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

/**
 *
 * @author Richard Malek
 */
public class ClassUtil {
  public static ClassInfo[] searchForClasses(Class<?> classObj, String pkgName) throws Exception {
    ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
    ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive(pkgName);

    List<ClassInfo> result = new ArrayList<ClassInfo>();
    for (ClassPath.ClassInfo ci : classes) {
      String p = ci.getName();// u.getPath();

      String className = p.replace(".class", "").replace('/', '.').replace('\\', '.');
      if (!className.startsWith(pkgName))
        continue;

      Class<?> c = Class.forName(className);
      if (searchForParent(classObj, c))
        result.add(new ClassInfo(c.getCanonicalName(), null));
    }
    return result.toArray(new ClassInfo[0]);
  }

  public static String[] searchForInstancesInJar(final String instanceDir, String pkgName) throws Exception {
    ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
    ImmutableSet<ClassPath.ResourceInfo> resources = classPath.getResources();

    List<String> result = new ArrayList<String>();
    pkgName = pkgName.replace('.', '/');
    for (ClassPath.ResourceInfo ri : resources) {
      String resName = ri.getResourceName();
      resName = resName.replace(System.getProperty("file.separator"), "/");

      if (resName.contains(".class"))
        continue;

      if (resName.startsWith(pkgName) && resName.contains(instanceDir) && !resName.endsWith(instanceDir + "/")) {
        if (!resName.startsWith("/"))
          resName = "/" + resName;

        result.add(resName);
      }

    }
    return result.toArray(new String[0]);
  }

  private static boolean searchForParent(Class<?> pattern, Class<?> current) {
    ArrayList<Class<?>> cls = new ArrayList<Class<?>>();

    // if(pattern.isInterface())
    for (Class<?> c : current.getInterfaces())
      cls.add(c);
    // else
    if (current.getSuperclass() != null)
      cls.add(current.getSuperclass());

    if (cls.isEmpty())
      return false;
    else
      for (Class<?> c : cls) {
        if (c.getName().equals(pattern.getName()))
          return true;
        else
          return searchForParent(pattern, c);
      }
    return false;
  }

}
