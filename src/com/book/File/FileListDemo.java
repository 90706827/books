package com.book.File;
import java.io.*;
import java.util.*;
/**
 * @author Administrator
 *	文件列表
 */
public class FileListDemo {

	public static void main(String[] args) {
//		FileFilterDemo();
//		File file = new File("D:\\Javas");
//		showDir(file);
//		removeDir(file); //删除小心操作 删除后不能找回
		FileListWriter();
	}
	public static void FileListWriter(){
		File dir = new File("D:\\mp3");
		List<File> list = new ArrayList<File>();
		FileToList(dir,list);
		File file = new File("D:\\mp3.txt");
		writerToFile(list,file);
	}
	public static void FileToList(File dir,List<File> list){
		File[] files = dir.listFiles();
		for(File file : files){
			if(file.isDirectory()){
				FileToList(file,list);
			}else{
				if(file.getName().endsWith(".mp3"));
					list.add(file);
			}
		}
	}
	public static void writerToFile(List<File> list,File file){
		BufferedWriter bufw =null;
		try{
			bufw = new BufferedWriter(new FileWriter(file));
			for(File f : list){
				bufw.write(f.getAbsolutePath());
				bufw.newLine();
				bufw.flush();
			}
			
		}catch(IOException e){
			
		}finally{
			try {
				if(bufw!=null)
				bufw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 删除目录下的所有文件
	 */
	public static void removeDir(File dir){
		File[] files = dir.listFiles();
		for(int i = 0; i<files.length;i++){
			if(files[i].isDirectory()){
				removeDir(files[i]);
			}else{
				System.out.println(files[i].delete());
			}
		}
		dir.delete();
	}
	/**
	 * 递归输出文件夹下所有子文件夹下的所有文件
	 */
	public static void showDir(File dir){//获取目录下所有文件夹下的所有文件 使用递归
		File[] files = dir.listFiles();
		for(int i = 0; i<files.length;i++){
			if(files[i].isDirectory()){
				showDir(files[i]);
			}else{
				System.out.println(files[i]);
			}
		}
	}
	/**
	 * 获取盘符下所有后缀名.wma的文件名称
	 */
	public static void FileFilterDemo(){
		File f = new File("D:\\mp3");
		
		String[] strs = f.list(new FilenameFilter(){
			public boolean accept(File f,String name){
				return name.endsWith(".wma");//获取文件下。wma文件
			}
		});//获取盘符下所有的文件名称 该目录必须存在 否则空指针
		for(String str : strs){
			System.out.println(str);
		}
	}
	/**
	 * 获取盘符下所有的文件名称
	 */
	public static void ListDemo(){
		File f = new File("c:\\");
		String[] strs = f.list();//获取盘符下所有的文件名称 该目录必须存在 否则空指针
		for(String str : strs){
			System.out.println(str);
		}
	}
	public static void listRootsDemo(){
		File[] files = File.listRoots();//获取系统所有盘符
		for(File f : files){
			System.out.println(f);
		}
	}
}
