package com.yc.javax.servlet;

public interface Servlet {
/**
 * Servlet接口用来定义生命周期的回调方法
 */
	
	public void init();
	
	
	public void destory();
		
	
	
	public void service( ServletRequest request,ServletResponse response);
		
	
	
}
