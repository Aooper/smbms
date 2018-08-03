package cn.smbms.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;

@Controller
public class LoginController {
	@Resource
	private UserServiceImpl userService;
	@RequestMapping("/login.do")
	public String logindo(HttpServletRequest request,String userCode,String userPassword){
		User user=userService.login(userCode, userPassword);
		if(null!=user){
			request.getSession().setAttribute(Constants.USER_SESSION, user);
			return "jsp/frame.jsp";
		}else{
			request.setAttribute("error", "用户名或密码不正确");
			return "forward:login.jsp";
		}
	}

}
