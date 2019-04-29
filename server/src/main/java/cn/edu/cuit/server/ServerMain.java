package cn.edu.cuit.server;

public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Netty Server");
        new Server().start();
    }
}


