package com.book.IO;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
/**
 * @author Administrator
 * 文件切割
 */
public class SplitDemo {

	public static void main(String[] args)throws IOException {
		FileInputStream fis = new FileInputStream("b.jpg");
		FileOutputStream fos = null;
		byte[] buf = new byte[150*1024];
		int len = 0;
		int count=1;
		while((len=fis.read(buf))!=-1){
			fos = new FileOutputStream((count++)+".jpg");
			fos.write(buf,0,len);
			fos.close();
		}
		fis.close();
		
		ArrayList<FileInputStream> aList = new ArrayList<FileInputStream>();
		aList.add(new FileInputStream("1.jpg"));
		aList.add(new FileInputStream("2.jpg"));
		aList.add(new FileInputStream("3.jpg"));
		final Iterator<FileInputStream> iterator = aList.iterator();
		Enumeration<FileInputStream> enumeration = new Enumeration<FileInputStream>(){
			public boolean hasMoreElements(){
				return iterator.hasNext();
			}
			public FileInputStream nextElement(){
				return iterator.next();
			}
		};
		SequenceInputStream sis = new SequenceInputStream(enumeration);
		FileOutputStream foss = new FileOutputStream("a.jpg");
		byte[] byf = new byte[1024];
		int lens =0;
		while((lens=sis.read(byf))!=-1){
			foss.write(byf,0,lens);
			foss.flush();
		}
		foss.close();
		sis.close();
	}

}
