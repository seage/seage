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
package org.seage.metaheuristic.genetics;

/**
 * @author Richard Malek (original)
 */
public class SubjectComparator implements java.util.Comparator
{

	public int compare(Object o1, Object o2)
	{
		Subject s1 = (Subject)o1;
		Subject s2 = (Subject)o2;

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
		//if (_maximizing)
                if(d1<d2)
                    return -1;
                if(d1>d2)
                    return 1;
                return 0;
                //else
                //    return (int)(d2-d1);
        }

		
}
