/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.classutil;

import java.net.URL;

/**
 *
 * @author rick
 */
public class ClassInfo
{
    private String _className;
    private URL[] _classPaths;


    public ClassInfo(String className, URL[] classPaths)
    {
        _className = className;
        _classPaths = classPaths;
    }  

    public String getClassName() {
        return _className;
    }

    public URL[] getClassPaths() {
        return _classPaths;
    }


}
