package cn.edu.cuit.tools;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static cn.edu.cuit.proto.ProtoMsg.MsgType;

public class Tools {
    public static final int ReconnectTime = 5;
    public static final int Port = 9999;
    public static final String IP = "127.0.0.1";
    public static final String MysqlIP = "127.0.0.1";
    public static final String MysqlDatabaseName = "ChatRoom";
    public static final String MysqlUser = "root";
    public static final String MysqlPassword = "psd";

    public static String dateNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        return df.format(new Date());
    }

    public static String getRemoteAddress(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

    public static String getLocalAddress(ChannelHandlerContext ctx) {
        return ctx.channel().localAddress().toString();
    }

    public static Msg HeartBeat(ChannelHandlerContext ctx) {
        Msg.Builder heartbeat = Msg.newBuilder();

        return heartbeat.
                setSendTime(dateNow()).
                setType(MsgType.HEARTBEAT).
                build();
    }
}
