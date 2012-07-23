/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.seage.org/license/cpl-v10.html
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.data;

/**
 *
 * @author Richard Malek
 */
public class ProblemInstanceInfo {
    
    protected String _instanceName;
    
    public ProblemInstanceInfo(String instanceName)
    {
        _instanceName = instanceName;
    }

    @Override
    public String toString() {
        return _instanceName;
    }
    
    
}
