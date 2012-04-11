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

import java.io.Serializable;

/**
 * @author Richard Malek (original)
 */
public class Gene implements Serializable
{
	protected int _value;

	private Gene()
	{ }

	public Gene(int value)
	{
		//if(value[0] == 0) throw new Exception("Invalid gene value");
		_value = value;        
	}

	public String toString()
	{
		Integer i = new Integer(_value);
		return i.toString();//value.toString();
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue( int value)
	{
		_value = value;
	}

	public int hashCode()
	{
		return _value;
	}

	public Object clone()
	{
		Gene gene = new Gene();
		gene._value = _value;
		return gene;
	}
}
