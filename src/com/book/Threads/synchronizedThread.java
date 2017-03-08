package com.book.Threads;

public class synchronizedThread {

	private static synchronizedThread sThread  = null;
	private synchronizedThread(){
		
	}
	/**
	 * @懒汉式
	 * 面试题：
	 * 1懒汉式和饿汉式有什么不同？懒汉式的特点是用于实例延迟加载
	 * 2懒汉式延迟加载有没有问题？有 多线程访问时有安全问题
	 * 3怎样解决懒汉式的安全问题？可以加同步来解决 但又有效率问题 用双重判断可以解决
	 * 4加同步时使用的锁是哪一个？该类所属的字节码为对象
	 */
	public static synchronizedThread getInstance(){
		if(sThread==null){//不为空直接返回
			synchronized(synchronizedThread.class){//多线程进入同步代码块
				if(sThread==null){//为空返回 
					sThread = new synchronizedThread();
				}
			}
		}
		return sThread;
	}
}class threadDemo{
	public static void main(String[] args) {
		
	}
}
