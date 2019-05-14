package cn.edu.cuit.operation;

import cn.edu.cuit.client.ClientMsg;

import static cn.edu.cuit.tools.Tools.dateNow;

public class Login {
    public static void sendUserInfo(String userName, String password) {
        ClientMsg clientMsg = new ClientMsg();

        Msg.Builder msg = Msg.newBuilder();
        UserInfo.Builder info = UserInfo.newBuilder();

        UserInfo userInfo = info.setName(userName).setPassword(password).build();

        Msg sendMsg = msg.setType(MsgType.LOGIN).setSendTime(dateNow()).setUserInfo(userInfo).build();

        clientMsg.setMsg(sendMsg);
    }

    public static Msg getReceiveLoginMsg() {
        return receiveLoginMsg;
    }

    public static void setReceiveLoginMsg(Msg receiveLoginMsg) {
        if (receiveLoginMsg.getType() == MsgType.LOGIN) {
            Login.receiveLoginMsg = receiveLoginMsg;
        } else {
            Login.receiveLoginMsg = null;
        }
    }

    private static Msg receiveLoginMsg;
}
