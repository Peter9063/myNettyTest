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
            // 要发送的数据
        	Long time=System.currentTimeMillis();
        	for(int i=0;i<list.size();i++){
        		list.get(i).send("Hi，我是客户端"+i+"，我的时间戳"+time);
        	}
             
            // 3000秒后进入下一次循环
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
        // 初始化本地UDP的Socket
        LocalUDPSocketProvider.getInstance().initSocket();
        // 启动本地UDP监听（接收数据用的）
        LocalUDPDataReciever.getInstance().startup();
         
        // 循环发送数据给服务端
        while(true)
        {
                // 要发送的数据
                String toServer = "Hi，我是客户端，我的时间戳"+System.currentTimeMillis();
                byte[] soServerBytes=null;
				try {
					soServerBytes = toServer.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                 
                // 开始发送
                DatagramPacket packet = new DatagramPacket(soServerBytes, soServerBytes.length);
                DatagramSocket localUDPSocket =LocalUDPSocketProvider.getInstance().getLocalUDPSocket();
                try {
					localUDPSocket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                System.out.println("发往服务端的信息已送出.");
                		
//                		UDPUtils.send(soServerBytes, soServerBytes.length);
//                if(ok)
//                	System.out.println("发往服务端的信息已送出.");
////                        Log.d("EchoClient", "发往服务端的信息已送出.");
//                else
//                	System.out.println("发往服务端的信息没有成功发出！！！");
//                        Log.e("EchoClient", "发往服务端的信息没有成功发出！！！");
                 
                // 3000秒后进入下一次循环
                try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}

}
