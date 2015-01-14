package com.easynetty4.codec.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import com.easynetty4.codec.kryo.serializer.KryoSerializer;
import com.easynetty4.codec.kryo.serializer.Serializer;

@Sharable
public class KryoDecoder extends MessageToMessageDecoder<ByteBuf> {
	private Serializer serializer = new KryoSerializer();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		
		//final byte[] array;
       // final int length = msg.readableBytes();
        //System.out.println(length+"=====");
            //array = msg.array();
            //System.out.println("============="+msg);
        
        try {
        	//System.out.println(System.currentTimeMillis()+"=4");
			out.add(serializer.deserialize(msg.array()));
			
		} catch (Exception ex) {
			throw ex;
		
	}
	}
    
}