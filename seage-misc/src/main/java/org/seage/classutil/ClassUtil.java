/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.classutil;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.util.ArrayList;
import java.util.List;


/**
 * .
 * @author Richard Malek
 */
public class ClassUtil {
  public static ClassInfo[] searchForClasses(Class<?> classObj, String pkgName) throws Exception {
    ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
    ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive(pkgName);

    List<ClassInfo> result = new ArrayList<ClassInfo>();
    for (ClassPath.ClassInfo ci : classes) {
      String p = ci.getName();

      String className = p.replace(".class", "").replace('/', '.').replace('\\', '.');
      if (!className.startsWith(pkgName)) {
        continue;
      }

      Class<?> c = Class.forName(className);
      if (classObj.isAssignableFrom(c)) {
        result.add(new ClassInfo(c.getCanonicalName(), null));
      }
    }
    return result.toArray(new ClassInfo[0]);
  }

  public static String[] searchForInstances(final String instanceDir, String pkgName)
      throws Exception {
    ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
    ImmutableSet<ClassPath.ResourceInfo> resources = classPath.getResources();

    List<String> result = new ArrayList<String>();
    pkgName = pkgName.replace('.', '/');
    for (ClassPath.ResourceInfo ri : resources) {
      String resName = ri.getResourceName();
      resName = resName.replace(System.getProperty("file.separator"), "/");

      if (resName.contains(".class")) {
        continue;
      }

      if (resName.startsWith(pkgName) && resName.contains(instanceDir)
          && !resName.endsWith(instanceDir + "/")) {
        if (!resName.startsWith("/")) {
          resName = "/" + resName;
        }

        result.add(resName);
      }

    }
    return result.toArray(new String[0]);
  }
}
