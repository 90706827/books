package com.book.IO;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 文件合并
 * @author Administrator
 * 多个流文件 同事写入到一个文件中
 */
public class SequenceDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException {
		Vector<FileInputStream> vector = new Vector<FileInputStream>();
		vector.add(new FileInputStream("1.txt"));
		vector.add(new FileInputStream("2.txt"));
		vector.add(new FileInputStream("3.txt"));
		Enumeration<FileInputStream> enumeration = vector.elements();
		SequenceInputStream sis = new SequenceInputStream(enumeration);
		FileOutputStream fos = new FileOutputStream("4.txt");
		byte[] buf = new byte[1024];
		int len = 0;
		while((len=sis.read(buf))!=-1){
			fos.write(buf,0,len);
		}
		fos.close();
		sis.close();
	}

}
