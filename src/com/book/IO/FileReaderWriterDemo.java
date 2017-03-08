package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *字符流
 */
public class FileReaderWriterDemo {
	public static void main(String[] args){
		try {
			copy_2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void copy_2(){
		FileWriter fw = null;
		FileReader fr = null;
		try {
			fw = new FileWriter("Writer.txt");
			fr = new FileReader("Reader.txt");
			char[] car = new char[1024];
			while(true){
				int chr = fr.read(car);
				if(chr==-1)break;
				fw.write(car,0,chr);
			}
			
		} catch (IOException e) {
			throw new RuntimeException("读写失败");
		}finally{
			try {
				if(fr!=null)
				fr.close();
				if(fw!=null)
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public static void copy_1()throws IOException{
		FileWriter fw = new FileWriter("Writer.txt");
		FileReader fr = new FileReader("Reader.txt");
		int ch = 0;
		while(true){
			ch = fr.read();
			if(ch==-1)break;
			fw.write(ch);
		}
		fr.close();
		fw.close();
	}
}
