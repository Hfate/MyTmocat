package com.yc.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import threadpool.Taskable;

public class TaskService implements Taskable {
	private Socket socket;
	private InputStream iis;
	private OutputStream oos;
	private boolean flag;
	
	public TaskService(Socket socket){
		this.socket=socket;
		//获取输入输出流
	
		try {
			this.iis=this.socket.getInputStream();
			this.oos=this.socket.getOutputStream();
			this.flag=true;
		} catch (IOException e) {
			YcConstants.logger.error("failed to get stream",e);
			flag=false;
			throw new RuntimeException(e);
		}
	}
		
	
	
	@Override
	public Object doTask() {
		//HTTP协议是一次请求和响应
		if(flag){
			//包装一个HttpServletRequest对象
			YcHttpServletRequest request;
			
			request = new YcHttpServletRequest(this.iis);
			
			//包装一个HttpServletResponse对象
			YcHttpServletResponse response=new YcHttpServletResponse(request,this.oos);
			//response.sendRedirect();
			//判断是静态资源还是动态资源
			Processor processor=null;
			if( request.getRequestURI().endsWith(".do") ){
				processor=new DynamicProcessor();
			}else{
				processor=new StaticProcessor();
			}
			processor.process(request,response);
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			YcConstants.logger.error("failed to close connertion to client ",e);
			flag=false;
		}
		return null;
	}

}
