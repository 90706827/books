package com.book.TcpSocket;

import java.net.*;
import java.io.*;
/**
 * TCP文件发送服务端
 * @author Administrator
 *
 */
public class TcpFileServer {

	public static void main(String[] args)throws Exception{
		ServerSocket ss = new ServerSocket(10006);
		Socket s = ss.accept();
		String ip = s.getInetAddress().getHostAddress();
		System.out.println(ip+":connected");
		
//		DataInputStream dis = new DataInputStream(s.getInputStream());
//		long l = dis.readLong();
		
		BufferedReader bufr = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(new FileWriter("F:\\server.txt"),true);
		String line = null;
		while((line=bufr.readLine())!=null){
//			if(line.equals(l))
//				break;
			out.println(line);
		}
		PrintWriter pw = new PrintWriter(s.getOutputStream(),true);
		pw.println("上传成功");
		out.close();
		s.close();
		ss.close();
	}
}
