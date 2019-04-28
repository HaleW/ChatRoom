package cn.edu.cuit.client;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static cn.edu.cuit.client.Client.channelFuture;
import static cn.edu.cuit.client.ClientHandler.ReceiveMsg;

public class ClientMsg {
    private boolean SendFlag = Boolean.parseBoolean(null);

    public Msg getMsg() {
        return ReceiveMsg;
    }

    public boolean setMsg(Msg msg) {
        if(channelFuture==null){
            return false;
        }

        ChannelFuture cf = channelFuture.channel().writeAndFlush(msg);

        cf.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {   //写操作完成
                System.out.println("发送成功！");
                SendFlag = true;
            } else {    //写操作失败
                System.out.println("发送失败！");
                SendFlag = false;
            }
        });
        return SendFlag;
    }
}
