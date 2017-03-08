package com.book.Math;

import java.lang.*;
import java.util.Random;

public class MathDemo {
	
	public static void main(String[] args) {
		Double d = Math.abs(-12.25);//绝对值
		d = Math.ceil(11.35);//ceil返回大于指定数据的最小整数
		d = Math.floor(12.25);//返回小于指定数的最大整数
		long l = Math.round(12.45);//四舍五入
		int i = (int)(Math.random()*100000);//随机数
		Random random = new Random();
		i = random.nextInt(100000);//随机数
		sop(d);
		sop(l);
		sop(i);
	}
	public static void sop(Object o){
		System.out.println(o);
	}
}









