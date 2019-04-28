package cn.edu.cuit.config;

import java.io.IOException;

public class GetConfig {
    public static int getPort() {
        int port = 0;
        try {
            port = Integer.parseInt(ConfigHelper.getConfig("netty.server.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }

    public static String getIP() {
        String ip = "";
        try {
            ip = ConfigHelper.getConfig("netty.server.ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }

    public static String getMysqlUrl(){
        String url="";
        try {
            url = ConfigHelper.getConfig("mysql.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getMysqlUser(){
        String user = "";
        try {
            user=ConfigHelper.getConfig("mysql.user");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String getMysqlPassword(){
        String password = "";
        try {
            password=ConfigHelper.getConfig("mysql.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static String getMysqlDriver(){
        String driver = "";
        try {
            driver=ConfigHelper.getConfig("mysql.driver");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return driver;
    }
}
