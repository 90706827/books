package com.book.IO;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Administrator
 * 打印字节流
 * PrintStream 
 * 构造对象可以接收 
 * file 
 * 字符串路径 
 * OutputStream
 * 
 * 打印字符流
 * PrintStream
 * file 
 * 字符串路径 
 * OutputStream
 * Writer
 */
public class PrintDemo {

	public static void main(String[] args)throws IOException {
		printWriter();
	}
	public static void printWriter()throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(new FileWriter("a.txt"),true);
		String line = null;
		while((line=br.readLine())!=null){
			if ("over".equals(line)) {
				break;
			}
			pw.println(line);
		}
		pw.close();
		br.close();
		
		
		
		
	}
}
