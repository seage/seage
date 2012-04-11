/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.seage.org/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.data;

import org.seage.data.DataNode;

/**
 * A structure describing a problem (name, id, available algorithms and instances)
 * @author rick
 * 
 * ProblemInfo
 *  |_ id
 *  |_ name
 *  |_ class
 *  |_ Algorithms
 *  |   |_ Algorithm
 *  |   |   |_ id
 *  |   |   |_ name
 *  |   |   |_ factoryClass
 *  |   |   |_ Parameter
 *  |   |   |   |_ name
 *  |   |   |   |_ max
 *  |   |   |   |_ min
 *  |   |   |   |_ init
 *  |   |   |_ ...
 *  |   |   |_ Parameter
 *  |   |_ Algorithm
 *  |       |_ ...
 *  |_ Instances
 *      |_ Instance
 *      |   |_ type ("file" | "resource")
 *      |   |_ path
 *      |_ ...
 *      |_ Instance
 */
public class ProblemInfo extends DataNode{
    
    
    public ProblemInfo(String name) {
        super(name);
    }

}