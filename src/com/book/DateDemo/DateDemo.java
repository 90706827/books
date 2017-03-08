package com.book.DateDemo;
import java.util.*;
import java.text.*;
public class DateDemo {
	public static void main(String[] args){
		Date d = new Date();
		System.out.println(d);
		//模式封装到sdf中
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss");
		System.out.println(sdf.format(d));
	}
}
