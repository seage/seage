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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

/**
 *
 * @author Richard Malek
 */
public class ClassPathAnalyzer
{

    String _filter = "";
    private List<String> _classNames;

    public ClassPathAnalyzer()
    {
        _classNames = new ArrayList<String>();
    }

    public ClassPathAnalyzer(String filter)
    {
        this();
        _filter = filter;
    }

    private boolean isFiltered(String s)
    {
        return s.replace('-', '.').replace(System.getProperty("file.separator"), ".").contains(_filter);
    }

    public List<String> analyzeClassPath()
    {
        String cp = System.getProperty("java.class.path");
        String delim = System.getProperty("path.separator");

        String[] paths = cp.split(delim);

        for (String s : paths)
        {
            try
            {
                File f = new File(s);
                if (f.isDirectory())
                    analyzeDir(f);
                if (f.isFile() && f.getName().endsWith(".jar"))
                    analyzeJar(f);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return _classNames;
    }

    private void analyzeDir(File dir)
    {
        // if filter
        if (!isFiltered(dir.getAbsolutePath()))
            return;
        analyzeDir(dir, dir.getAbsolutePath() + System.getProperty("file.separator"));
    }

    private void analyzeDir(File dir, String prefix)
    {
        for (File f : dir.listFiles())
        {
            if (f.isDirectory())
                analyzeDir(f, prefix);
            if (f.isFile())
                addClassPath(f.getAbsolutePath().replace(prefix, ""));
        }
    }

    private void analyzeJar(File f) throws IOException
    {
        JarFile jf = new JarFile(f);
        Manifest mf = jf.getManifest();

        List<JarFile> jars = new ArrayList<JarFile>();
        jars.add(jf);

        Object o = mf.getMainAttributes().get(new java.util.jar.Attributes.Name("Class-Path"));
        if (o != null)
        {
            for (String p : o.toString().split(" "))
            {
                if (!p.endsWith(".jar"))
                    continue;
                try
                {
                    File f2 = new File(f.getAbsoluteFile().getParentFile().getAbsolutePath()
                            + System.getProperty("file.separator") + p);
                    if (f2.isFile() && f2.exists())
                        jars.add(new JarFile(f2));
                }
                catch (ZipException ex)
                {
                    System.err.println("'" + p + "'");
                    ex.printStackTrace();
                }
            }
        }

        for (JarFile jar : jars)
        {
            if (!isFiltered(jar.getName()))
                continue;

            for (Enumeration<?> entries = jar.entries(); entries.hasMoreElements();)
            {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryName = entry.getName();
                if ((entryName.endsWith("/")))
                    continue;
                addClassPath(entryName);

            }
        }
    }

    private void addClassPath(String path)
    {
        String className = path;

        if (!_classNames.contains(className))
            _classNames.add(className);
    }
}
