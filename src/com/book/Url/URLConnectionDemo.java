package com.book.Url;

import java.net.*;
import java.io.*;

public class URLConnectionDemo{

	public static void main(String[]args)throws Exception{
		URL url = new URL("http://www.xilinke.cn");
		URLConnection conn = url.openConnection();
		InputStream in = conn.getInputStream();
		byte[] buf = new byte[1025];
		while(true){
			int len = in.read(buf);
			if (len<=0) {
				break;
			}
			System.out.print(new String(buf,0,len,"utf-8"));
			
		}
		
	}
}
