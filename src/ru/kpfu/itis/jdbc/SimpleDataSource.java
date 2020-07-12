package ru.kpfu.itis.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Leontev Roman
 * 11-905
 * 10.07.20
 */

public class SimpleDataSource {

    public Connection openConnection(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
