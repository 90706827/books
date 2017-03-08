package com.book.IO;

import java.io.*;

/**
 * @author Administrator
 * 字符流
 */
public class MyLineNumberReader extends MyBufferedReader {
	private int lineNumber;
	MyLineNumberReader(Reader fr) {
		super(fr);
	}
	public String myReadLine()throws IOException{
		lineNumber++;
		return super.MyReadLine();
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public static void main(String[] args)throws IOException{
		FileReader fr = new FileReader("Reader.txt");
		MyLineNumberReader mlnr = new MyLineNumberReader(fr);
		String line = null;
		while((line = mlnr.myReadLine())!=null){
			System.out.println(mlnr.getLineNumber()+":"+line);
		}
		mlnr.close();
	}
}
