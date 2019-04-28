package cn.edu.cuit.server;

import cn.edu.cuit.config.GetConfig;

public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Netty Server");
        final int port = GetConfig.getPort();
        new Server(port).start();
    }
}


