package cn.edu.cuit.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static cn.edu.cuit.tools.Tools.*;

class DBHelper {
    Connection getConnection() {
        Connection connection = null;

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(MysqlUser);
        dataSource.setPassword(MysqlPassword);
        dataSource.setServerName(MysqlIP);
        dataSource.setDatabaseName(MysqlDatabaseName);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    void closeConnection() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
