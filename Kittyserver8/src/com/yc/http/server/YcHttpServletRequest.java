package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpSession;

public class YcHttpServletRequest implements HttpServletRequest{
	private String method;
	private String protocal;
	private String ServerName;
	private String requestURI;//资源地址
	private String requestURL;
	private String contextPath;
	private int serverPort;
	private String realPath=System.getProperty("user.dir")+"\\webapps";
	
	private InputStream iis;
	private String queryString;
	
	public String getMethod() {
		return method;
	}


	public String getProtocal() {
		return protocal;
	}


	public String getServerName() {
		return ServerName;
	}


	public String getRequestURI() {
		return requestURI;
	}


	public String getRequestURL() {
		return requestURL;
	}


	public String getContextPath() {
		return contextPath;
	}


	public int getServerPort() {
		return serverPort;
	}


	public String getRealPath() {
		return realPath;
	}
	
	
	//所有头的信息
	private Map<String,String> headers=new HashMap<String,String>();
	private String sessionid;
	
	@Override
	public String getHeader(String name){
		String value=null;
		if( headers.containsKey(name) ){
			value=headers.get(name);
		}
		return value;
	}
	
	public String getSessionid(){
		return this.sessionid;
	}
	
	@Override
	public HttpSession getSession(){
		HttpSession session=null;
		//从headers中取出Cookie头
		//从Cookie头中取kittyesessionid Cookie:kittysessionid=xxx
		String cookievalue=headers.get("Cookie");
		if( cookievalue!=null && cookievalue.length()>0  ){
			String[] cookies=cookievalue.split("; ");
			for( String s:cookies ){
				String[] cookie=s.split("=");
				if( cookie[0].equals(YcConstants.SESSIONID)  ){
					this.sessionid=cookie[1];
					break;
				}
			}
		}
		//再从YcHttpSessionContext中根据sessionid取session 
		session=YcHttpSessionContext.getInstance().getSession( sessionid );
		//如果没有，则创建一session，同时指定他的id,创建时间YcHttpSessionContext中
		if( session==null ){
			session=new YcHttpSession();
			this.sessionid=session.getId();
			YcHttpSessionContext.getInstance().setSession(session.getId(), session);
		}
		//有，就取出来
		return session;
	}
	
	
	public void parseRequestInfoString (String requestInfoString ){
		StringTokenizer st=new StringTokenizer(requestInfoString);
		if( st.hasMoreTokens() ){
			this.method=st.nextToken();
			this.requestURI=st.nextToken();
			this.protocal=st.nextToken();
			this.contextPath="/"+this.requestURI.split("/")[1];
			
		}
		parseParameter( requestInfoString );
		parseHeader(requestInfoString);
			
		
		
	}
	
	private void parseHeader(String requestInfoString) {
		//取出\r\n\r\n前面的
		String head=requestInfoString.substring(0, requestInfoString.indexOf("\r\n\r\n"));
		String [] ss=head.split("\r\n");
		
		if( ss!=null&& ss.length>0 ){
			for(int i=1;i<ss.length;i++){
				//取出一行
				String [] strs=ss[i].split(": ");
				headers.put(strs[0], strs[1]);
			}
		}
		getSession();
	}


	public YcHttpServletRequest(InputStream iis) {
		//接收输入流
		this.iis=iis;
		parse();
	}


	public void parse() {
		
			String requestInfoString =readFromInputSteam();//请求头
			if( requestInfoString ==null|| "".equals(requestInfoString)){
				return;
			}
			//解析requestInfo字符串
			parseRequestInfoString (  requestInfoString  );
		
	}


/*	private void paresRequestInfoString(String requestInfoString) {
		//通过StringTokenizer类的方法取出头信息中的HTTP协议,在进行拼接
		StringTokenizer st=new StringTokenizer( requestInfoString );
		if(st.hasMoreTokens()){
			this.method=st.nextToken();
			this.requestURI=st.nextToken();
			this.protocal=st.nextToken();
			this.contextPath="/"+this.requestURI.split("/")[1];//contextPath应用上下路径
		}
		parseParameter(requestInfoString);
	}*/


	private void parseParameter(String protocal) {
		//1判断是否有 ？，有则要取? 前面当做erquestURI
		//以下解决了地址栏后面的参数解析问题
		if( requestURI.indexOf("?") >=0 ){//res/HelloServlet.do?name=a
			this.queryString=requestURI.substring(requestURI.indexOf("?")+1);
			this.requestURI=requestURI.substring(0,requestURI.indexOf("?"));
			//从queryString中解析出参数？name=a&pwd=a；
			String[] ss=this.queryString.split("&");
			if( ss!=null&& ss.length>0 ){
				for( String s:ss ){
					String[] paire=s.split("=");
					if( paire!=null&&paire.length>0 ){
						this.parameters.put(paire[0], paire[1]);
					}
				}
			}
			
		}
		
		
		if( this.method.equals("POST")){
			//
			String ps=protocal.substring(protocal.indexOf("\r\n\r\n")+4);
			if( ps!=null && ps.length()>0){
				String[] ss=ps.split("&");
				if( ss!=null&&ss.length>0 ){
					for( String s:ss){
						String[] paire=s.split("=");
						this.parameters.put(paire[0], paire[1]);
					}
				}
			}
		}
		
	}

	public String getQueryString(){
		return this.queryString;
	}

	private String readFromInputSteam()  {
		String protocal=null;
		StringBuffer sb=new StringBuffer(1024*10);
		byte[] bs=new byte[1024*10];
		int length=-1;
		try {
			length=this.iis.read(bs);
		} catch (IOException e) {
			e.printStackTrace();
			length=-1;
		}
		for(int j=0;j<length;j++){
			sb.append((char) bs[j]);
		}
		protocal=sb.toString();
		return protocal;
	}
	
	private Map<String, Object> attributes=new HashMap<String,Object>();
	
	
	public Object getAttribute(String key){
		return attributes.get(key);
	}
	
	
	public void setAttribute(String key,Object value){
		attributes.put(key,value);
	}
	
	
	private Map<String, String > parameters=new HashMap<String,String >();
	
	public String getParameter(String key){
		return parameters.get(key);
	}
	
	public Map<String ,String >getParameterMap(){
		return this.parameters;
	}


	@Override
	public ServletContext getServletContext() {
		return YcServletContext.getIntance();
	}


	


	






	

}
