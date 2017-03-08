package com.book.TcpSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP客户端发送并接受服务端传输的数据
 * @author Administrator
 *
 */
public class TcpTestClient1 {
	public static void main(String[] args)throws Exception {
		//建立服务
		Socket s = new Socket("10.1.74.88",10005);
		//获取键盘录入
		BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
		//将数据发给服务端
//		BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		PrintWriter pWriter = new PrintWriter(s.getOutputStream(),true);
		//取服务端返回的大写数据
		BufferedReader bufIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line = null;
		while ((line=bufr.readLine())!=null) {
			if("over".equals(line))
				break;
			pWriter.print(line);
//			bufw.write(line);
//			bufw.newLine();//同等于回车换行
//			bufw.flush();
			String str = bufIn.readLine();
			System.out.println("server:"+str);
		}
		//结束，关闭资源
		bufr.close();
		s.close();
	}
}