/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

import org.seage.metaheuristic.fireflies.*;
import java.util.*;

/**
 * SolutionComparator.
 *
 * @author Richard Malek
 */
public class SolutionComparator implements Comparator
{
	private boolean _maximizing;

        public SolutionComparator(){
            this(false);
        }

	public SolutionComparator(boolean maximizing)
	{
		_maximizing = maximizing;
	}

	public int compare(Object o1, Object o2)
	{
		Solution s1 = (Solution)o1;
		Solution s2 = (Solution)o2;
		//boolean b = SingleThreadedTabuSearch.firstIsBetterThanSecond(s1.getObjectiveValue(), s2.getObjectiveValue(), _maximizing);

		//if (b == true)
		//    return 1;
		//else
		//    return -1;

		for (int i = 0; i < s1.getObjectiveValue().length; i++)
		{
			int result = compare(s1.getObjectiveValue()[i], s2.getObjectiveValue()[i]);
			if (result == 0)
				continue;
			else
				return result;

		}
		return 0;
	}

	private int compare(double d1, double d2)
	{
		int max = -1;
		if (_maximizing == false)
			max = 1;

		boolean b1 = d1 > d2;
		boolean b2 = d1 < d2;

		if (b1 == false && b2 == false)
			return 0;
		else
			if (b1 == true)
				return max;
			else
				if (b2 == true)
					return -max;
		return 0;
	}
}
