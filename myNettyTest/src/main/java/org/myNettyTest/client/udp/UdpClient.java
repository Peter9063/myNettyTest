package org.myNettyTest.client.udp;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

public class UdpClient {
	
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("config"+File.separator+"log4j2Client.properties");
		
		int clientSize=10000;
		
		List<UdpClientHandler> list=new ArrayList<UdpClientHandler>();
		for(int i=0;i<clientSize;i++){
			UdpClientHandler item=new UdpClientHandler(0,"127.0.0.1",9999);
			item.startUp();
			list.add(item);
			
		}
		
        while(true)
        {
            // Ҫ���͵�����
        	Long time=System.currentTimeMillis();
        	for(int i=0;i<list.size();i++){
        		list.get(i).send("Hi�����ǿͻ���"+i+"���ҵ�ʱ���"+time);
        	}
             
            // 3000��������һ��ѭ��
            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}


	/**
	 * @param args
	 */
	public static void main1(String[] args) {
        // ��ʼ������UDP��Socket
        LocalUDPSocketProvider.getInstance().initSocket();
        // ��������UDP���������������õģ�
        LocalUDPDataReciever.getInstance().startup();
         
        // ѭ���������ݸ������
        while(true)
        {
                // Ҫ���͵�����
                String toServer = "Hi�����ǿͻ��ˣ��ҵ�ʱ���"+System.currentTimeMillis();
                byte[] soServerBytes=null;
				try {
					soServerBytes = toServer.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 
                // ��ʼ����
                DatagramPacket packet = new DatagramPacket(soServerBytes, soServerBytes.length);
                DatagramSocket localUDPSocket =LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
                try {
					localUDPSocket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println("��������˵���Ϣ���ͳ�.");
                		
//                		UDPUtils.send(soServerBytes, soServerBytes.length);
//                if(ok)
//                	System.out.println("��������˵���Ϣ���ͳ�.");
////                        Log.d("EchoClient", "��������˵���Ϣ���ͳ�.");
//                else
//                	System.out.println("��������˵���Ϣû�гɹ�����������");
//                        Log.e("EchoClient", "��������˵���Ϣû�гɹ�����������");
                 
                // 3000��������һ��ѭ��
                try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}

}
