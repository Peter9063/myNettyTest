package org.myNettyTest.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
	int counter=0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	ByteBuf cache=null;
    	String str="QUERY TIME ORDER"+System.getProperty("line.separator");
    	byte[] resp=str.getBytes();
    	for(int i=0;i<100;i++){
        	cache = Unpooled.copiedBuffer(resp);
        	ctx.writeAndFlush(cache);
    	}
    }
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf cache = (ByteBuf) msg;
        byte[] req=new byte[cache.readableBytes()];
        cache.readBytes(req);
        String body=new String(req,"UTF-8");
        System.out.println("Now is "+body+" the counter is: "+ ++counter);
        
//    	byte[] resp="XXXXXX".getBytes();
//    	ByteBuf cache1 = Unpooled.copiedBuffer(resp);
//    	ctx.writeAndFlush(cache1);
        
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }



}
