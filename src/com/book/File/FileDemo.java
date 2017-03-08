package com.book.File;

import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 *	文件基础方法
 */
public class FileDemo {
	
	public static void main(String[] args) throws IOException, Exception{
		File file = new File("D:\\abc", "args.txt");
		//创建
//		file.mkdirs();//创建多级目录
		file.createNewFile();//创建文件返回true 不创建返回false 已有文件不会重新创建覆盖
//		file.mkdir();//创建目录
		
		//删除
//		file.delete();//删除文件 成功返回true 失败返回false 如果写入或读取文件进行时 删除会失败
//		file.deleteOnExit();//程序在退出时删除指定文件 即使在操作的文件一样被删除
		//判断
		file.exists();//文件是否存在
		file.isDirectory();//是目录
		file.isFile();//是文件
		file.isHidden();//是否隐藏文件
		System.out.println(file.isAbsolute());//是否绝对路径 是true
		//获取
		file.getPath();//获取封装路径
		file.getAbsolutePath();//获取绝对路径
		file.getName();//返回文件名字
		file.getParent();//返回的是绝对路径中的父目录，如果获取的是相对路径 返回空
		file.lastModified();//返回文件最后一次被修改的时间
		file.length();//返回文件的长度
		File file1 = new File("D:\\abc","abc.txt");
		file.renameTo(file1);//把args.txt更换成abc.txt
		File file2 = new File("C:\\abc","DDD.txt");
		file1.renameTo(file2);//把abc.txt拷贝到C盘abc中更名为DDD.txt
	}
}
