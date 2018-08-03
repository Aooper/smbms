package cn.smbms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("拦截................");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String url = request.getRequestURI();
		System.out.println(url);
		
		if (null != request.getSession().getAttribute("userSession")){
			System.out.println("放行...........");
			return true;
		}
		else {
			System.out.println("未放行....");
			response.sendRedirect(request.getSession().getServletContext().getContextPath()+"/error.jsp");
			return false;
		}
	}
}
