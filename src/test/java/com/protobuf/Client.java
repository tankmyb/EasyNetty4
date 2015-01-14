package com.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.easynetty4.codec.protobuf.DecoderEx;
import com.easynetty4.codec.protobuf.ProtobufEncoderEx;
import com.easynetty4.codec.protobuf.ProtobufMappingManager;

public class Client {

	public static void main(String[] args) {
		ProtobufMappingManager.put(2000, PersonProtobuf.Person.getDefaultInstance());
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					//ch.pipeline().addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast("encoder", new ProtobufEncoderEx());
					ch.pipeline().addLast("frameDecoder",
							new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast("decoder",
							new DecoderEx(PersonProtobuf.Person.getDefaultInstance()));
					
				/*ch.pipeline().addLast("frameEncoder",new ProtobufVarint32LengthFieldPrepender());
				ch.pipeline().addLast("encoder", new ProtobufEncoder());
				ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
				ch.pipeline().addLast("decoder", new ProtobufDecoder(PersonProtobuf.Person.getDefaultInstance()));*/
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
				PersonProtobuf.Person.Builder owner = PersonProtobuf.Person
						.newBuilder();
				owner.setEmail(line);
				owner.setId(1);
				owner.setName("aaaaaaaa");
				ch.writeAndFlush(owner.build());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}