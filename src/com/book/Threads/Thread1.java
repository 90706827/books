package com.book.Threads;

/**
 * @author Administrator
 * sleep(time);暂停毫秒 时间到继续执行
 * wait();暂停 notify();唤醒
 * stop();结束
 * Thread.currentThread()获取当前线程对象
 * getName（） 获取线程的名称
 * 设置线程名称 setName 活着构造函数
 */
public class Thread1 extends Thread{
	private int a=100;
	public void run(){
		while(true){
			if (a>0) {
				System.out.println(Thread.currentThread().getName()+"--"+a--);

			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	 Thread1 thread1  = new Thread1();
	 thread1.start();
	}
	

}
