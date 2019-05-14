package cn.edu.cuit.server;

import cn.edu.cuit.database.DBOperation;
import cn.edu.cuit.proto.ProtoMsg.Msg;
import cn.edu.cuit.proto.ProtoMsg.MsgType;
import cn.edu.cuit.proto.ProtoMsg.UserInfo;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.HashMap;
import java.util.Map;

import static cn.edu.cuit.tools.Tools.*;

public class ServerHandler extends SimpleChannelInboundHandler<Msg> {
    private static Map<String, Channel> online = new HashMap<>();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws InterruptedException {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals(event.state())) {
                ctx.writeAndFlush(HeartBeat(ctx));
            } else {
                ctx.channel().closeFuture().sync();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(dateNow() + " 与 " + getRemoteAddress(ctx) + " 建立连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(dateNow() + " 与 " + getRemoteAddress(ctx) + " 断开连接");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(ctx.channel().remoteAddress() + "出现异常：" + cause.getCause());
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) {
        switch (msg.getType()) {
            case LOGON:
                logonMsg(ctx, msg);
                break;
            case LOGIN:
                loginMsg(ctx, msg);
                online.put(msg.getUserInfo().getName(), ctx.channel());
                break;
            case HEARTBEAT:
                break;
            case MSG:
                sendMsg(msg);
                break;
            case FRIENDS:
                friends(ctx, msg);
                break;
            case USERS:
                users(ctx, msg);
                break;
            case ADD:
                add(ctx, msg);
                break;
        }
        System.out.println("Receive:\n" + msg);
    }

    private void add(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();
        int res = operation.insertFriend(msg.getUserInfo().getName(), msg.getTargetName());
        Msg.Builder m = Msg.newBuilder();
        Msg mm;
        if (res != 0) {
            mm = m.setType(MsgType.ADD).setTargetName(msg.getTargetName()).setSendTime(dateNow()).build();
        } else {
            mm = m.setType(MsgType.ADD).setSendTime(dateNow()).build();
        }
        ctx.writeAndFlush(mm);
        System.out.println("Send:\n" + mm);
    }

    private void users(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();

        Map<Integer, String> map = operation.selectAll();

        Msg.Builder m = Msg.newBuilder();
        Msg mm = m.setType(MsgType.USERS).putAllFriends(map).setSendTime(dateNow()).build();

        ctx.writeAndFlush(mm);
        System.out.println("Send:\n" + mm);
    }

    private void sendMsg(Msg msg) {
        for (Map.Entry<String, Channel> entry : online.entrySet()) {
            if (entry.getKey().equals(msg.getTargetName())) {
                Channel channel = entry.getValue();
                channel.writeAndFlush(msg);
                ChannelFuture cf = channel.newSucceededFuture();
                cf.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {   //写操作完成
                        System.out.println("发送成功！");
                    } else {    //写操作失败
                        System.out.println("发送失败！");
                    }
                });
                System.out.println("Send:\n" + msg);
                break;
            }
        }
    }

    private void logonMsg(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();
        UserInfo userInfo = operation.selectByUserName(msg.getUserInfo().getName());

        if (userInfo != null) {
            UserInfo.Builder info = UserInfo.newBuilder();
            userInfo = info.setId(-1).build();
        } else {
            operation.insert(msg.getUserInfo());
            userInfo = operation.selectByUserName(msg.getUserInfo().getName());
        }

        Msg.Builder m = Msg.newBuilder();

        Msg logonMsg = m.setUserInfo(userInfo).setSendTime(dateNow()).setType(MsgType.LOGON).build();

        ctx.writeAndFlush(logonMsg);

        System.out.println("Send:\n" + logonMsg);
    }

    private void loginMsg(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();

        UserInfo userInfo = operation.selectByUserName(msg.getUserInfo().getName());
        if (userInfo == null) {
            UserInfo.Builder info = UserInfo.newBuilder();
            userInfo = info.setId(-1).build();
        }
        Msg.Builder m = Msg.newBuilder();

        Msg loginMsg = m.setUserInfo(userInfo).setSendTime(dateNow()).setType(MsgType.LOGIN).build();

        ctx.writeAndFlush(loginMsg);

        System.out.println("Send:\n" + loginMsg);
    }

    private void friends(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();

        Map<Integer, String> map = operation.selectFriends(msg.getUserInfo().getName());

        Msg.Builder m = Msg.newBuilder();
        Msg mm = m.setType(MsgType.FRIENDS).putAllFriends(map).setSendTime(dateNow()).build();

        ctx.writeAndFlush(mm);
        System.out.println("Send:\n" + mm);
    }
}