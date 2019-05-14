package cn.edu.cuit.operation;

import cn.edu.cuit.client.ClientMsg;

import static cn.edu.cuit.tools.Tools.dateNow;

public class Logon {
    public static void goToLogon(String name,String phone, String email, String password){
        ClientMsg clientMsg = new ClientMsg();
        Msg.Builder m = Msg.newBuilder();
        UserInfo.Builder info = UserInfo.newBuilder();
        UserInfo userInfo = info
                .setName(name)
                .setPhone(phone)
                .setEmail(email)
                .setPassword(password)
                .build();
        Msg msg = m.setUserInfo(userInfo).setSendTime(dateNow()).setType(MsgType.LOGON).build();

        clientMsg.setMsg(msg);
    }

    public static boolean isRightForPassword(String psd1,String psd2){
        return psd1.equals(psd2);
    }

    public static Msg getReceiveLogonMsg() {
        return receiveLogonMsg;
    }

    public static void setReceiveLogonMsg(Msg receiveLogonMsg) {
        if (receiveLogonMsg.getType() == MsgType.LOGON) {
            Logon.receiveLogonMsg = receiveLogonMsg;
        } else {
            Logon.receiveLogonMsg = null;
        }
    }

    private static Msg receiveLogonMsg;
}
