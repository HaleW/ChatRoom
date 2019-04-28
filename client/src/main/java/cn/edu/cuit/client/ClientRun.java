package cn.edu.cuit.client;

import java.util.Scanner;

import cn.edu.cuit.proto.ProtoMsg;

import static cn.edu.cuit.tools.Tools.*;

public class ClientRun {
    public static void Run() {
        new Client().connect(IP, Port);
    }

    public static void main(String[] args) {
        Run();
        Scanner in = new Scanner(System.in);
        while (in.next().equals("a")){
            ClientMsg msg = new ClientMsg();
            ProtoMsg.Msg.Builder m = ProtoMsg.Msg.newBuilder();
            ProtoMsg.Msg mm= m.setTargetIP("aaaaaaaaaa").build();
            msg.setMsg(mm);
        }
    }
}
