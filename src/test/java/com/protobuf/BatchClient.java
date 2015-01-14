package com.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.easynetty4.codec.protobuf.ProtobufMappingManager;

public class BatchClient {

	public static void main(String[] args) {
		ProtobufMappingManager.put(2, PersonProtobuf.Person.getDefaultInstance());
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<Channel>() {
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
				ch.pipeline().addLast("handler", new ClientHandler());
				};
			});

			Channel ch = bootstrap.connect("127.0.0.1", 8888).sync().channel();
			// 控制台输入
			
			for (int i=0;i<1000000;i++) {
				String line = i+"";
				PersonProtobuf.Person.Builder owner = PersonProtobuf.Person
						.newBuilder();
				owner.setEmail(line);
				owner.setId(1);
				owner.setName("aaaaaaaa");
				ch.writeAndFlush(owner.build());
			}
			Thread.sleep(10000000L);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
}