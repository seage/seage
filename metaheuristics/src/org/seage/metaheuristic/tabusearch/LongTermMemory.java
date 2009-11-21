package org.seage.metaheuristic.tabusearch;

/**
 * LongTermMemory interface.
 * 
 * <p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
 * The Common Public License, developed by IBM and modeled after their industry-friendly IBM Public License,
 * differs from other common open source licenses in several important ways:
 * <ul>
 *  <li>You may include this software with other software that uses a different (even non-open source) license.</li>
 *  <li>You may use this software to make for-profit software.</li>
 *  <li>Your patent rights, should you generate patents, are protected.</li>
 * </ul>
 * </p>
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
