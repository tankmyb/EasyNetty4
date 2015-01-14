package com.easynetty4.codec.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class KryoFrameDecoder extends ByteToMessageDecoder {

	// TODO maxFrameLength + safe skip + fail-fast option
	// (just like LengthFieldBasedFrameDecoder)

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(in.readableBytes()<4){
			return;
		}
		in.markReaderIndex();
		int length = in.readInt();
		//System.out.println(length+"============lenth");
		if (in.readableBytes() < length) {
			in.resetReaderIndex();
		} else {
			//System.out.println(System.currentTimeMillis()+"=3");
			out.add(in.readBytes(length));
			
		}
	}
}
