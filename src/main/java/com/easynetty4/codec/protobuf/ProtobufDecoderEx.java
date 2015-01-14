package com.easynetty4.codec.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

import java.util.List;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.MessageLite;

class ProtobufCommonDecoder extends ProtobufDecoder {

	public ProtobufCommonDecoder(MessageLite prototype) {
		super(prototype);
	}
    public void invokeDecode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		super.decode(ctx, msg, out);
		
	}
}
/**
 * protobuf解码器，根据类型查找协议类
 * @author tankma
 *
 */
@Sharable
public class ProtobufDecoderEx extends MessageToMessageDecoder<ByteBuf>{
	 @Override
	    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
	        
	        buf.markReaderIndex();  
	        //System.out.println("==========1===messageId"+buf.readableBytes());
	        final byte[] typeBuf = new byte[5];
	        int messageId=-1;
	        for (int i = 0; i < typeBuf.length; i ++) {
	        	typeBuf[i] = buf.readByte();
	            if (typeBuf[i] >= 0) {
	                messageId = CodedInputStream.newInstance(typeBuf, 0, i + 1).readRawVarint32();
	                if(messageId>0){
	                	//System.out.println(messageId+"===l");
		                break;
	                }
	            }
	        }
	        //int messageId=buf.readShort();
	       // System.out.println(messageId+"=============messageId"+buf.readableBytes());
	        MessageLite bodyLite = ProtobufMappingManager.getMessage(messageId);  
	        if(bodyLite == null) {  
	            buf.resetReaderIndex();  
	            return;  
	        }  
	          
	        ProtobufCommonDecoder decoder = new ProtobufCommonDecoder(bodyLite); 
	        //System.out.println(decoder+"==d");
	        decoder.invokeDecode(ctx, buf,out);  
	       // System.out.println(dataLite);
	        //GameMessage message = new GameMessage();  
	        //message.setId(messageId);  
	        //message.setMessage(dataLite);  
	 }
}
