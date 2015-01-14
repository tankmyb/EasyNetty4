package com.kryo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import com.easynetty4.codec.kryo.KryoDecoder;
import com.easynetty4.codec.kryo.KryoEncoder;
import com.easynetty4.codec.kryo.KryoFrameDecoder;
import com.easynetty4.codec.kryo.KryoFrameEncoder;
import com.easynetty4.codec.protobuf.EncoderEx;
import com.easynetty4.codec.protobuf.ProtobufDecoderEx;
import com.easynetty4.codec.protobuf.ProtobufEncoderEx;
import com.easynetty4.codec.protobuf.ProtobufMappingManager;

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
					ch.pipeline().addLast("frameEncoder", new KryoFrameEncoder());
					ch.pipeline().addLast("encoder",new KryoEncoder());
					//ch.pipeline().addLast("frameDecoder",  new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
					ch.pipeline().addLast("frameDecoder",  new KryoFrameDecoder());

				ch.pipeline().addLast("decoder", new KryoDecoder());
				
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