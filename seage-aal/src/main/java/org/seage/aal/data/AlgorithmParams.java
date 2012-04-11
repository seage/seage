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
 * Algorithm parameters
 * @author Richard Malek
 * 
 */
public class AlgorithmParams extends DataNode{

    /**
	 * A generated value
	 */
	private static final long serialVersionUID = 1L;

	public AlgorithmParams(DataNode dn) {
        super(dn);
    }

    public AlgorithmParams(String name) {
        super(name);
    }

}
