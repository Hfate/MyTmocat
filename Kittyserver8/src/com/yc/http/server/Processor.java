package com.yc.http.server;

import com.yc.javax.servlet.ServletResponse;
import com.yc.javax.servlet.ServletRequest;

/**
 * 资源静态或动态处理器
 * @author Administrator
 *
 */
public interface Processor {
	
	/**
	 * 处理（动，静）的方法
	 * @param request请求对象：解析头信息，得到uri method pares parameter
	 * @param response响应对象，输出
	 */
	public void process( ServletRequest request,ServletResponse response  );

	
}
