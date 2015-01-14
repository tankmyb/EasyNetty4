package com.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.easynetty4.codec.protobuf.ProtobufMappingManager;

public class Server {

	public static void main(String[] args) {
		ProtobufMappingManager.put(2, PersonProtobuf.Person.getDefaultInstance());
		EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
		EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(10);
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					/*ch.pipeline().addLast("frameEncoder",new ProtobufEncoderEx());
					//ch.pipeline().addLast("encoder", new EncoderEx());
					//ch.pipeline().addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
					//ch.pipeline().addLast("encoder", new ProtobufEncoder());
					ch.pipeline().addLast("frameDecoder",
							new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast("decoder",
							new ProtobufDecoderEx());*/
					ch.pipeline().addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast("encoder", new ProtobufEncoder());
					ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast("decoder", new ProtobufDecoder(PersonProtobuf.Person.getDefaultInstance()));
					ch.pipeline().addLast("handler", new ServerHandler());
				};
			});
			serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossEventLoopGroup.shutdownGracefully();
			workerEventLoopGroup.shutdownGracefully();
		}
	}
}