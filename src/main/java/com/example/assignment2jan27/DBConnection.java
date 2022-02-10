package com.example.assignment2jan27;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final private String DB_URL = "jdbc:mysql://localhost:3306/books";

    static final private String USER = "root";
    static final private String PASS = "gordie28";

    public static Connection initDatabase() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }
}
