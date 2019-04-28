package cn.edu.cuit.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

class Server {

    private final int port;

    Server(int port) {
        this.port = port;
    }

    void start() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); //bossGroup用于接收
        EventLoopGroup workerGroup = new NioEventLoopGroup();   //workerGroup用于处理接收

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();  //一个启动NIO服务的辅助启动类。配置服务器的各种信息。
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer())  //指定ChannelInitializer，对于每个已接受的连接都调用它
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            System.out.println("Server 启动...");

            ChannelFuture f = serverBootstrap.bind(port).sync();  //绑定端口，开始接收进来的连接
            System.out.println("Server 启动成功,端口是:" + port);
            f.channel().closeFuture().sync();   //关闭channel和块，直到它被关闭
        } finally {
            //关闭EventLoopGroup，释放所有资源
            workerGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();

            System.out.println("Server 关闭...");
        }
    }
}
