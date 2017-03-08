package com.book.readLog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadLog {
	 public static void main(String[] args){
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			br =  new BufferedReader(new FileReader("D:\\nohup.out"));
			bw = new BufferedWriter(new FileWriter("D:\\lognohup.txt"));
			String line = "";
			long i =1;
			boolean lean = false;
			int ling = 0;
			while((line=br.readLine())!=null){
				i++;
				if(line.matches("(.*)(Exception)+(.*)")){
					lean=true;
					ling=20;
					System.out.println("true");
					bw.write("\n\t");//ReadLine()读取一行有效数据 不读取换行
					bw.newLine();
					bw.flush();
				}
				if(line.matches("(.*)(Exception|at )+(.*)")&&lean&&ling>0){
					ling--;
					bw.write("line="+i+":"+line);//ReadLine()读取一行有效数据 不读取换行
					bw.newLine();
					bw.flush();
				}
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
