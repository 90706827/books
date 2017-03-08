package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 * 字符流文件写入
 */
public class FileWriterDemo {
	public static void main(String[] args){
		 FileWriter fw = null;
		try {
			//创建fileWriter对象 一旦创建必须有明确被操作的文件 
			//会被创建到指定的文件下 有同名会被覆盖 true为重新创建后继续写入
			fw = new FileWriter("demo.txt",true);
			//将字符串写入到流中（内存中）
			fw.write("你好\r\n");
			//刷新该流中的缓冲中的数据 将数据刷到目的地中
			fw.flush();
			//关闭流资源 关闭前会执行同Flush()一样
//			fw.write(new char[]{'1','2'});
//			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fw!=null)
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
