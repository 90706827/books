package com.book.UdpSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 聊天工具接收端
 * @author jangni
 *
 */
public class chatSend implements Runnable {
	private DatagramSocket dSocket;
	public chatSend(DatagramSocket dSocket){
		this.dSocket = dSocket;
	}
	public void run(){
		try {
			BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			while((line=bufr.readLine())!=null){
				if("886".equals(line))
					break;
				byte[] buf = line.getBytes();
				DatagramPacket dPacket = new DatagramPacket(buf,buf.length,InetAddress.getByName("10.1.74.255"),10002);
				dSocket.send(dPacket);
			}
		} catch (Exception e) {
			throw new RuntimeException("发送端失败");
		}
	}
}
