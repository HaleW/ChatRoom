package cn.edu.cuit.client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import static cn.edu.cuit.tools.Tools.IP;
import static cn.edu.cuit.tools.Tools.Port;

public class ClientConnectionListener implements ChannelFutureListener {
    private Client client = new Client();

    @Override
    public void operationComplete(ChannelFuture channelFuture){
        if (!channelFuture.isSuccess()){
            final EventLoop eventLoop = channelFuture.channel().eventLoop();
            eventLoop.schedule(() -> {
                System.out.println(" 与服务器连接断开！5秒后见尝试重新连接！");
                client.connect(IP, Port);
            }, 5, TimeUnit.SECONDS);
        }else {
            System.out.println(" 与服务器连接成功！");
        }
    }
}
