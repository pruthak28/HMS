package com.data.orion.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class ConnectionFactory {
    //static reference to itself
    private static ConnectionFactory instance = new ConnectionFactory();
    public static final String URL = "jdbc:mysql://localhost:3306/health_management_system?zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true";
    public static final String USER = "root";
    public static final String PASSWORD = "root@123";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
   
      //private constructor
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
            System.out.println("Error code"+e.getErrorCode());
            System.out.println("Error Msg"+e.getMessage());
            System.out.println("Error Cause"+e.getCause());
        }
        return connection;
    }   
   
    public static Connection getConnection() {
        return instance.createConnection();
    }
   
}