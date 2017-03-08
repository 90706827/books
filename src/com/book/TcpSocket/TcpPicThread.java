package com.book.TcpSocket;

import java.io.*;
import java.net.*;

/**
 * 多线程 服务端接收客户端上传的图片
 * @author Administrator
 *
 */
public class TcpPicThread implements Runnable {
	private Socket s;
	public TcpPicThread(Socket s){
		this.s = s;
	}
	public void run() {
		int count = 1;
		String ip = s.getInetAddress().getHostAddress();
		try{
			System.out.println(ip+"---");
			InputStream in = s.getInputStream();
			
			File file = new File("D:\\"+ip+"-"+count+".jpg");
			while(file.exists())
				file = new File("D:\\"+ip+"-"+(count++)+".jpg");
			
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len=0;
			while((len=in.read(buf))!=-1){
				fos.write(buf,0,len);
			}
			
			OutputStream os = s.getOutputStream();
			os.write("上传成功".getBytes());
			
			fos.close();
			s.close();
		}catch(Exception e){
			throw new RuntimeException(ip+":"+e.getMessage());
		}
		
	}

}
