package cn.edu.cuit.client;

import java.util.concurrent.TimeUnit;

import cn.edu.cuit.proto.ProtoMsg.Msg;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;



class ClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        //读超时时间，写超时时间，所有类型超时时间，时间格式
        pipeline.addLast(new IdleStateHandler(0, 50, 0, TimeUnit.SECONDS));
        //半包处理
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //告诉ProtobufDecoder需要解码的目标是什么
        pipeline.addLast(new ProtobufDecoder(Msg.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        //业务逻辑实现
        pipeline.addLast(new ClientHandler());
    }
}