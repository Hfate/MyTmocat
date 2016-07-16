package com.yc.http.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class KittyClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s=new Socket("192.168.3.29",10001);//192.168.3.29  localhost
		System.out.println("客户端连接上服务器了");
		
	}
	
}

