package cn.edu.cuit.operation;

import cn.edu.cuit.proto.ProtoMsg.Msg;

public class Main {
    public static Msg getUsers() {
        return users;
    }

    public static void setUsers(Msg users) {
        Main.users = users;
    }

    private static Msg users;
}
