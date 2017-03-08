package com.book.DateDemo;
import java.util.*;
public class CalendarDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		System.out.println(cal);
		System.out.println(cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月"+(cal.get(Calendar.DAY_OF_MONTH)+1)+"日");
	}

}
