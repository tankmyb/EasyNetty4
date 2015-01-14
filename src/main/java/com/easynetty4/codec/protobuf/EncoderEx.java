package com.easynetty4.codec.protobuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
@Sharable
public class EncoderEx extends MessageToMessageEncoder<MessageLiteOrBuilder> {
    @Override
    protected void encode(
            ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
    	int type = ProtobufMappingManager.getMessageId(msg.getClass());
        if (msg instanceof MessageLite) {
            out.add(wrappedBuffer(Unpooled.buffer(2).writeShort(type).array(),((MessageLite) msg).toByteArray()));
            return;
        }
        if (msg instanceof MessageLite.Builder) {
            out.add(wrappedBuffer(Unpooled.buffer(2).writeShort(type).array(),((MessageLite.Builder) msg).build().toByteArray()));
        }
    }
}
