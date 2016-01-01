package org.seage.problem.tsp.tour;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class TspOptimalTour
{
    private static Logger _logger = LoggerFactory.getLogger(TspOptimalTour.class.getName());

    public String Name;
    public Integer[] OptimalTour;
    public int OptimalLength;

    public static Integer[] mirrorTour(Integer[] array)
    {
        Integer[] result = new Integer[array.length];

        for (int i = 0; i < result.length; i++)
            result[i] = array[array.length - i - 1];

        return result;
    }

    public static Integer[] shiftTour(Integer[] array, int offset)
    {
        Integer[] result = new Integer[array.length];

        for (int i = 0; i < result.length; i++)
            result[i] = array[(i + offset) % array.length];

        return result;
    }

    public static Integer[] applySwapMove(Integer[] array, int[] move)
    {
        Integer[] result = array.clone();

        int a = result[move[0]];
        result[move[0]] = result[move[1]];
        result[move[1]] = a;

        return result;
    }

    public static void printTour(Integer[] array)
    {
        for (int i = 0; i < array.length; i++)
            _logger.debug(array[i] + " ");

        _logger.debug(" ");

    }
}
