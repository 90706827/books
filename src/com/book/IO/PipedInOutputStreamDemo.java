package com.book.IO;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author Administrator
 *	管道流 读取和写入不能同时要不会阻塞
 */
public class PipedInOutputStreamDemo {

	public static void main(String[] args)throws Exception {
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream();
		inputStream.connect(outputStream);
		Write write = new Write(outputStream);
		Read read = new Read(inputStream);
		new Thread(read).start();
		new Thread(write).start();
	}

}class Write implements Runnable{
	private PipedOutputStream out;
	Write(PipedOutputStream out){
		this.out=out;
	}
	public void run() {
		try {
			System.out.println("开始写入数据等待6秒....");
			Thread.sleep(6000);
			out.write("piped lai la".getBytes());
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("管道写入失败");
		}
	}
	
}class Read implements Runnable{
	private PipedInputStream in;
	Read(PipedInputStream in){
		this.in=in;
	}
	public void run() {
		try {
			byte[] buf = new byte[1024];
			System.out.println("读取前... 没有数据阻塞");
			int len = in.read(buf);
			System.out.println("读取前... 阻塞结束");
			String string = new String(buf,0,len);
			System.out.println(string);
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("管道读取失败");
		}
	}
	
}
