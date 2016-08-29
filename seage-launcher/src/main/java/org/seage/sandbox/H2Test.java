package org.seage.sandbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Test
{
    public static void main(String[] args)
    {
        try
        {
            new H2Test().test1();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void test1() throws Exception
    {
        String queryCreate = "DROP TABLE IF EXISTS test;" +
                "CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY )";

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/h2db/seage", "sa", "sa");
        PreparedStatement createStmt = null;

        try
        {
            createStmt = conn.prepareStatement(queryCreate);
            createStmt.execute();
        }
        finally
        {
            conn.close();// TODO: B - handle exception
        }

        long t1 = System.currentTimeMillis();

        Thread[] threads = new Thread[4];
        for (int i = 0; i < threads.length; i++)
            threads[i] = new Thread(new Task((i + 1) * 1000000));

        for (int i = 0; i < threads.length; i++)
            threads[i].start();

        for (int i = 0; i < threads.length; i++)
            threads[i].join();

        System.out.println(System.currentTimeMillis() - t1);

    }

    private class Task implements Runnable
    {
        private int _number;

        public Task(int number)
        {
            _number = number;
        }

        @Override
        public void run()
        {
            final String queryInsert = "INSERT INTO test VALUES (?)";
            Connection conn = null;

            try
            {
                conn = DriverManager.getConnection("jdbc:h2:~/h2db/seage", "sa", "sa");
                PreparedStatement insertStmt = null;

                insertStmt = conn.prepareStatement(queryInsert);

                for (int i = 0; i < 100000; i++)
                {
                    insertStmt.setInt(1, _number + i);
                    insertStmt.executeUpdate();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (conn != null)
                    try
                    {
                        conn.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
    }

    public void processLogs0() throws Exception
    {
        String queryCreate = "DROP TABLE IF EXISTS test;" +
                "CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY, name VARCHAR(50))";

        String queryCreate2 =
        //"DROP TABLE IF EXISTS test2;"+
        "CREATE TEMP TABLE test2 (id INTEGER PRIMARY KEY)";

        String queryInsert = "INSERT INTO test VALUES (?, ?)";

        String queryInsert2 = "INSERT INTO test2 VALUES (?)";

        String queryMinus = "SELECT id FROM test2 MINUS SELECT id FROM test";

        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/h2db/seage", "sa", "sa");
        PreparedStatement createStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement selectStmt = null;
        try
        {
            createStmt = conn.prepareStatement(queryCreate);
            createStmt.execute();
            createStmt = conn.prepareStatement(queryCreate2);
            createStmt.execute();

            insertStmt = conn.prepareStatement(queryInsert);

            for (int i = 0; i < 10000; i++)
            {
                insertStmt.setInt(1, i + 1);
                insertStmt.setString(2, new Integer(((i + 1) * 10)).toString());
                insertStmt.execute();
            }
            long t1 = System.currentTimeMillis();
            insertStmt = conn.prepareStatement(queryInsert2);

            for (int i = 0; i < 100000; i++)
            {
                insertStmt.setInt(1, i);
                insertStmt.execute();
            }

            selectStmt = conn.prepareStatement(queryMinus);

            @SuppressWarnings("unused")
			ResultSet rs = selectStmt.executeQuery();
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);

        }
        finally
        {
            if (conn != null)
                conn.close();
        }
    }
}
