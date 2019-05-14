package cn.edu.cuit.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static cn.edu.cuit.client.Client.channelFuture;

public class ClientMsg {
    public void setMsg(Msg msg) {
        if(channelFuture==null){
            return;
        }

        ChannelFuture cf = channelFuture.channel().writeAndFlush(msg);

        cf.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {   //写操作完成
                System.out.println("发送成功！");
            } else {    //写操作失败
                System.out.println("发送失败！");
            }
        });
    }
}
