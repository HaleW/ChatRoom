package cn.edu.cuit.operation;

public class Add {
    private static Msg users;
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
