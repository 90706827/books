package com.book.TcpSocket;

import java.net.*;
import java.io.*;

/**
 * TCP客户端
 * @author Administrator
 *
 */
public class TcpClient2 {

	public static void main(String[] args)throws Exception{
		//建立socket服务 指定连接的主机和端口
		Socket s = new Socket("192.168.1.4",10004);
		//获取socket流中的输出流 将数据写入到该流中，通过网路发送给服务端
		OutputStream out = s.getOutputStream();
		out.write("服务端，你好".getBytes());
		//获取socket流中的输入流，将服务端反馈的数据获取到 并打印
		InputStream in  = s.getInputStream();
		byte[] buf = new byte[1024];
		int len = in.read(buf);
		System.out.println(new String(buf,0,len));
		//关闭客服端资源
		s.close();
	}
}
