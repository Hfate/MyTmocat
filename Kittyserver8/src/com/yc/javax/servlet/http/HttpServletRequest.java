package com.yc.javax.servlet.http;

import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.ServletRequest;


public interface HttpServletRequest extends ServletRequest {
	/**
	 * 请求的方法
	 */
	public String getMethod();


	public String getRequestURI();
	
	public ServletContext getServletContext();
	
	public HttpSession getSession();
	
	public String getHeader(String name);
}
