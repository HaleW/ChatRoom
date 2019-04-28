package cn.edu.cuit.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelHandlerContext;

import static cn.edu.cuit.proto.ProtoMsg.MsgType.HEARTBEAT;

public class Tools {
    public static final int ReconnectTime = 5;
    public static final int Port=9999;
    public static final String IP = "10.18.19.40";

    public static String dateNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return df.format(new Date());
    }

    public static String getRemoteAddress(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

    public static String getLocalAddress(ChannelHandlerContext ctx) {
        return ctx.channel().localAddress().toString();
    }

    public static Msg HeartBeat(ChannelHandlerContext ctx){
        Msg.Builder heartbeat = Msg.newBuilder();
        return heartbeat.
                setType(HEARTBEAT).
                setSendTime(dateNow()).
                setTargetIP(getLocalAddress(ctx)).
                build();
    }
}
