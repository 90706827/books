package com.book.Threads;

/**
 * @author Administrator
 * JDK 1.5升级
 * lock 代替 synchronized
 */
public class WaitNotityDemo {
	public static void main(String[] args) {
		Resos res = new Resos();
		 
		new Thread(new pro(res)).start();
		new Thread(new pro(res)).start();
		new Thread(new cons(res)).start();
		new Thread(new cons(res)).start();
		
	}
}class Resos{
	private String name;
	private int count = 1;
	private boolean falg = false;
	public synchronized void set(String name){
		while(falg) {//多生产者用while
			try{this.wait();}catch(Exception e){}
		}
		this.name= name+"--"+count++;
		System.out.println(Thread.currentThread().getName()+"[生产者]"+this.name);
		falg =  true;
		this.notifyAll();//多生产者用唤醒所有
	}
	public synchronized void out(){
		while(!falg){//多消费者用while
			try{this.wait();}catch(Exception e){}
		}
		System.out.println(Thread.currentThread().getName()+"[消费者]--"+this.name);
		falg = false;
		this.notifyAll();//多消费者用唤醒所有
	}
}class pros implements Runnable{
	private Reso res;
	pros(Reso r){
		this.res=r;
	}
	public void run(){
		while(true){
			res.set("花花");
		}
	}
}class conss implements Runnable{
	private Reso res;
	conss(Reso r){
		this.res=r;
	}
	public void run(){
		while(true){
			res.out();
		}
	}
}
