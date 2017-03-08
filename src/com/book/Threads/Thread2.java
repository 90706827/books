package com.book.Threads;

/**
 * @author Administrator
 * 继承和接口的区别  集成不能在继承 接口可以再继承
 * 同步代码块 解决安全问题
 * synchronized(对象){} 锁是对象
 * synchronized 修饰    锁是this
 * 静态中				   锁是类.Class
 * 同步前提：
 * 1.必须2个以上线程
 * 2.必须多个线程同用一个锁
 */
public class Thread2 implements Runnable {
	private int in = 600;
	Object object  = new Object();
	public void run() {
		while(true){
			show();
//			synchronized (object) {//每个线程排队进入同步代码块 只有一个线程执行完了下个个线程才能进入执行 private共用
//				if (in>0) {
//					try {
//						Thread.sleep(10);
//					} catch (InterruptedException e){ e.printStackTrace();}
//					System.out.println(Thread.currentThread().getName()+"||"+in--);
//				}
//			}
			
		}
	} 
	public synchronized void  show() {
		if (in>0) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e){ e.printStackTrace();}
			System.out.println(Thread.currentThread().getName()+"||"+in--);
		}
	}
	
	public static void main(String[] args) {
		Thread2 thread2 = new Thread2();
		new Thread(thread2).start();
		new Thread(thread2).start();
		new Thread(thread2).start();
		new Thread(thread2).start();
	}
}
