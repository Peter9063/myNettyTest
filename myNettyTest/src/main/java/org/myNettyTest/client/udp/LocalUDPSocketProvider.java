package org.myNettyTest.client.udp;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class LocalUDPSocketProvider {
//    private static final String TAG = LocalUDPSocketProvider.class.getSimpleName();
    private static LocalUDPSocketProvider instance = null;
    private DatagramSocket localUDPSocket = null;

    public static LocalUDPSocketProvider getInstance()
    {
            if (instance == null)
                    instance = new LocalUDPSocketProvider();
            return instance;
    }
     
    public void initSocket()
    {
            try
            {
                    // UDP���ؼ����˿ڣ����Ϊ0����ʾ��ϵͳ���䣬����ʹ��ָ���˿ڣ�
                    this.localUDPSocket = new DatagramSocket(0);
                    // ����connect֮��ÿ��sendʱDatagramPacket�Ͳ���Ҫ���Ŀ��������ip��port��
                    // * ע�⣺connect����һ��Ҫ��DatagramSocket.receive()����֮ǰ���ã�
                    // * ��Ȼ��send���ݽ��ᱻ�����������������ǹٷ�API��bug��Ҳ�����ǵ�
                    // * �ù淶��Ӧ����������û���ҵ��ٷ���ȷ��˵��
                    this.localUDPSocket.connect(InetAddress.getByName("127.0.0.1"), 9999);
                    this.localUDPSocket.setReuseAddress(true);
                    System.out.println("new DatagramSocket()�ѳɹ����.");
                    
//                    Log.d(TAG, "new DatagramSocket()�ѳɹ����.");
            }
            catch (Exception e)
            {
            	System.out.println("localUDPSocket����ʱ����ԭ���ǣ�" + e.getMessage());
//                    Log.w(TAG, "localUDPSocket����ʱ����ԭ���ǣ�" + e.getMessage(), e);
            }
    }

    public DatagramSocket getLocalUDPSocket()
    {
            return this.localUDPSocket;
    }
}
