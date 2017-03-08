package com.book.TcpSocket;

import java.net.*;
import java.io.*;

/**
 * TCP服务端
 * @author Administrator
 *
 */
public class TcpServer2 {

	public static void main(String[] args)throws Exception{
		ServerSocket ss = new ServerSocket(10004);
		Socket s = ss.accept();
		String ip = s.getInetAddress().getHostAddress();
		System.out.println(ip+"::");
		
		InputStream in = s.getInputStream();
		byte[] buf = new byte[1024];
		int len = in.read(buf);
		System.out.println(new String(buf,0,len));
		
		OutputStream out = s.getOutputStream();
		out.write("哥们收到，你也好".getBytes());
		
		s.close();
		ss.close();
	}
}
