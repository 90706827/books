package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *	字节流缓冲区
 */
public class MyBufferedInputStream {
	private InputStream in;
	private byte[] buf = new byte[1024*4];
	private int pos =0,count=0;
	MyBufferedInputStream(InputStream in){
		this.in = in;
	}
	public int myRead()throws IOException{
		if(count==0){
			count = in.read(buf);
			if(count<0){
				return -1;
			}
			pos = 0;
			byte b = buf[pos];
			count--;
			pos++;
			return b&255;
		}else if(count>0){
			byte b = buf[pos];
			count--;
			pos++;
			return b&0xff;
		}
		return -1;
	}
	public void myClose()throws IOException{
		in.close();
	}
	public static void main(String[] args)throws IOException {
		MyBufferedInputStream bufis = new MyBufferedInputStream(new FileInputStream("D:\\yinger.mp3"));
		BufferedOutputStream bufos = new BufferedOutputStream(new FileOutputStream("D:\\copy_1.mp3"));
		int lin = 0;
		while((lin = bufis.myRead())!=-1){
			bufos.write(lin);
		}
		if(bufis!=null)bufis.myClose();
		if(bufos!=null)bufos.close();
	}

}
