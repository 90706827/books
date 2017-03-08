package com.book.UdpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP传输方式 发送数据
 * @author jangni
 *
 */
public class UdpSend {
	
	public static void main(String[] args) throws Exception {
		//1.创建udp服务 通过DatagramSocket对象
		DatagramSocket ds = new DatagramSocket(8888);//默认一个端口 这里可以指定
		//2.确定数据，并封装成数据包
		byte[] data ="udp ge me lai le".getBytes();
		DatagramPacket dPacket = new DatagramPacket(data,data.length,InetAddress.getByName("10.1.74.88"),10000);
		//3.通过socket服务，将已有的数据包发送出去，通过send方法
		ds.send(dPacket);
		//4.关闭资源
		ds.close();
		
	}
}
