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
            // ��ȡ�յ�������
            ByteBuf buf = (ByteBuf) packet.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, CharsetUtil.UTF_8);
            
            
            InetSocketAddress sourAdd= packet.sender();
            logger.debug("��NOTE��>>>>>> �յ�"+sourAdd.getAddress()+":"+sourAdd.getPort()+"�ͻ��˵����ݣ�"+body);
            
            
            String res="Hello "+sourAdd.getAddress()+":"+sourAdd.getPort()+"�ͻ��ˣ�����Server,�յ��������"+body+"���ҵ�ʱ�����"+System.currentTimeMillis();
            // �ظ�һ����Ϣ���ͻ���
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
