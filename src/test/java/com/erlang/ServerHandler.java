package com.erlang;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
	static AtomicInteger a = new AtomicInteger(0);
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("===========connect========="+a.incrementAndGet());
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("===========channelUnregistered"+a.decrementAndGet());
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("-server:" + "channelRead:" + msg + "=="
				+ System.currentTimeMillis());
		ctx.writeAndFlush(msg);

	}
}