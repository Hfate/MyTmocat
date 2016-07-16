package com.yc.http.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.yc.javax.servlet.http.Cookie;
import com.yc.javax.servlet.http.HttpServletResponse;
import com.yc.javax.servlet.http.JspWriter;

public  class YcHttpServletResponse implements HttpServletResponse{
	private OutputStream oos;
	private YcHttpServletRequest request;
	private String contentType;
	private int cookieIndex=0;
	private Cookie[] cookies=new Cookie[50];
	
	public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos) {
		this.request=request;
		this.oos=oos;
		
		Cookie c=new Cookie( YcConstants.SESSIONID,request.getSessionid() );
		cookies[cookieIndex]=c;
		cookieIndex++;
	}

	@Override
	public void addCookie(Cookie cookie) {
		if( cookieIndex>=cookies.length ){
			return;
		}
		cookies[cookieIndex]=cookie;
		cookieIndex++;
	}

	

	@Override
	public JspWriter getJspWriter() {
		JspWriter jspWriter=new JspWriter(this.oos,this);
		
		return jspWriter;
	}

	@Override
	public Cookie[] getCookies() {
		Cookie[] cs=new Cookie[cookieIndex];
		for(int i=0;i<cookieIndex;i++){
			cs[i]=cookies[i];
		}
		return cs;
	}
	
	public void sendRedirect() {
		
			//从request中取出url，  /res/index.html
			String url=request.getRequestURI();
			File f=new File(request.getRealPath(), url);
			
			String responseprotocal =null;  //响应头协议
			byte[] fileContent=null;      //响应的内容
			
			if( !f.exists() ){
				fileContent =readFile(new File(request.getRealPath(),request.getContextPath()+"/404.html"));
				responseprotocal=gen404(fileContent.length);
			}else{
				fileContent=readFile(f);
				responseprotocal=gen200(fileContent.length);
			}
			try {
				oos.write(responseprotocal.getBytes());//写协议
				oos.flush();
				oos.write(fileContent);//写出文件
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if( oos!=null ){
					try {
						oos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	/**
	 * 要考虑文件的类型
	 * @param bodylength  内容的长度
	 * @return
	 */
	private String gen200(long bodylength) {
		String uri=this.request.getRequestURI();//取出要访问的文件的地址
		int index=uri.lastIndexOf(".");
		if(index>=0){
			index=index+1;
		}
		String fileExtension =uri.substring(index);//文件的后缀名
		String protocal200="";
		if("JPG".equalsIgnoreCase(fileExtension) || "JPEG".equalsIgnoreCase(fileExtension)){
			contentType="image/JPEG";
			protocal200="HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "+bodylength+"\r\n\r\n";
		}else if("PNG".equalsIgnoreCase(fileExtension)){
			contentType="image/PNG";
			protocal200="HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "+bodylength+"\r\n\r\n";
		}else if("json".equalsIgnoreCase(fileExtension)){
			contentType="appliactiom/json";
			protocal200="HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "+bodylength+"\r\n\r\n";
		}else{
			contentType="text/html";
			protocal200="HTTP/1.1 200 OK\r\nContent-Type: "+contentType+"\r\nContent-Length: "+bodylength+"\r\n\r\n";
		}
		
		return protocal200;
	}

	private String gen404(long bodylength) {
		String protocal404="HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: "+bodylength+"\r\n\r\n";
		
		return protocal404;
	}

	private byte[] readFile(File f) {
		FileInputStream fis=null;
		ByteArrayOutputStream boas=new ByteArrayOutputStream();//字节输出流（将字节数组数据保存到内存）
		//读取文件
		try {
			fis=new FileInputStream(f);
			byte[] bs=new byte[1024];
			int length=-1;
			while( (length=fis.read(bs,0,bs.length))!=-1  ){
				boas.write(bs, 0, length   );//写到缓存内存
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( fis!=null ){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return boas.toByteArray();//一次性从内存中读取所有的字节数组返回
	}
	
	
	public PrintWriter getWriter(){
		PrintWriter pw=new PrintWriter( this.oos );
		return pw;
	}
	
	public String getContentType(){
		return this.contentType;
	}

	
	
	
	
}