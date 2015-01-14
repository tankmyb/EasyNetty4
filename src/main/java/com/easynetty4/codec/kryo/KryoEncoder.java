package com.easynetty4.codec.kryo;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.easynetty4.codec.kryo.serializer.KryoSerializer;
import com.easynetty4.codec.kryo.serializer.Serializer;

@Sharable
public class KryoEncoder extends MessageToMessageEncoder<Object> {
	private Serializer serializer = new KryoSerializer();
    @Override
    protected void encode(
            ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
    	//System.out.println(System.currentTimeMillis()+"=1");
    	    //byte[] b = serializer.serialize(msg);
    	    //System.out.println(b.length);
            out.add(serializer.serialize(msg));
           
    }
}