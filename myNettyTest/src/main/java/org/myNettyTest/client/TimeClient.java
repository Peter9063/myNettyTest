package org.myNettyTest.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {

	/**
	 * Èë¿Ú
	 * @param args
	 */
	public static void main(String[] args) {
		
		new TimeClient().connect(8989,"127.0.0.1");
	}

	public void connect(int port, String host) {
		
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		
		try{
			Bootstrap b=new Bootstrap();
			b.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new TimeClientHandler());
					}
				});	
			
			ChannelFuture f=b.connect(host,port).sync();
			
			f.channel().closeFuture().sync();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			workerGroup.shutdownGracefully();
		}
		
	}

	
	
}
