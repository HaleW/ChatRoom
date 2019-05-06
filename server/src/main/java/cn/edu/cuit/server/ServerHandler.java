package cn.edu.cuit.server;

import java.util.HashMap;
import java.util.Map;

import cn.edu.cuit.database.DBOperation;
import cn.edu.cuit.proto.ProtoMsg.Msg;
import cn.edu.cuit.proto.ProtoMsg.MsgType;
import cn.edu.cuit.proto.ProtoMsg.UserInfo;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import static cn.edu.cuit.tools.Tools.HeartBeat;
import static cn.edu.cuit.tools.Tools.dateNow;
import static cn.edu.cuit.tools.Tools.getRemoteAddress;

public class ServerHandler extends SimpleChannelInboundHandler<Msg> {
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
//        for (Map.Entry<String, Channel> entry : online.entrySet()) {
//            if (entry.getValue().equals(ctx.channel())) {
//                online.remove(entry.getKey());
//                break;
//            }
//        }
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
    protected void messageReceived(ChannelHandlerContext ctx, Msg msg) {
        switch (msg.getType()) {
            case LOGON:
                logonMsg(ctx, msg);
                break;
            case LOGIN:
                loginMsg(ctx, msg);
                online.put(msg.getUserInfo().getName(),ctx.channel());
                break;
            case HEARTBEAT:
                break;
            case MSG:
                sendMsg(msg);
                break;
            case USERS:
                Users(ctx,msg);
                break;
        }
        System.out.println("Receive:\n" + msg);
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

    private static Map<String, Channel> online = new HashMap<>();

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

    private void Users(ChannelHandlerContext ctx, Msg msg) {
        DBOperation operation = new DBOperation();

        Map<Integer, String> map = operation.selectFriends(msg.getUserInfo().getName());

        Msg.Builder m = Msg.newBuilder();
        Msg mm = m.setType(MsgType.USERS).putAllFriends(map).setSendTime(dateNow()).build();

        ctx.writeAndFlush(mm);
        System.out.println("Send:\n" + mm);
    }
}