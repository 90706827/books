package com.book.TcpSocket;

import java.net.*;
import java.io.*;

/**
 * @author jangni
 * TCP 服务端
 *
 */
public class TcpServer {

	public static void main(String[] args) throws Exception{
		//建立服务端的socket服务 serverSocket() 并监听一个端口
		ServerSocket ss = new ServerSocket(10003);
		//获取链接过来的客户端对象 通过serverSocket的accept方法 
		//没有连接就会等 所以这个方法阻塞式的
		Socket s = ss.accept();
		String ip = s.getInetAddress().getHostAddress();
		System.out.print(ip+"::");
		//客户端发数据过来，服务端要使用对应的客户端对象，并获取到该客服端对象的
		//读取流来读取发过来的数据并打印在控制台
		InputStream in = s.getInputStream();
		byte[] buf = new byte[1024];
		int len = in.read(buf);
		System.out.println(new String(buf,0,len));
		//关闭服务端（可选）服务端一般常开
		s.close();
		ss.close();
	}
}
