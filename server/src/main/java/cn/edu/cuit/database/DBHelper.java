package cn.edu.cuit.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cn.edu.cuit.config.GetConfig;

public class DBHelper {
    public Connection getConnection() {
        final String dbUrl = GetConfig.getMysqlUrl();
        final String dbDriver = GetConfig.getMysqlDriver();
        final String dbUser = GetConfig.getMysqlUser();
        final String dbPassword = GetConfig.getMysqlPassword();
        Connection connection = null;
        try {
            Class.forName(dbDriver);
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void closeConnection(){
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
