package com.yucheng.im.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class ConnRemoteServer {

	public static String openRemoteServer(String userLoginKey,String userId,String urlStr) throws IOException{
		URL url = new URL(urlStr); 

		URLConnection connection = url.openConnection(); 

		connection.setDoOutput(true);

		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1"); 

		out.write("userLoginKey="+userLoginKey+"&userId="+userId); //这里组织提交信息 

		out.flush(); 

		out.close(); 

		//获取返回数据 
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); 

		String line = null; 

		StringBuffer content= new StringBuffer(); 

		while((line = in.readLine()) != null) 
		{ 
		   //line为返回值，这就可以判断是否成功、 
		    content.append(line); 
		}
		in.close() ;
		return content.toString();
	}
}
