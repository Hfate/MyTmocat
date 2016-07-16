import com.yc.javax.servlet.http.Cookie;
import com.yc.javax.servlet.http.HttpServlet;
import com.yc.javax.servlet.http.HttpServletRequest;
import com.yc.javax.servlet.http.HttpServletResponse;
import com.yc.javax.servlet.http.JspWriter;


public class HelloServlet2 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  {
		
		doPost(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)  {
		Cookie c=new Cookie("name","zy");
		Cookie c2=new Cookie("pwd","a");
		Cookie c3=new Cookie("age","12");
		response.addCookie(c);
		response.addCookie(c2);
		response.addCookie(c3);//有一个集合存cookie
		
		JspWriter pw=response.getJspWriter();
		pw.println("<html><body><h1>hello</h1></body></html>");
	}
		
}
