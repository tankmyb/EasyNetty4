package com.easynetty4.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.easynetty4.codec.protobuf.ProtobufDecoderEx;
import com.easynetty4.codec.protobuf.ProtobufEncoderEx;
public class EasyNettyServer implements IEasyNettyServer{

	
	@Override
	public void bind(int... ports) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .handler(new LoggingHandler(LogLevel.INFO))
         .childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				 ChannelPipeline p = ch.pipeline();
				 ch.pipeline().addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast("encoder", new ProtobufEncoder());
					ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				ch.pipeline().addLast("decoder", new ProtobufDecoderEx());
			}
        	 
         });
	}

	
}
