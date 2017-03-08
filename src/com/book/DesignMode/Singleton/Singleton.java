package com.book.DesignMode.Singleton;

/**
 * 单例设计模式
 * @author jangni
 * 单例要素：
 *  1、私有的构造方法
 *  2、指向自己实力的私有静态引用
 *  3、以自己实力为返回值得静态的共有的方法
 *  单例模式的有点：
 *  1、在内存中只有一个对象，节省内存空间
 *  2、避免频繁的创建销毁对象，可以提高性能
 *  3、避免对共享资源的多重占用
 *  4、可以全局访问
 */
public class Singleton {
	/*
	 饿汉式单例
	private static Singleton singleton = new Singleton();
	private Singleton(){}
	public static Singleton getInstance(){
		return singleton;
	}
	 */
	/*
	 * 懒汉式单例
	 * synchronized 使安全化 但是性能下降
	 
	private static Singleton singleton;
	private Singleton(){};
	public static synchronized Singleton getInstance() {
		if (singleton==null) {
			singleton = new Singleton();
		}
		return singleton;
	}
	*/
	/*
	 * 懒汉式单例 双重锁
	 * synchronized 使安全化 使懒汉提高性能
	 */
		private static Singleton singleton;
		private Singleton(){};
		public static  Singleton getInstance() {
			if (singleton==null) {
				synchronized(Singleton.class){
					if (singleton==null) {
						singleton = new Singleton();
					}
				}
				
			}
			return singleton;
		}
	
	/*
	 * 使用Java虚拟机的机制进行同步保证
	 
	private static class SingletonHolder{
		public final static Singleton instance = new Singleton();
	}
	public static Singleton getInstance(){
		return SingletonHolder.instance;
	}*/
}
