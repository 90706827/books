package com.book.SystemDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunTimeDemo {
	public static void main(String[] args) {
		 excuteCommand("ipconfig");
		 excuteCommand("ping 10.1.74.1");
	}
	public static void  excuteCommand(String command){

		Runtime r = Runtime.getRuntime();
		Process p;
		try {

			p = r.exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String inline;
			while ((inline = br.readLine()) != null) {
				System.out.println(inline);

			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
