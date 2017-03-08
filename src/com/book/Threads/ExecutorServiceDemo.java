package com.book.Threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceDemo {
	public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
	public static void mains(){
		ses.scheduleAtFixedRate(new Runnable(){
			public void run(){
				System.out.println("a");
			}
		}, 0, 60,TimeUnit.SECONDS);
		}
	public static void main(String[] args) {
		mains();
	}
}
