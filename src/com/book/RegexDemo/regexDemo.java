package com.book.RegexDemo;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeSet;
import java.util.regex.*;

public class regexDemo {

	/**
	 * JAVA正则表达
	 * @throws Exception 
	 * @jangni
	 */
	public static void main(String[] args0) throws Exception{
		htmlChongChong();//网页爬虫
		//普通正常表达
		checkQQ("a234567890123456","[a-zA-Z][0-9]{4,15}");//基本java正则表达
		checkQQ("","[abc]*");//?不出现或出现一次 *零次或多次 + 一次或多次 {n}恰好n次 {n，}至少n次 {n，m} 至少n，不超过m
		checkQQ("d","[^abc]");//除了abc任何字符
		checkQQ("123456","\\d{6}");// . 任何字符 \d数字 \D非数字  \s空白字符 \S非空白字符 \w单子字符 \W 非单词字符
		checkQQ("13800001111","1[358]\\d{9}");
		//正则切割
		split("a,b,c",",");
		split("a,,b,,,c",",+");
		split("c:\\abc\\t.txt","\\\\");//转义字符
	    split("akkbcctddg","(.)\\1");//叠词切割 1代表第一组
	    split("akkkkkkkbccctdddg","(.)\\1+");//叠词切割
	    //正则替换
	    replaceAll("www888888sss55555555name6666666","\\d{5,}","#");
	    replaceAll("akkkkkkkbccctdddg","(.)\\1+","&");
	    replaceAll("akkkkkkkbccctdddg","(.)\\1+","$1");//$代表组的字符 1代表第几个组的
	    //正则获取
	    pattern("woa ai nia","\\b[a-z]{2}\\b");
	    //练习一
	    String str ="我我...我我...要要.要学..学学...学编编...编成成...成..".replaceAll("\\.", "");
	    str = str.replaceAll("(.)\\1+", "$1");
	    System.out.println(str);
	    //练习二
	    String ip ="192.168.2.5 102.5.6.5 10.1.25.8 2.2.2.2 1.2.52.2";
	    ip = ip.replaceAll("(\\d+)","00$1").replaceAll("0*(\\d{3})","$1");
	    System.out.println(ip);
	    String[] arr = ip.split(" ");
	    TreeSet<String> ts = new TreeSet<String>();
	    for(String s:arr){
	    	ts.add(s);
	    }
	    for(String s : ts){
	    	System.out.println(s.replaceAll("0*(\\d+)","$1"));
	    }
	    //练习三
	    System.out.println("abc123@sina.com".matches("[a-zA-Z][a-zA-Z0-9_]{5,12}@[a-zA-Z0-9]+\\.[a-zA-Z]+"));
	}
	public static void pattern(String str,String regex){
		Pattern pat = Pattern.compile(regex);//将规则封装成对象
		Matcher mat = pat.matcher(str);//关联获取匹配器对象
		while(mat.find()){//找了后才能取group
			System.out.println(mat.start()+"---"+mat.end());
			System.out.println(mat.group());
		}
	}
	/**
	 * @param str
	 * @param regex
	 * @param newStr
	 * 正则替换
	 */
	public static void replaceAll(String str,String regex,String newStr){
		str = str.replaceAll(regex, newStr);
		System.out.println(str);
	}
	/**
	 * @param str
	 * @param regex
	 * 正则切割
	 */
	public static void split(String str,String regex){
		String[] arr = str.split(regex);
		for(String s :arr){
			System.out.println(s);
		}
	}
	/**
	 * @param QQ
	 * @param regex
	 * 基本正则表达
	 */
	public static void checkQQ(String QQ,String regex){
		boolean b = QQ.matches(regex);
		System.out.println(b);
	}
	public static void htmlChongChong() throws Exception{
		URL url = new URL("http://192.168.1.254:8080/web/abc.html");
		URLConnection conn = url.openConnection();
		BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		BufferedReader buff = new BufferedReader(new FileReader("mail.txt"));
		Pattern p = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
		String line = null;
		while((line=buff.readLine())!=null){
			Matcher m = p.matcher(line);
			while(m.find()){
				System.out.println(m.group());
			}
		}
	}
}
