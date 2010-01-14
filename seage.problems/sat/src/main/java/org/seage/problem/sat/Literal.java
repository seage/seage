package org.seage.problem.sat;

/**
 * Summary description for Literal.
 */
public class Literal
{
	int _value;

	public Literal(int value)
	{
		_value = value;
	}

	public boolean isNeg()
	{
		return _value < 0 ? true : false;
	}

	public int getValue()
	{
		return Math.abs(_value);
	}

	public String toString()
	{
		String result = "";
		result += _value;
		return result;
	}
}
