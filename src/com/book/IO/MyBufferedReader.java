package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *	字符流自定义装饰类
 */
public class MyBufferedReader extends Reader {
	private Reader fr = null;
	MyBufferedReader(Reader fr){
		this.fr = fr;
	}
	public String MyReadLine()throws IOException{
		StringBuilder sb = new StringBuilder();
		int str = 0;
		while((str=fr.read())!=-1){
			if(str=='\r')
				continue;
			if(str=='\n')
				return sb.toString();
			else
				sb.append((char)str);
		}
		if(sb.length()>0)
			return sb.toString();
		return null;
	}
	public void MyClose()throws IOException{
		fr.close();
	}
	@Override
	public void close() throws IOException {
		fr.close();
	}
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return fr.read(cbuf, off, len);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("Reader.txt");
		MyBufferedReader mbr = new MyBufferedReader(fr);
		String str = null;
		while((str=mbr.MyReadLine())!=null){
			System.out.println(str);
		}
		mbr.MyClose();
	}
}
