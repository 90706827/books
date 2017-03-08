package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字符流
 */
public class LineNumberReaderDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws IOException {
		FileReader fr = new FileReader("Reader.txt");
		LineNumberReader lnr = new LineNumberReader(fr);
		String line = null;
		lnr.setLineNumber(2);
		while((line = lnr.readLine())!=null){
			System.out.println(lnr.getLineNumber()+":"+line);
		}
		lnr.close();
	}

}
