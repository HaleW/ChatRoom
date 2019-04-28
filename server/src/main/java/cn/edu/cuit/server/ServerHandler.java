package cn.edu.cuit.server;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import static cn.edu.cuit.tools.Tools.dateNow;
import static cn.edu.cuit.tools.Tools.getRemoteAddress;

public class ServerHandler extends SimpleChannelInboundHandler<Msg> {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws InterruptedException {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals(event.state())) {
                //ctx.writeAndFlush(HeartBeat(ctx));
            } else {
                ctx.channel().closeFuture().sync();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //Log.d(ServerLog, dateNow() +" 与 " + getRemoteAddress(ctx) + " 建立连接");
        System.out.println(dateNow() +" 与 " + getRemoteAddress(ctx) + " 建立连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //Log.d(ServerLog, dateNow() +" 与 " + getRemoteAddress(ctx) + " 断开连接");
        System.out.println(dateNow() +" 与 " + getRemoteAddress(ctx) + " 断开连接");
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object obj) {
//        Msg msg = (Msg) obj;
//        System.out.println("read"+msg);
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //Log.d(ServerLog,ctx.channel().remoteAddress() + "出现异常：" + cause.getCause());

        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Msg msg) {
        switch (msg.getType()){
            case LOGON:
                break;
            case LOGIN:
                break;
            case HEARTBEAT:
                break;
            case MSG:
                break;
        }
        System.out.println(msg);
    }
}