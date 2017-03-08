package com.book.UdpSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP传输方式 发送数据
 * @author jagnni
 *
 */
public class UdpSend2 {

	public static void main(String[] args)throws Exception{
		DatagramSocket dSocket = new DatagramSocket();
		BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while((line=bufr.readLine())!=null){
			if("886".equals(line))
				break;
			byte[] buf =line.getBytes();
			DatagramPacket dPacket = new DatagramPacket(buf,buf.length,InetAddress.getByName("10.1.74.88"),10001);
			dSocket.send(dPacket);
		}
		dSocket.close();
	}
}
