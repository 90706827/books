package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字节流 拷贝一个图片文件
 */
public class InOutStreamImageDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("D:\\bg.jpg");
			fos = new FileOutputStream("D:\\b.jpg");
			byte[] buf = new byte[1024];
			int ch = 0;
			while((ch = fis.read(buf))!=-1){
				fos.write(buf, 0, ch);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis!=null)
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(fos!=null)
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
