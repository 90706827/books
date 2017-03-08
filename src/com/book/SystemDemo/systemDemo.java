package com.book.SystemDemo;
import java.util.*;
public class systemDemo {

	/**
	 * @param args
	 * System
	 * out 标准输出，默认是控制台
	 * in 标准输入，默认是键盘
	 */
	public static void main(String[] args) {
		double [] serverMemeory = new double[4];
		serverMemeory[0] = (Runtime.getRuntime().maxMemory()/1024.0);//返回 Java 虚拟机试图使用的最大内存量。
        serverMemeory[1] = (Runtime.getRuntime().freeMemory()/1024.0);//返回 Java 虚拟机中的空闲内存量。
        serverMemeory[2] = (Runtime.getRuntime().totalMemory()/1024.0);//返回 Java 虚拟机中的内存总量。
        serverMemeory[3] = ((serverMemeory[1]/serverMemeory[2])*100);
		
		System.gc();//告诉垃圾收集器打算进行垃圾收集，而垃圾收集器进不进行收集是不确定的
		System.runFinalization();//强制调用已经失去引用的对象的finalize方法 
		
		//获取版本信息 及系统信息 做更新时可以判断是否支持
		Properties prop = System.getProperties();
		//自己设定一下虚拟机信息
		System.setProperty("mykey","myvalue");
		//获取指定属性信息
		System.out.println(prop.getProperty("os.name"));
		
		//可不可以在jvm启动时 动态加载一些属性信息
		//java -Dhaha-yeshaha systemDemo  用大D写入键值对 
		System.out.println(prop.getProperty("haha"));
		//获取所有
		//因为properties是hashtable的子类
		//可以通过map方法取值,该集合存储的都是字符串 没有泛型定义
		//虚拟机打印默认状态下加载信息
		for(Object obj : prop.keySet()){
			String value = (String)prop.get(obj);
			System.out.println(obj+"::"+value);
		}
	}

}
