package com.book.TcpSocket;

import java.io.*;
import java.net.*;
/**
 * @author jangni
 *TCP 客服端
 */
public class TcpClient {

	public static void main(String[] args) throws Exception{
		//创建客户端的socket服务 制定目的主机和端口
		Socket s = new Socket("192.168.1.4",10003);
		//为了发送数据，应该获取socket流中的输出流
		OutputStream out =s.getOutputStream();
		out.write("tcp ge men lai le".getBytes());
		s.close();
	}
}
