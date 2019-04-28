package cn.edu.cuit.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

class Client {
    static ChannelFuture channelFuture;
    void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ClientInitializer());

            channelFuture = bootstrap.connect(host, port);
            channelFuture.addListener(new ClientConnectionListener());
        } catch (Exception e){
            System.out.println("连接失败！");
            workerGroup.shutdownGracefully();
        }
    }
}


