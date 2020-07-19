package ru.itis.jdbc;

import java.sql.*;

/**
 * @author Leontev Roman
 * 11-905
 * 10.07.20
 */

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "111";

    public static void main(String[] args) throws SQLException {

        SimpleDataSource dataSource = new SimpleDataSource();
        Connection connection = dataSource.openConnection(URL, USER, PASSWORD);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from bar");

        while (resultSet.next()) {

            System.out.print("| ID - " + resultSet.getInt("id") + " | ");
            System.out.print("Name of Cocktail - " + resultSet.getString("cocktail") + " | ");
            System.out.print("Price - " + resultSet.getInt("price") + " | ");

            if (resultSet.getString("alcohol").equals("yes"))
                System.out.println("Cocktail is alcohol     |");

            else
                System.out.println("Cocktail is not alcohol |");
        }

        resultSet.close();

        System.out.println("-------------------");

        resultSet = statement.executeQuery("select * from bar b full outer join popularity p on b.id = p.cocktail_id;");

        while (resultSet.next()) {
            System.out.println("ID " + resultSet.getInt("cocktail_id"));
        }

        connection.close();

    }
}
