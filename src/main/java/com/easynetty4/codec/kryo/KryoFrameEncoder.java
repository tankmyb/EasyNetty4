package com.easynetty4.codec.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class KryoFrameEncoder extends  MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(
            ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
    	//System.out.println(System.currentTimeMillis()+"=2");
        out.writeInt(msg.length);
        out.writeBytes(msg);
        
    }
}