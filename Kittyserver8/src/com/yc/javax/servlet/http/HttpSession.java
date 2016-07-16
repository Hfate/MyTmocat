package com.yc.javax.servlet.http;

public interface HttpSession {
	public Object getAttribute(String name);
	
	public void setAttribute(String name,Object obj);
	
	public void removeAttribute(String name);
	
	public String getId();
	
	/**
	 * 	创建时间
	 */
	public long getCreationTime();
	
	
	/**
	 * 最后一次访问时间
	 */
	public long getLastAccessedTime();
	
}
