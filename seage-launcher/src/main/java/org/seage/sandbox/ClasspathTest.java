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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

/**
 * 
 * @author Richard Malek
 */
public class ClasspathTest {

  public static void main(String[] args) throws Exception {
    new ClasspathTest().foo();
    ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
    ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive("org.seage.problem");

    // for (String c : classes) {
    // System.out.println(c);

    // Class.forName(c);
    // }

    System.out.println("CRC: " + classes.size());
  }

  public void foo() {
    // System.out.println(this.getClass().getClassLoader().getParent().getParent());
    try {
      System.out.println(ClassLoader.getSystemClassLoader().toString());

      String cp = System.getProperty("java.class.path");
      String delim = System.getProperty("path.separator");

      String[] paths = cp.split(delim);
      List<CPItem> cpItems = new ArrayList<CPItem>();

      // List<String> jarPaths = new ArrayList<String>();

      for (String s : paths) {
        System.out.println(s);

        File f = new File(s);
        if (f.isDirectory()) {
          cpItems.add(new DirCPItem(s));
        }
        if (f.isFile() && f.getName().endsWith(".jar")) {
          JarCPItem jcpi = new JarCPItem(s);
          cpItems.add(jcpi);
          cpItems.addAll(jcpi.getDependencyJars());
        }
      }

      for (CPItem i : cpItems) {
        if (i.getPath().contains("seage.problem"))
          System.out.println(i.toString());

        // i.process();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    // ClassLoader cl = (ClassLoader) getClass().getClassLoader();
    // try {
    // URL url = cl.findResource("META-INF/MANIFEST.MF");
    // Manifest manifest = new Manifest(url.openStream());
    // // do stuff with it
    // } catch (IOException E) {
    // // handle
    // }
  }

  private class CPItem {
    protected String _path;

    public CPItem(String path) {
      _path = path;
    }

    @Override
    public String toString() {
      return getClass().getName() + " - " + _path;
    }

    public void process() throws IOException {
    };

    public String getPath() {
      return _path;
    }
  }

  private class DirCPItem extends CPItem {

    public DirCPItem(String path) {
      super(path);
    }

    @Override
    public void process() throws IOException {
      super.process();
    }

    @Override
    public int hashCode() {
      return _path.hashCode();
    }

  }

  private class JarCPItem extends CPItem {

    public JarCPItem(String path) {
      super(path);
    }

    @Override
    public void process() throws IOException {
      try {
        JarFile jf = new JarFile(new File(_path));
        jf.getManifest();

        for (JarCPItem i : getDependencyJars())
          System.out.println("\t" + i);

        // for(JarEntry je =
        // jf.entries().nextElement();jf.entries().hasMoreElements();je
        // = jf.entries().nextElement())
        // while(jf.entries().hasMoreElements())
        // {
        // JarEntry je = jf.entries().nextElement();
        // System.out.println("\t"+je.getName());
        // }

        for (Enumeration<?> entries = jf.entries(); entries.hasMoreElements();) {

          JarEntry entry = (JarEntry) entries.nextElement();
          System.out.println(entry);

        }
        jf.close();
      } catch (ZipException e) {
        System.err.println("\tErr: " + _path);
      }

    }

    public List<JarCPItem> getDependencyJars() throws IOException {
      List<JarCPItem> result = new ArrayList<JarCPItem>();

      JarFile jf = new JarFile(new File(_path));
      Manifest mf = jf.getManifest();

      Object o = mf.getMainAttributes().get(new java.util.jar.Attributes.Name("Class-Path"));
      if (o != null)
        for (String p : o.toString().split(" ")) {
          result.add(new JarCPItem(new File(_path).getParent() + "/" + p));
        }
      jf.close();

      return result;
    }
  }

}
