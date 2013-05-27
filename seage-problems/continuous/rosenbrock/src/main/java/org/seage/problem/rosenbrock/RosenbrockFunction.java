package org.seage.problem.rosenbrock;

public class RosenbrockFunction
{
	// f(X) = SUM(i = 0, to N-1) [(1 - Xi)^2 + 100 * (Xi+1 - Xi^2)^2]
	public static double f(double[] x)
	{
		double value = 0.0;
        for(int i = 0; i < x.length - 1; i++)
        {
            value += f0(x[i], x[i+1]);
        }
        return value;
	}
	
	public static double f(Double[] x)
	{
		double value = 0.0;
        for(int i = 0; i < x.length - 1; i++)
        {
            value += f0(x[i], x[i+1]);
        }
        return value;
	}
	
	private static double f0(double x1, double x2)
	{
		return (Math.pow(1 - x1, 2) + 100 * Math.pow( x2 - Math.pow( x1, 2 ) , 2 ) );
	}
}
