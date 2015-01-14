package com.kryo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Person> {

@Override
protected void channelRead0(ChannelHandlerContext ctx, Person msg) throws Exception {
System.out.println("client:" + "channelRead:" + msg.getEmail());
}
}