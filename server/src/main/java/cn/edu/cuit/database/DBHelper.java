package cn.edu.cuit.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static cn.edu.cuit.tools.Tools.MysqlDriver;
import static cn.edu.cuit.tools.Tools.MysqlPassword;
import static cn.edu.cuit.tools.Tools.MysqlUrl;
import static cn.edu.cuit.tools.Tools.MysqlUser;

class DBHelper {
    Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(MysqlDriver);
            connection = DriverManager.getConnection(MysqlUrl, MysqlUser, MysqlPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    void closeConnection(){
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
