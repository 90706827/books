package com.book.Threads;

public class threadStopDemo{
	
	public static void main(String[] args) {
		mains stopDemo = new mains();
		Thread t1 = new Thread(stopDemo);
		Thread t2 = new Thread(stopDemo);
		t1.setDaemon(true);//守护线程
		t2.setDaemon(true);//主线程已结束次线程同时结束
		t1.start();
		t2.start();
		int sum = 0;
		while(true){
			if(sum++ == 60){
//				t1.interrupt();//中断线程使用interrupt方法 在异常中false
//				t2.interrupt();
				break;
			}
			System.out.println("吃"+sum+"碗");
		}
		System.out.println("over");
	}
}class mains implements Runnable {
	
	private boolean falg = true;
	public synchronized void run() {
		while(falg){
//			try {
//				wait();//特殊情况 当线程冻结状态 线程就不会结束
//			} catch (Exception e) {
//				System.out.println("错误");
//				falg=false;//中断线程使用interrupt方法 在异常中false
//			}
			System.out.println(Thread.currentThread().getName()+"来碗面");
		}
	}
}
