package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *字符流读取流缓冲区
 */
public class BufferedReaderDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException {
		//创建一个读取流
		FileReader fr = new FileReader("E:\nohup.out");
		//加入缓冲区
		BufferedReader br = new BufferedReader(fr);
		
		//一行一行读取
		String str = null;
		while((str=br.readLine())!=null){//readLine()返回NULL
			System.out.println(str);
		}
		br.close();
	}

}
