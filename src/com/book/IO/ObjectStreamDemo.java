package com.book.IO;
import java.io.*;

/**
 * @author Administrator
 * 对象的序列化 读写  静态不能被序列化
 */
public class ObjectStreamDemo {

	public static void main(String[] args)throws Exception {
		writerObject();
		readerObject();
	}
	
	public static void writerObject()throws IOException{
		ObjectOutputStream oos = 
			new ObjectOutputStream(new FileOutputStream("obj.txt"));
		oos.writeObject(new Person("张三",20));
		oos.close();
	}

	public static void readerObject()throws Exception{
		ObjectInputStream ois =
			new ObjectInputStream(new FileInputStream("obj.txt"));
		Person person = (Person)ois.readObject();
		System.out.println(person.toString());
		ois.close();
	}
}
