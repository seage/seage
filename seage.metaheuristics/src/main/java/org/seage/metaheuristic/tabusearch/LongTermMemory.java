/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
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
package org.seage.metaheuristic.tabusearch;

/**
 * LongTermMemory interface.
 * 
 * @author Richard Malek
 */
public interface LongTermMemory
{
	public void clearMemory();
	public void memorizeSolution(Solution soln, boolean newBestSoln);
	public Solution diversifySolution();
	public void resetIterNumber();
}
