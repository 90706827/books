package com.book.RuntimeDemo;

import java.io.IOException;

public class runtimeDemo {

	/**
	 * @param args
	 * Runtime对象
	 * 该类并没有提供构造函数
	 * 说明不可以new对象，那么会直接想到该类中的方法都是静态的
	 * 发现该类中海油非静态方法
	 * 说明该类肯定会提供方法获取本类对象，而且该方法是静态的 并返回值类型是本类型
	 * 该方式是static Runtime getRuntime() 使用了单例设计模式
	 * 
	 */
	public static void main(String[] args) throws Exception {
		Runtime r = Runtime.getRuntime();
		Process s = r.exec("D:\\4-6.exe");//执行后打开该文件
		Thread.sleep(5000);
		s.destroy();//杀掉此进程
		s = r.exec("notepad.exe D:\\text.txt");
	}

}
