package com.book.TcpSocket;

import java.net.*;
import java.io.*;

/**
 * TCP 文件发送客户端
 * @author Administrator
 *
 */
public class TcpFileClient {

	public static void main(String[] args) throws Exception{
		Socket s = new Socket("192.168.1.4",10006);
		BufferedReader bufr = new BufferedReader(new FileReader("D:\\test.txt"));
		PrintWriter out = new PrintWriter(s.getOutputStream(),true);
//		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//		long time = System.currentTimeMillis();
//		dos.writeLong(time);
		String line = null;
		while((line=bufr.readLine())!=null){
			out.println(line);
		}
//		dos.writeLong(time);
		s.shutdownOutput();//关闭客户端的输出流-相当于给流中加入一个结束标记
		
		BufferedReader bufIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String str = bufIn.readLine();
		System.out.println(str);
		bufr.close();
		s.close();
	}
}