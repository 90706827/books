package com.book.TcpSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * TCP服务端接收客户端发来的数据并返回给客户端数据
 * @author Administrator
 *
 */
public class TcpTestServer1 {
	public static void main(String[] args)throws Exception {
		ServerSocket ss = new ServerSocket(10005);
		Socket s = ss.accept();
		//读取socket中的数据
		BufferedReader bufr = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		//socket输出流 将大写数据写入到socket输出流 并发送给客户端
//		BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		PrintWriter pWriter = new PrintWriter(s.getOutputStream(),true);
		String line = null;
		while((line=bufr.readLine())!=null){//必须要有回车符才接受完成
			System.out.println(line);
			pWriter.print(line);
//			bufw.write(line.toUpperCase());
//			bufw.newLine();
//			bufw.flush();
		}
		s.close();
		ss.close();
	}
}
 