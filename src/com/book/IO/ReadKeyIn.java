package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字节流读取键盘录入
 * 字节流换字符流使用BufferReader的ReadLine
 */
public class ReadKeyIn {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException {
		reader_3();
	}
	/**
	 * 怎样选择各种流的使用
	 * 1、明确源和目的
	 * 	 源：输入流 InputStream Reader
	 * 目的：输出流 OutputStream Writer
	 * 2、操作的数据是否是纯文本
	 *  是  ：字符流
	 *  不是：字节流
	 * 3、当体系明确后，在明确要使用哪个具体的对象
	 * 	通过设备来区分：
	 * 	源设备：内存 硬盘 键盘
	 *  目的设备： 内存 硬盘 控制台
	 *  
	 * 1、将一个文件中的数据存储到另一个文件中，复制文件
	 * 	源：文本文件 Reader
	 * 	设备：硬盘 FileReader
	 *  提高效率：BufferedReader
	 *  
	 *  目的:OutputStream writer
	 *  文本：writer
	 *  设备：硬盘 fileWriter
	 *  提高效率 BuferedWriter
	 */
	public static void reader_3()throws IOException{
		//键盘录入常用写法
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("out.txt")));//System.in
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out ));//new FileOutputStream("out.txt")
		String line = null;
		while((line = br.readLine())!=null){
			if(line.equals("over"))
				break;
			bw.write(line);
			bw.newLine();
			bw.flush();
		}
		br.close();
	}
	public static void reader_2()throws IOException{
		InputStream in = System.in;
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while((line = br.readLine())!=null){
			System.out.print(line);
		}
		br.close();
	}
	public static void reader_1()throws IOException{
		InputStream in = System.in;
		StringBuilder sb = new StringBuilder();
		while(true){
			int chr = in.read();
			if(chr=='\r')
			{
				continue;
			}else if(chr=='\n'){
				String s = sb.toString();
				if(s.equals("over")){
					break;
				}else{
					System.out.println(s);
					new StringBuilder();
					sb.delete(0, sb.length());
				}
			}else{
				sb.append((char)chr);
			}
			
		}
	}
}
