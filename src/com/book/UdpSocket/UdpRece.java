package com.book.UdpSocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP传输方式 接受数据
 * @author jangni
 *
 */
public class UdpRece {
	
	public static void main(String[] args) throws Exception{
		//1.创建udp socket服务，建立端点，监听一个端口-就是给接受程序定义数字标示
		DatagramSocket dSocket = new DatagramSocket(10000);
		//2.定义数据包 存储接受数据
		byte[] buf = new byte[1024];
		DatagramPacket dPacket = new DatagramPacket(buf,buf.length);
		//3.通过服务的receive方法将接收到的数据存入数据包中
		dSocket.receive(dPacket);//没有数据就等 有数据就接受
		//4.通过数据包的方法获取其中的数据
		String ip = dPacket.getAddress().getHostAddress();
		String data = new String(dPacket.getData(),0,dPacket.getLength());
		int port = dPacket.getPort();
		System.out.println(ip+"::"+data+"::"+port);
		//5.关闭资源
		dSocket.close();
	}
}
