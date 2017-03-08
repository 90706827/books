package com.book.Url;
import java.net.*;
public class UrlDemo {
	public static void main(String[] args)throws MalformedURLException{
		URL url = new URL("http://192.168.1.254:8080/myweb/demo.html?name=haha&age=30");
		System.out.println(url.getProtocol());
		System.out.println(url.getHost());
		System.out.println(url.getPort());
		System.out.println(url.getPath());
		System.out.println(url.getFile());
		System.out.println(url.getQuery());
	}
}
