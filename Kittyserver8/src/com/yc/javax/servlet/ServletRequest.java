package com.yc.javax.servlet;

import java.util.Map;

public interface ServletRequest {
	public String getRealPath();
	
	
	public Object getAttribute( String key );
	
	
	public void setAttribute( String key, Object value );
	
	
	/**
	 * 获取 通过 http:// localhost:8080/xx/xx.jsp?name=a&age=3
	 */
	public String getParameter( String key );
	
	
	public Map<String ,String >getParameterMap();
	
	
	/**
	 * 解析请求： 1解析出uri 2解析出参数 name .age 3.解析出请求方式get/post/head
	 */
	public void parse();
	
	
	public String getServerName();
	
	
	public int getServerPort();
	
}
