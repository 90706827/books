package com.book.Threads;


public class DeadLockDemo implements Runnable {
	private boolean falg;
	DeadLockDemo(boolean falg){
		this.falg = falg;
	}
	public void run(){
		if(falg){
			synchronized (myLock.boj1) {
				System.out.println("myLock.boj1");
				synchronized (myLock.boj2) {
					System.out.println("myLock.boj2");
				}
			}
		}else{
			synchronized (myLock.boj2) {
				System.out.println("else myLock.boj1");
				synchronized (myLock.boj1) {
					System.out.println("else myLock.boj2");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread( new DeadLockDemo(true));
		Thread t2 = new Thread( new DeadLockDemo(false));
		t1.start();
		t2.start();
		
	}
}class myLock{
	static Object boj1 = new Object();
	static Object boj2 = new Object();
}
