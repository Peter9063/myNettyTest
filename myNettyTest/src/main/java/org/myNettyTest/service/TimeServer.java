package org.myNettyTest.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
	
	/**
	 * Èë¿Ú
	 * @param args
	 */
	public static void main(String[] args) {
		
		new TimeServer().bind(8989);
		
	}
	
	public void bind(int port){
		
		EventLoopGroup bossGroup=new NioEventLoopGroup();
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		
		try{
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new TimeServerHandler());
					}
				});	
			
			ChannelFuture f=b.bind(port).sync();
			
			f.channel().closeFuture().sync();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	

}
