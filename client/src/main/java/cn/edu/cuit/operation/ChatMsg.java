package cn.edu.cuit.operation;

import java.util.ArrayList;
import java.util.List;

public class ChatMsg {
    public static Msg getReceiveMsg() {
        return receiveMsg;
    }

    public static void setReceiveMsg(Msg receiveMsg) {
        ChatMsg.receiveMsg = receiveMsg;
    }

    private static Msg receiveMsg;
    public static List<Msg> list = new ArrayList<>();
}
