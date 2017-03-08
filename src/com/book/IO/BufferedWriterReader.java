package com.book.IO;
import java.io.*;
/**
 * @author Administrator
 *字符流
 */
public class BufferedWriterReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br =  new BufferedReader(new FileReader("Reader.txt"));
			bw = new BufferedWriter(new FileWriter("Writer.txt"));
			String line = null;
			while((line=br.readLine())!=null){
				bw.write(line);//ReadLine()读取一行有效数据 不读取换行
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(br!=null)br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			try{
				if(bw!=null)bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		
	}

}
