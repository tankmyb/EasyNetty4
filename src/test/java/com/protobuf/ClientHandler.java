package com.protobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<PersonProtobuf.Person> {

@Override
protected void channelRead0(ChannelHandlerContext ctx, PersonProtobuf.Person msg) throws Exception {
System.out.println("client:" + "channelRead:" + msg.getEmail());
}
}