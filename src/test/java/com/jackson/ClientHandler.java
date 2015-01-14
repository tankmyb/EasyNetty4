package com.jackson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Person> {

@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
	System.out.println("==========");
		super.exceptionCaught(ctx, cause);
	}

@Override
protected void channelRead0(ChannelHandlerContext ctx, Person msg) throws Exception {
System.out.println("client:" + "channelRead:" + msg.getEmail());
}
}