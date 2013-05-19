package org.seage.problem.rosenbrock;

public class RosenbrockFunction
{
	// f(X) = SUM(i = 0, to N-1) [(1 - Xi)^2 + 100 * (Xi+1 - Xi^2)^2]
	public static double f(double[] x)
	{
		double value = 0.0;
        for(int i = 0; i < x.length - 1; i++)
        {
            value += (Math.pow(1 - x[i], 2) + 100 * Math.pow( x[i + 1] - Math.pow( x[i], 2 ) , 2 ) );
        }
        return value;
	}
}
