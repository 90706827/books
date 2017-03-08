package com.book.UdpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 聊天工具接收端
 * @author jangni
 *
 */
public class chatRece implements Runnable {
	
	private DatagramSocket dSocket;
	public chatRece(DatagramSocket dSocket){
		this.dSocket = dSocket;
	}
	public void run()  {
		try {
			while(true){
				byte[] buf = new byte[1024];
				DatagramPacket dPacket = new DatagramPacket(buf,buf.length);
				dSocket.receive(dPacket);
				String ip = dPacket.getAddress().getHostAddress();
				String data = new String(dPacket.getData(),0,dPacket.getLength());
				System.out.println(ip+"::"+data);
			}
		} catch (Exception e) {
			throw new RuntimeException("接收端失败");
		}
	}
}
