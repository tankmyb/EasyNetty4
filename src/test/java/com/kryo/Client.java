package com.kryo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.easynetty4.codec.kryo.KryoDecoder;
import com.easynetty4.codec.kryo.KryoEncoder;
import com.easynetty4.codec.kryo.KryoFrameDecoder;
import com.easynetty4.codec.kryo.KryoFrameEncoder;

public class Client {

	public static void main(String[] args) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast("frameEncoder", new KryoFrameEncoder());
					ch.pipeline().addLast("encoder",new KryoEncoder());
					ch.pipeline().addLast("frameDecoder",  new KryoFrameDecoder());

				ch.pipeline().addLast("decoder", new KryoDecoder());
				
				ch.pipeline().addLast("handler", new ClientHandler());
				};
			});

			Channel ch = bootstrap.connect("127.0.0.1", 8888).sync().channel();
			// 控制台输入
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			for (;;) {
				String line = in.readLine();
				if (line == null || "".equals(line)) {
					continue;
				}
				Person owner = new Person();
				owner.setEmail(line);
				owner.setId(1);
				owner.setName("aaaaaaaa");
				ch.writeAndFlush(owner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}