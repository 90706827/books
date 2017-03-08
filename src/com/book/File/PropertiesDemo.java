package com.book.File;
import java.io.*;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.CountDownLatch;
/**
 * @author Administrator
 * Properties 是 hashtable的子类
 * 具有map集合的特点 键值对都是字符串
 * 是集合中和IO技术相结合的集合容器
 */
public class PropertiesDemo {

	public static void main(String[] args)throws IOException {
		porper_3();
	}
	public static void porper_3()throws IOException{
		Properties prt = new Properties();
		File file = new File("count.properties");
		if(!file.exists()){
			file.createNewFile();
		}
		FileInputStream fis = new FileInputStream(file);
		prt.load(fis);
		
		prt.setProperty("time", "1020");
		FileOutputStream fos = new FileOutputStream(file);
		prt.store(fos, "updateDate");
		fis.close();
		fos.close();
	}
	/**
	 * 加载一个配置文件 并改变保存到配置文件
	 */
	public static void proper_2()throws IOException{
		FileInputStream fis = new FileInputStream("user.properties");
		Properties po = new Properties();
		po.load(fis);//加载数据
		po.setProperty("wangwu", "40");
		FileOutputStream fos = new FileOutputStream("user.properties");
		po.store(fos, "haha");//写入信息
		System.out.println(po);
		po.list(System.out);
		fis.close();
		fos.close();
	}
	/**
	 * 读取文件信息
	 */
	public static void proper_1()throws IOException{
		BufferedReader bufReader = new BufferedReader(new FileReader("user.properties"));
		String lineString = null;
		Properties prp = new Properties();
		while((lineString=bufReader.readLine())!=null){
			String[] arrString = lineString.split("=");
			prp.setProperty(arrString[0], arrString[1]);
		}
		bufReader.close();
		System.out.println(prp);
	}
	/**
	 * 基本properties操作
	 */
	public static void proper() {
		Properties prop = new Properties();
		prop.setProperty("zhangsan", "23");
		prop.setProperty("wangwu", "45");
		String value = prop.getProperty("wangwu");
		System.out.println(value);
		Set<String> names = prop.stringPropertyNames();
		for(String a :names){
			System.out.println(a+"="+prop.getProperty(a));
		}
	}
}
