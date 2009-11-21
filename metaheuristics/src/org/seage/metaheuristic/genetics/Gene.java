package org.seage.metaheuristic.genetics;

import java.io.Serializable;

/**
 * Summary description for Gene.
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
