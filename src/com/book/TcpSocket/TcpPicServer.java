package com.book.TcpSocket;

import java.net.*;
import java.io.*;
/**
 * TCP服务端接收上传数据并返回结果
 * @author Administrator
 *
 */
public class TcpPicServer {
	public static void main(String[] args)throws Exception{
		ServerSocket ss = new ServerSocket(10007);
		
		while(true){
			Socket s = ss.accept();
			new Thread(new TcpPicThread(s)).start();
		}
//		InputStream in = s.getInputStream();
//		FileOutputStream fos = new FileOutputStream("D:\\Tcp.JPG");
//		byte[] buf = new byte[1024];
//		int len=0;
//		while((len=in.read(buf))!=-1){
//			fos.write(buf,0,len);
//		}
//		
//		OutputStream os = s.getOutputStream();
//		os.write("上传成功".getBytes());
//		
//		fos.close();
//		s.close();
//		ss.close();
	}
	
	
}
