package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字节流 拷贝一个mp3文件
 */
public class BufferedInOutStreamDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		copy();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
	public static void copy(){
		try {
			BufferedInputStream bufis = new BufferedInputStream(new FileInputStream("D:\\yinger.mp3"));
			BufferedOutputStream bufos = new BufferedOutputStream(new FileOutputStream("D:\\copy.mp3"));
			int lin = 0;
			while((lin = bufis.read())!=-1){
				bufos.write(lin);
			}
			if(bufis!=null)bufis.close();
			if(bufos!=null)bufos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
