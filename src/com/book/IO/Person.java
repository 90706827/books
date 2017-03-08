package com.book.IO;

import java.io.Serializable;

public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;//static 静态修饰不能被序列化
	private int age;    //transient 不能被序列化 堆内存中存在
	public Person(String name,int age) {
		this.name = name;
		this.age = age;
	}
	public String toString(){
		return name+age;
	}
}
