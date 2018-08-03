package cn.smbms.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;

@Controller
public class LogOutController {
	@RequestMapping("/jsp/logout.do")
	public String loginOut(HttpServletRequest request){
		request.getSession().removeAttribute(Constants.USER_SESSION);
		return "redirect:../login.jsp";
	}
}
