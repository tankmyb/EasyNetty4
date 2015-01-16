package com.easynetty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {

	public static void main(String[] args) {
		EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
		EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(10);
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup);
			serverBootstrap.channel(NioServerSocketChannel.class);
			serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast("encoder1",new LengthFieldPrepender(4));
					ch.pipeline().addLast("encoder",new StringEncoder());
					ch.pipeline().addLast("decoder",new StringDecoder());
					ch.pipeline().addLast("decoder1",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
					   
					ch.pipeline().addLast("handler", new ServerHandler());
				};
			});
			serverBootstrap.option(ChannelOption.SO_RCVBUF, 10000);
			serverBootstrap.option(ChannelOption.SO_SNDBUF, 10000);
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