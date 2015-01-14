package com.jackson;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.easynetty4.codec.jackson.JacksonDecoder;
import com.easynetty4.codec.jackson.JacksonEncoder;

public class BatchClient {

	public static void main(String[] args) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<Channel>() {
				
				@Override
				protected void initChannel(Channel ch) throws Exception {
					 ch.pipeline().addLast("encoder",new JacksonEncoder());
					ch.pipeline().addLast("decoder",new JacksonDecoder(Person.class));
					   
				ch.pipeline().addLast("handler", new ClientHandler());
				};
			});
			bootstrap.option(ChannelOption.SO_RCVBUF, 10000);
			bootstrap.option(ChannelOption.SO_SNDBUF, 10000);
			Channel ch = bootstrap.connect("127.0.0.1", 8888).sync().channel();
			// 控制台输入
			Thread.sleep(1000L);
			for (int i=0;i<1000000;i++) {
				String line = i+"";
				Person owner = new Person();
				owner.setEmail(line);
				owner.setId(1);
				owner.setName("aaaaaaaa");
				ch.writeAndFlush(owner);
				//Thread.sleep(1);
			}
			Thread.sleep(10000000L);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}