/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.temp;

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
 * @author rick
 */
public class ClassPathAnalyzer {
    
    private List<String> _classNames;
    
    public ClassPathAnalyzer()
    {
        _classNames = new ArrayList<String>();
    }
    
    public List<String> analyzeClassPath()
    {
        String cp = System.getProperty("java.class.path");
        String delim = System.getProperty("path.separator");

        String[] paths = cp.split(delim);
        
        for (String s : paths) {
            try
            {
                File f = new File(s);
                if(f.isDirectory())
                    analyzeDir(f);
                if(f.isFile() && f.getName().endsWith(".jar"))
                    analyzeJar(f);
            }
            catch(Exception ex)
            {
                ex. printStackTrace();
            }
        }
        
        return _classNames;
    }
    
    private void analyzeDir(File dir)
    {
        analyzeDir(dir, dir.getAbsolutePath()+"/");
    }
    private void analyzeDir(File dir, String prefix)
    {
        for(File f : dir.listFiles())
            if(f.isDirectory())
                analyzeDir(f, prefix);
            else
                if(f.isFile() && f.getName().endsWith(".class"))
                    addClassPath(f.getAbsolutePath().replace(prefix, ""));
    }
    
    private void analyzeJar(File f) throws IOException
    {
        JarFile jf = new JarFile(f);
        Manifest mf = jf.getManifest();
        
        List<JarFile> jars = new ArrayList<JarFile>();
        jars.add(jf);
        
        Object o = mf.getMainAttributes().get(new java.util.jar.Attributes.Name("Class-Path"));
        if(o != null)            
            for(String p : o.toString().split(" "))
            {
                File f2 = new File(f.getParent() + "/" +p); 
                if(f2.exists())
                    jars.add(new JarFile(f2));
            }
        
        for(JarFile jar : jars)
        {
            //System.out.println(jar.getName());
            
            for (Enumeration entries = jar.entries(); entries.hasMoreElements();) {                
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryName = entry.getName();
                if(entryName.endsWith(".class"))
                    addClassPath(entryName);
                                   
            }
        }   
    }
    
    private void addClassPath(String path)
    {
        String className = path.replace(".class", "").replace('/', '.');
                
        if(!_classNames.contains(className))        
            _classNames.add(className);
//        else
//            System.out.println("\t"+className);
    }
}
