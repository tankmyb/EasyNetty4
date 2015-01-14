package com.easynetty4.codec.jackson;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.easynetty4.utils.JacksonUtil;

public class JacksonEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object respBean,
			ByteBuf out) throws Exception {
		String res = JacksonUtil.getJsonStr(respBean);
		byte[] data = res.getBytes();
		int dataLength = data.length;
		out.writeInt(dataLength);
		out.writeBytes(data);

	}
}