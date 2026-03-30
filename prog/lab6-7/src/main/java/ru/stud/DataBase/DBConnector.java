package ru.stud.DataBase;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private String URL;
    private String USERNAME;
    private String PASSWORD;

    public DBConnector(String url,String username,String password){
        this.URL=url;
        this.USERNAME=username;
        this.PASSWORD=password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USERNAME,PASSWORD);
    }
}
