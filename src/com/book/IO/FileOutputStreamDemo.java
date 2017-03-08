package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字符流写入
 */
public class FileOutputStreamDemo {

	/**
	 * @param args
	 * 
	 * 	 
	 * */
	public static void main(String[] args) {
		try {
//			writerFile();
//			readFile();
			readFile_1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void writerFile()throws IOException{
		FileOutputStream fos = new FileOutputStream("Reader.txt");
		fos.write("abcde".getBytes());
		fos.close();
	}
	public static void readFile_1()throws IOException{
		FileInputStream fis = new FileInputStream("Reader.txt");
		int num = fis.available();//返回文本有多少字节
		System.out.println(num);
		byte[] buf = new byte[1024];
		int ch = 0;
		while((ch = fis.read(buf))!=-1){
			System.out.println(new String(buf,0,ch));
		}
		fis.close();
	}
	public static void readFile()throws IOException{
		FileInputStream fis = new FileInputStream("Reader.txt");
		int ch = 0;
		while((ch = fis.read())!=-1){
			System.out.println((char)ch);
		}
		fis.close();
	}
}
