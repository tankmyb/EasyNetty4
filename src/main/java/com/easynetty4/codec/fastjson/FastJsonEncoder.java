package com.easynetty4.codec.fastjson;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.alibaba.fastjson.JSON;

public class FastJsonEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object respBean,
			ByteBuf out) throws Exception {
		String res = JSON.toJSONString(respBean);
		byte[] data = res.getBytes();
		int dataLength = data.length;
		out.writeInt(dataLength);
		out.writeBytes(data);

	}
}