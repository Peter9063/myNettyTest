package org.myNettyTest.service.udp;

import java.io.File;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class UdpServer {
	
	private static Logger logger=LoggerFactory.getLogger(UdpServer.class);

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args){
		
		PropertyConfigurator.configure("config"+File.separator+"log4j.properties");
		
		logger.debug("serivce is startUp...");
		
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new UdpSeverHandler());
        
        try {
        	// 服务端监听在9999端口
			b.bind(9999).sync().channel().closeFuture().await();
		} catch (InterruptedException e) {
			logger.error("error",e);
		}
        
        logger.debug("serivce was closed...");

	}

}
