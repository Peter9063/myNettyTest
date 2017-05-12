package org.myNettyTest.client.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.myNettyTest.service.udp.UdpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClientHandler {
	
	private Logger logger=LoggerFactory.getLogger(UdpClientHandler.class);
	
	
	private DatagramSocket localUDPSocket = null;
	private Thread tread=null;
	
	/**
	 * 
	 * @param localPort
	 * @param serviceIp
	 * @param servicePort
	 * @return
	 */
	public UdpClientHandler(int localPort,String serviceIp,int servicePort){
        try
        {
            // UDP本地监听端口（如果为0将表示由系统分配，否则使用指定端口）
        	localUDPSocket = new DatagramSocket(localPort);
            // 调用connect之后，每次send时DatagramPacket就不需要设计目标主机的ip和port了
            // * 注意：connect方法一定要在DatagramSocket.receive()方法之前调用，
            // * 不然整send数据将会被错误地阻塞。这或许是官方API的bug，也或许是调
            // * 用规范就应该这样，但没有找到官方明确的说明
        	localUDPSocket.connect(InetAddress.getByName(serviceIp), servicePort);
        	localUDPSocket.setReuseAddress(true);
        	
        	logger.debug("new DatagramSocket()已成功完成.");
        }
        catch (Exception e)
        {
        	logger.error("error",e);
        }
		
	}
	
	public DatagramSocket getLocalUDPSocket(){
		return localUDPSocket;
	}
	
	public void send(String msg){
		
		
        byte[] soServerBytes=null;
		try {
			soServerBytes = msg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("error",e);
			return;
		}
		
        DatagramPacket packet = new DatagramPacket(soServerBytes, soServerBytes.length);
        try {
        	localUDPSocket.send(packet);
		} catch (IOException e) {
			logger.error("error",e);
			return;
		}
	}
	
	public void startUp(){
		tread = new Thread(new Runnable()
        {
                public void run()
                {
                        try
                        {
                        	logger.debug("本地UDP端口侦听中，端口=0...");
                            //开始侦听
                            udpListeningImpl();
                        }
                        catch (Exception ex)
                        {
                        	logger.error("error",ex);
                        }
                }
        });
		tread.start();
	}
	
    private void udpListeningImpl() throws Exception
    {
            while (true)
            {
                    byte[] data = new byte[1024];
                    // 接收数据报的包
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    if ((localUDPSocket == null) || (localUDPSocket.isClosed()))
                            continue;
                     
                    // 阻塞直到收到数据
                    localUDPSocket.receive(packet);
                     
                    // 解析服务端发过来的数据
                    String msg = new String(packet.getData(), 0 , packet.getLength(), "UTF-8");
                    logger.debug("【NOTE】>>>>>> 收到服务端的消息："+msg);
            }
    }
}
