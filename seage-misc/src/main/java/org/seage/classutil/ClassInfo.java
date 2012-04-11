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
