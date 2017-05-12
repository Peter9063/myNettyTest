package org.myNettyTest.service.udp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpSeverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	
	private Logger logger=LoggerFactory.getLogger(UdpServer.class);

	@Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
    {	
            // 读取收到的数据
            ByteBuf buf = (ByteBuf) packet.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, CharsetUtil.UTF_8);
            
            
            InetSocketAddress sourAdd= packet.sender();
            logger.debug("【NOTE】>>>>>> 收到"+sourAdd.getAddress()+":"+sourAdd.getPort()+"客户端的数据："+body);
            
            
            String res="Hello "+sourAdd.getAddress()+":"+sourAdd.getPort()+"客户端，我是Server,收到你的内容"+body+"，我的时间戳是"+System.currentTimeMillis();
            // 回复一条信息给客户端
            try {
				ctx.writeAndFlush(new DatagramPacket(
				Unpooled.copiedBuffer(res
				                , CharsetUtil.UTF_8)
				                , packet.sender())).sync();
			} catch (InterruptedException e) {
				logger.error("error"+e);
			}
    }

}
