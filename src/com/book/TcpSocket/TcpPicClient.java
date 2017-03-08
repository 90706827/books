package com.book.TcpSocket;

import java.net.*;
import java.io.*;
/**
 * TCP客户端上传图片并接收返回结果
 * @author Administrator
 *
 */
public class TcpPicClient {
	public static void main(String[] args)throws Exception{
		
		if(args.length!=1){
			System.out.println("请选择一个jpg格式的图片");
			return;
		}
		File file = new File(args[0]);
		if(!(file.exists() && file.isFile())){
			System.out.println("该文件有问题，要么不存在，要么不是文件");
			return;
		}
		if(!file.getName().endsWith(".jpg")){
			System.out.println("图片格式错误，请重新选择");
			return;
		}
		if(file.length()>1024*1024*5){
			System.out.println("文件过大，没干好像");
			return;
		}
		
		
		Socket s = new Socket("192.168.1.4",10007);
		FileInputStream fis = new FileInputStream(file);
		OutputStream out = s.getOutputStream();
		byte[] buf = new byte[1024];
		int len = 0;
		while((len=fis.read(buf))!=-1){
			out.write(buf,0,len);
		}
		s.shutdownOutput();
		
		InputStream in = s.getInputStream();
		byte[] bufin=new byte[1024];
		int num = in.read(bufin);
		System.out.println(new String(bufin,0,num));
		
		fis.close();
		s.close();
	}
}
