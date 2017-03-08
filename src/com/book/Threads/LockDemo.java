package com.book.Threads;

import java.util.concurrent.locks.*;

/**
 * @author Administrator
 * 生产者 消费者
 * 多线程生产、多线程消费
 * 优化 Lock类和condition类的使用
 */
public class LockDemo {
	public static void main(String[] args) {
		Reso res = new Reso();
		
		new Thread(new pros(res)).start();
		new Thread(new pros(res)).start();
		new Thread(new conss(res)).start();
		new Thread(new conss(res)).start();
		
	}
}class Reso{
	private String name;
	private int count = 1;
	private boolean falg = false;
	Lock lock = new ReentrantLock();
	Condition condition_pro = lock.newCondition();
	Condition condition_con = lock.newCondition();
	public  void set(String name){
		lock.lock();
		try {
			while(falg) {//多生产者用while
				try{condition_pro.await();}catch(Exception e){}
			}
			this.name= name+"--"+count++;
			System.out.println(Thread.currentThread().getName()+"[生产者]"+this.name);
			falg =  true;
			condition_con.signalAll();
		} finally{
			lock.unlock();
		}
	}
	public void out(){
		lock.lock();
		try {
			while(!falg){//多消费者用while
				try{condition_con.await();}catch(Exception e){}
			}
			System.out.println(Thread.currentThread().getName()+"[消费者]--"+this.name);
			falg = false;
			condition_pro.signalAll();
		} finally{
			lock.unlock();
		}
		
	}
}class pro implements Runnable{
	private Resos res;
	pro(Resos r){
		this.res=r;
	}
	public void run(){
		while(true){
			res.set("花花");
		}
	}
}class cons implements Runnable{
	private Resos res;
	cons(Resos r){
		this.res=r;
	}
	public void run(){
		while(true){
			res.out();
		}
	}
}
