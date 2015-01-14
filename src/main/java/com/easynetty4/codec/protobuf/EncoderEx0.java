package com.easynetty4.codec.protobuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
@Sharable
public class EncoderEx0 extends MessageToMessageEncoder<MessageLiteOrBuilder> {
    @Override
    protected void encode(
            ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
    	// 获取协议类型
    			//int type = ProtobufMappingManager.getMessageId(msg.getClass());
        if (msg instanceof MessageLite) {
            out.add(wrappedBuffer(((MessageLite) msg).toByteArray()));
            return;
        }
        if (msg instanceof MessageLite.Builder) {
            out.add(wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray()));
        }
    }
}
