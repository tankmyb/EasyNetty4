package com.easynetty4.codec.protobuf;

import static io.netty.buffer.Unpooled.wrappedBuffer;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;

@Sharable
public class ProtobufEncoderEx extends
		MessageToByteEncoder<MessageLiteOrBuilder> {
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg,
			ByteBuf out) throws Exception {
		// 获取协议类型
		int type = ProtobufMappingManager.getMessageId(msg.getClass());
		
		ByteBuf buf = null;
		if (msg instanceof MessageLite) {
			buf = wrappedBuffer(((MessageLite) msg).toByteArray());
		}
		if (msg instanceof MessageLite.Builder) {
			buf = wrappedBuffer(((MessageLite.Builder) msg).build()
					.toByteArray());
		}

		int typeLen = CodedOutputStream.computeRawVarint32Size(type);
		int bodyLen = buf.readableBytes();
		// System.out.println(bodyLen+"====len");
		int headerLen = CodedOutputStream.computeRawVarint32Size(bodyLen);

		out.ensureWritable(headerLen + typeLen + bodyLen);

		CodedOutputStream headerOut = CodedOutputStream.newInstance(
				new ByteBufOutputStream(out), headerLen);
		headerOut.writeRawVarint32(bodyLen + typeLen);
		headerOut.writeRawVarint32(type);
		headerOut.flush();

		out.writeBytes(buf, buf.readerIndex(), bodyLen);
	}
}
