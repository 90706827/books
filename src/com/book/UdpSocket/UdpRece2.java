package com.book.UdpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP传输方式 接受数据
 * @author jagnni
 *
 */
public class UdpRece2 {
	public static void main(String[] args)throws Exception {
		DatagramSocket dSocket = new DatagramSocket(10001);
		while (true) {
			byte[] buf = new byte[104];
			DatagramPacket dPacket = new DatagramPacket(buf,buf.length);
			dSocket.receive(dPacket);
			String ip = dPacket.getAddress().getHostAddress();
			String data = new String(dPacket.getData(),0,dPacket.getLength());
			System.out.println(ip+"::"+data);
		}
	}
}
