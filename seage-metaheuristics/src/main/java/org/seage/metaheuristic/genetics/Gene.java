/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
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
