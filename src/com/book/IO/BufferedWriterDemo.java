package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *字符流写入流缓冲区
 */
public class BufferedWriterDemo {
	public static void main(String[] args)throws IOException{
		//创建一个字符写入流对象
		FileWriter fw = new FileWriter("Readr.txt");
		//为了提高写入流效率，加入缓冲技术
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("abcde");
		bw.newLine();//换行符 代替\r\n
		//缓冲要刷新才可写入
		bw.flush();
		//关闭缓冲区就是关闭缓冲区的流对象所以不用写fw.close();
		bw.close();
		
	}	
}
