package cn.edu.cuit.operation;

import cn.edu.cuit.proto.ProtoMsg;
import cn.edu.cuit.proto.ProtoMsg.Msg;

public class Add {
    private static ProtoMsg.Msg users;
    private static Msg user;

    public static Msg getUsers() {
        return users;
    }

    public static void setUsers(Msg users) {
        Add.users = users;
    }

    public static Msg getUser() {
        return user;
    }

    public static void setUser(Msg user) {
        Add.user = user;
    }
}
