package com.jackson;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Person> {
	AtomicInteger a = new AtomicInteger();
@Override
public void channelRead0(ChannelHandlerContext ctx, Person msg) throws Exception {
	if(a.getAndIncrement()%1000==0){
		System.out.println(a.intValue()+"-server:" + "channelRead:" + msg.getEmail()+"=="+System.currentTimeMillis());
	}



//long size = carInfoReq.getSerializedSize();
//byte[] buf = carInfoReq.toByteArray();
//ctx.writeAndFlush(carInfo.build());
}
}