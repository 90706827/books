package com.book.Threads;

/**
 * @author Administrator
 * 等待唤醒机制
 */
public class WaitNotifyDome {
	public static void main(String[] args) {
		Res res = new Res();
		
		new Thread(new input(res)).start();
		new Thread(new output(res)).start();
		
	}
}class Res{
	private String name;
	private String sex;
	private boolean falg = false;
	public synchronized void set(String name,String sex){
		if (falg) {
			try{this.wait();}catch(Exception e){}
		}
		this.name= name;
		this.sex = sex;
		falg =  true;
		this.notify();
	}
	public synchronized void out(){
		if(!falg){
			try{this.wait();}catch(Exception e){}
		}
		System.out.println(name+"-------"+sex);
		falg = false;
		this.notify();
	}
}class input implements Runnable{
	private Res res;
	input(Res r){
		this.res=r;
	}
	public void run(){
		int i=0;
		while(true){
			if (i==0) {
				res.set("红红", "女女");
			}else{
				res.set("绿绿", "男男");
			}
			i=(i+1)%2;
		}
	}
}class output implements Runnable{
	private Res res;
	output(Res r){
		this.res=r;
	}
	public void run(){
		while(true){
			res.out();
		}
	}
}
