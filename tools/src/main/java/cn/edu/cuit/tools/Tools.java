package cn.edu.cuit.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import cn.edu.cuit.proto.ProtoMsg.MsgType;

public class Tools {
    public static final int ReconnectTime = 5;
    public static final int Port = 9999;
    public static final String IP = "47.106.235.197";

    public static String dateNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        return df.format(new Date());
    }

    public static Msg HeartBeat() {
        Msg.Builder heartbeat = Msg.newBuilder();

        return heartbeat.
                setSendTime(dateNow()).
                setType(MsgType.HEARTBEAT).
                build();
    }
}
