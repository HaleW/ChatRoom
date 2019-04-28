package cn.edu.cuit.operation;

import java.util.Map;

import cn.edu.cuit.client.ClientMsg;
import cn.edu.cuit.proto.ProtoMsg;

import static cn.edu.cuit.tools.Tools.dateNow;

public class Login {
    public static Map getUserInfos() {
        return userInfos;
    }

    public static void setUserInfos(Map userInfos) {
        Login.userInfos = userInfos;
    }

    private static Map userInfos;

    public static void sendUserInfo(String userName, String password) {
        ClientMsg clientMsg = new ClientMsg();
        ProtoMsg.Msg.Builder msg = ProtoMsg.Msg.newBuilder();
        ProtoMsg.UserInfo.Builder info = ProtoMsg.UserInfo.newBuilder();
        ProtoMsg.UserInfo userInfo = info.setName(userName).setPassword(password).build();
        ProtoMsg.Msg sendMsg = msg.setType(ProtoMsg.MsgType.LOGIN).setSendTime(dateNow()).setUserInfo(userInfo).build();
        clientMsg.setMsg(sendMsg);
    }

    public static ProtoMsg.UserInfo receiveUserInfo;
}
