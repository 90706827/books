package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *字符流
 */
public class FileReaderDemo {
	public static void main(String[] args)throws IOException{
		FileReader fr = new FileReader("demo.txt");
//		//单个字符读取
//		while(true){
//			int chr = fr.read(); //读到没有后chr得到的值是-1
//			if(chr==-1)break;
//			System.out.print((char)chr);
//		}
		char[] car = new char[1024];
		while(true){
			int chr = fr.read(car);
			if(chr==-1)break;
			System.out.println(new String(car,0,chr));
		}
		fr.close();
	}
}
