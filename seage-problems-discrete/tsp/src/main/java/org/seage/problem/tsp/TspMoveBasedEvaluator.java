package org.seage.problem.tsp;

public class TspMoveBasedEvaluator
{
    protected City[] _cities;

    public TspMoveBasedEvaluator(City[] cities)
    {
        _cities = cities;
    }

    public double[] evaluate(Integer[] tour, int[] move, double originalLength) throws Exception
    {
        try
        {
            //Integer[] tour = solution._tour;
            int len = tour.length;

            // If move is null, calculate distance from scratch
            if (move == null)
            {
                double dist = 0;
                for (int i = 0; i < len; i++)
                {
                    dist += getCityDistance(tour[i] - 1, i + 1 >= len ? tour[0] - 1 : tour[i + 1] - 1);
                }

                return new double[] { dist };
            } // end if: move == null
              // Else calculate incrementally
            else
            {
                //int mv = (TspSwapMove) move;
                int pos1 = move[0];
                int pos2 = move[1];
                if (pos1 > pos2)
                {
                    int a = pos1;
                    pos1 = pos2;
                    pos2 = a;
                }

                // Prior objective value
                double dist = 0;//solution.getObjectiveValue();

                int pos1L = (pos1 - 1 + len) % len;
                int pos1R = (pos1 + 1 + len) % len;
                int pos2L = (pos2 - 1 + len) % len;
                int pos2R = (pos2 + 1 + len) % len;

                int delta = Math.abs(pos1 - pos2);
                // Treat a pair swap move differently
                if (delta == 1)
                {
                    //     | |
                    // A-B-C-D-E: swap C and D, say (works for symmetric matrix only)
                    dist -= getCityDistance(tour[pos1L] - 1, tour[pos1] - 1); // -BC
                    dist -= getCityDistance(tour[pos2] - 1, tour[pos2R] - 1); // -DE
                    dist += getCityDistance(tour[pos1L] - 1, tour[pos2] - 1); // +BD
                    dist += getCityDistance(tour[pos1] - 1, tour[pos2R] - 1); // +CE
                    return new double[] { originalLength + dist };
                } // end if: pair swap
                else if (delta == (len - 1))
                {
                    // |       |
                    // A-B-C-D-E: swap A and E, say (works for symmetric matrix only)
                    dist -= getCityDistance(tour[pos1] - 1, tour[pos1R] - 1); // -AB
                    dist -= getCityDistance(tour[pos2L] - 1, tour[pos2] - 1); // -DE
                    dist += getCityDistance(tour[pos1R] - 1, tour[pos2] - 1); // +EB
                    dist += getCityDistance(tour[pos1] - 1, tour[pos2L] - 1); // +DA
                    return new double[] { originalLength + dist };
                }
                // Else the swap is separated by at least one customer
                else
                {
                    //   |     |
                    // A-B-C-D-E-F: swap B and E, say
                    dist -= getCityDistance(tour[pos1L] - 1, tour[pos1] - 1); // -AB
                    dist -= getCityDistance(tour[pos1] - 1, tour[pos1R] - 1); // -BC
                    dist -= getCityDistance(tour[pos2L] - 1, tour[pos2] - 1); // -DE
                    dist -= getCityDistance(tour[pos2] - 1, tour[pos2R] - 1); // -EF

                    dist += getCityDistance(tour[pos1L] - 1, tour[pos2] - 1); // +AE
                    dist += getCityDistance(tour[pos2] - 1, tour[pos1R] - 1); // +EC
                    dist += getCityDistance(tour[pos2L] - 1, tour[pos1] - 1); // +DB
                    dist += getCityDistance(tour[pos1] - 1, tour[pos2R] - 1); // +BF
                    return new double[] { originalLength + dist };
                } // end else: not a pair swap
            } // end else: calculate incremental
        }
        catch (Exception ex)
        {
            throw ex;
        }
    } // end evaluate

    private double getCityDistance(int i, int j)
    {
        return getEuclidianDistance(_cities[i].X, _cities[i].Y, _cities[j].X, _cities[j].Y);
    }

    /** Calculate distance between two points. */
    private double getEuclidianDistance(double x1, double y1, double x2, double y2)
    {
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.round(Math.sqrt(xDiff * xDiff + yDiff * yDiff));
    }

}
