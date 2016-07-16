package com.yc.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import threadpool.ThreadPoolManager;

public class KittyServer {
	private ThreadPoolManager tpm;
	
	public static void main(String[] args) throws IOException  {
		KittyServer ks=new KittyServer();
		ks.startServer();
		
	}	
		private void startServer(){
			ServerSocket ss=null;
			int port=parseServerXml();
			tpm=new ThreadPoolManager(10,null);
			try{
				ss= new ServerSocket(port);
			    YcConstants.logger.debug("KittyServer is starting and listening"+ss.getLocalPort());
			
			
			}catch(Exception e){
				e.printStackTrace();
				YcConstants.logger.error("端口"+port+"已被占用");
				return;
			}
			
			while(true){
				try {
					Socket s=ss.accept();
					YcConstants.logger.debug("a client "+s.getInetAddress()+"is contenting ");
					
					TaskService ts=new TaskService(s);
					tpm.process(ts);
					//Thread t=new Thread(ts);
					//t.start();

				} catch (IOException e) {
					
					YcConstants.logger.error("Clinet is down"+e.getMessage());
				}
			}
			
		}
		private int parseServerXml() {
			List<Integer> list =new ArrayList<Integer>();
			//通过DocumentBuilderFactory创建XML解析器
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			try {
				//通过解析器创建一个可以加载并生成XML的DocumentBuilder
				DocumentBuilder builder=factory.newDocumentBuilder();
				//通过DocumentBuilder加载并生成一颗xml树，Document对象的实例
				Document doc=builder.parse(YcConstants.SERVERCONFIG);
				//通过Document可以遍历这棵树，并读取相应节点中的内容
				NodeList nl=doc.getElementsByTagName("Connector");
				for(int i=0;i< nl.getLength();i++){
					Element node=(Element) nl.item(i);
					list.add(Integer.parseInt(node.getAttribute("port")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			return list.get(0);
		}
	 
}		
