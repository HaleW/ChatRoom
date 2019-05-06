package cn.edu.cuit.operation;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.proto.ProtoMsg;
import cn.edu.cuit.proto.ProtoMsg.Msg;

public class ChatMsg {
    public static void sendMsg(Msg msg){
        ClientMsg clientMsg = new ClientMsg();
        clientMsg.setMsg(msg);
    }

    public static Msg getReceiveMsg() {
        return receiveMsg;
    }

    public static void setReceiveMsg(Msg receiveMsg) {
        ChatMsg.receiveMsg = receiveMsg;
    }

    private static Msg receiveMsg;
    public static List<ProtoMsg.Msg> list=new ArrayList<>();
}
