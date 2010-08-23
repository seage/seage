/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.classutil;

/**
 *
 * @author rick
 */
public class ClassInfo
{
    private String _jarPath;
    private Class _class;
    private String[] _classPaths;


    public ClassInfo(String jarPath, Class classObj, String[] classPaths)
    {
        _jarPath = jarPath;
        _class = classObj;
        _classPaths = classPaths;
    }  

    public Class getClassObj() {
        return _class;
    }

    public String getPackagePath() {
        return _jarPath;
    }

    public String[] getClassPaths() {
        return _classPaths;
    }


}
