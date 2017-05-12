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
            // UDP���ؼ����˿ڣ����Ϊ0����ʾ��ϵͳ���䣬����ʹ��ָ���˿ڣ�
        	localUDPSocket = new DatagramSocket(localPort);
            // ����connect֮��ÿ��sendʱDatagramPacket�Ͳ���Ҫ���Ŀ��������ip��port��
            // * ע�⣺connect����һ��Ҫ��DatagramSocket.receive()����֮ǰ���ã�
            // * ��Ȼ��send���ݽ��ᱻ�����������������ǹٷ�API��bug��Ҳ�����ǵ�
            // * �ù淶��Ӧ����������û���ҵ��ٷ���ȷ��˵��
        	localUDPSocket.connect(InetAddress.getByName(serviceIp), servicePort);
        	localUDPSocket.setReuseAddress(true);
        	
        	logger.debug("new DatagramSocket()�ѳɹ����.");
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
                        	logger.debug("����UDP�˿������У��˿�=0...");
                            //��ʼ����
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
                    // �������ݱ��İ�
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    if ((localUDPSocket == null) || (localUDPSocket.isClosed()))
                            continue;
                     
                    // ����ֱ���յ�����
                    localUDPSocket.receive(packet);
                     
                    // ��������˷�����������
                    String msg = new String(packet.getData(), 0 , packet.getLength(), "UTF-8");
                    logger.debug("��NOTE��>>>>>> �յ�����˵���Ϣ��"+msg);
            }
    }
}
