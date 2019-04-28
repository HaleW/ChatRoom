package cn.edu.cuit.client;

import java.util.concurrent.TimeUnit;

import cn.edu.cuit.operation.Login;
import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import static cn.edu.cuit.tools.Tools.HeartBeat;
import static cn.edu.cuit.tools.Tools.IP;
import static cn.edu.cuit.tools.Tools.Port;
import static cn.edu.cuit.tools.Tools.ReconnectTime;


public class ClientHandler extends SimpleChannelInboundHandler<Msg> {
    static Msg ReceiveMsg = null;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws InterruptedException {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                ctx.writeAndFlush(HeartBeat(ctx));
            } else {
                ctx.channel().closeFuture().sync();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("建立连接时间：" );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开连接时间：");

        final EventLoop eventLoop = ctx.channel().eventLoop();

        Client client = new Client();

        eventLoop.schedule(() -> client.connect(IP, Port), ReconnectTime, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Msg msg) {
            switch (msg.getType()){
                case MSG:
                    break;
                case HEARTBEAT:
                    break;
                case LOGIN:
                    Login.receiveUserInfo=msg.getUserInfo();
                    break;
                case LOGON:
                    break;
            }
        System.out.println(msg);
    }
}