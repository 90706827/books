package com.book.UdpSocket;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 开始启动聊天
 * @author jangni
 *
 */
public class startChatSocket {

	public static void main(String[] args) {
		DatagramSocket sendSocket;
		try {
			sendSocket = new DatagramSocket();
			DatagramSocket receSocket = new DatagramSocket(10002);
			new Thread(new chatSend(sendSocket)).start();
			new Thread(new chatRece(receSocket)).start();
		} catch (SocketException e) {
			System.out.println(e.getMessage());
		}
	}
}
