package com.book.Threads;

public class JoinDemo {
	public static void main(String[] args)throws Exception {
		joins joins = new joins();
		Thread t1 = new Thread(joins,"T1");
		Thread t2 = new Thread(joins,"T2");
		t1.start();
		t2.start();
//		t2.setPriority(Thread.MAX_PRIORITY);//设置优先级
//		t1.join(1);//t1 和 主线程 抢CPU分配优先级  join放到t2 后面 t1 和t2 交替  只有t1执行完 主线程在执行
		for (int i = 0; i < 60; i++) {
			System.out.println(Thread.currentThread().getName()+"---"+i);
		}
	}
}class joins implements Runnable{
	public void run(){
		for (int i = 0; i < 60; i++) {
			System.out.println(Thread.currentThread().getName()+"---"+i);
			Thread.yield();//几个线程交替执行
		}
	}
}
