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

package org.seage.aal;

import org.seage.data.DataNode;

/**
 *
 * @author Richard Malek
 */
public interface IAlgorithmFactory
{
    IAlgorithmAdapter createAlgorithm() throws Exception;
}
