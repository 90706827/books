package com.book.IP;

import java.net.*;

public class IPDemo {
	public static void main(String[] args) throws Exception{
		InetAddress ip =  InetAddress.getLocalHost();
		System.err.println(ip.toString());
		System.out.println(ip.getHostAddress());
		System.out.println(ip.getHostName());
		
		//获取任意一台主机
		InetAddress iAddress = InetAddress.getByName("www.baidu.com");
		System.out.println(iAddress.getHostAddress());
		System.out.println(iAddress.getHostName());
		
		
	}
}
