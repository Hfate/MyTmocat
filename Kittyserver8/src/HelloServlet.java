import java.io.IOException;







import java.io.PrintWriter;

import com.yc.javax.servlet.Servlet;
import com.yc.javax.servlet.ServletContext;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;


public class HelloServlet extends HttpServlet {
	 
    public HelloServlet() {
        super();
        System.out.println("HelloServlet的构造方法");
    }

	public void init()  {
		
		super.init();
		System.out.println("init的方法");
	}

	public void service(HttpServletRequest request, HttpServletResponse response)  {
		System.out.println("service被调用了");
		super.service(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
		System.out.println("doGet()被调用了");
		doPost(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		
		String name=request.getParameter("name");
		String pwd=request.getParameter("pwd");
		Integer age=Integer.parseInt(request.getParameter("age"));
		
		
		System.out.println("dopost被调用了");
		System.out.println("计数器开始运行了");
		
		ServletContext application =request.getServletContext();
		Integer count=new Integer(0);
		if( application.getAttribute("count")!= null ){
			count =(Integer) application.getAttribute("count");
		}
		count++;
		application.setAttribute("count", count);
		System.out.println("访问次数"+count);
		
		String html="<html><head></head><body><hr />visited count:"+count+"</body></html>";
		
		PrintWriter out=response.getWriter();
		String protocal200="HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: "+html.getBytes().length+"\r\n\r\n";
		out.println(protocal200);
		out.println(html);
		out.flush();
	
	}
	
	
	
}
