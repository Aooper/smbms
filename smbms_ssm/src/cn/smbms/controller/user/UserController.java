package cn.smbms.controller.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;

@Controller
public class UserController {
	@Resource
	private UserServiceImpl userService;
	@Resource
	private RoleService roleService;
	Map<String, String> resultMap=new HashMap<String, String>();
	
	@RequestMapping("/updatePwd")
	public String updatePwd(HttpServletRequest request,String newpassword){
		Object object=request.getSession().getAttribute(Constants.USER_SESSION);
		boolean flag = false;
		if(object != null && !StringUtils.isNullOrEmpty(newpassword)){
			flag = userService.updatePwd(((User)object).getId(),newpassword);
			if(flag){
				request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
				request.getSession().removeAttribute(Constants.USER_SESSION);//session注销
			}else{
				request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
			}
		}else{
			request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
		}
		return "jsp/pwdmodify.jsp";
	}
	@RequestMapping(value="/getPwdByUserId")
	@ResponseBody
	public String getPwdByUserId(HttpSession session,String oldpassword) throws IOException{
		Object o=session.getAttribute(Constants.USER_SESSION);
		if(null == o ){//session过期
			resultMap.put("result", "sessionerror");
		}else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
			resultMap.put("result", "error");
		}else{
			String sessionPwd = ((User)o).getUserPassword();
			if(oldpassword.equals(sessionPwd)){
				resultMap.put("result", "true");
			}else{//旧密码输入不正确
				resultMap.put("result", "false");
			}
		}
		
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping("/queryuser")
	private String query(@RequestParam(value="queryname",required=false)String queryUserName
			,@RequestParam(value="queryUserRole",required=false)String temp
			,String pageIndex,Model model) throws IOException{
		//查询用户列表
		int queryUserRole = 0;
		List<User> userList = null;
		//设置页面容量
    	int pageSize = Constants.pageSize;
    	//当前页码
    	int currentPageNo = 1;
		/**
		 * http://localhost:8090/SMBMS/userlist.do
		 * ----queryUserName --NULL
		 * http://localhost:8090/SMBMS/userlist.do?queryname=
		 * --queryUserName ---""
		 */
		System.out.println("queryUserName servlet--------"+queryUserName);  
		System.out.println("queryUserRole servlet--------"+queryUserRole);  
		System.out.println("query pageIndex--------- > " + pageIndex);
		if(queryUserName == null){
			queryUserName = "";
		}
		if(temp != null && !temp.equals("")){
			queryUserRole = Integer.parseInt(temp);
		}
		
    	if(pageIndex != null){
    		try{
    			currentPageNo = Integer.valueOf(pageIndex);
    		}catch(NumberFormatException e){
    			return "error.jsp";
    		}
    	}	
    	//总数量（表）	
    	int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
    	//总页数
    	PageSupport pages=new PageSupport();
    	pages.setCurrentPageNo(currentPageNo);
    	pages.setPageSize(pageSize);
    	pages.setTotalCount(totalCount);
    	
    	int totalPageCount = pages.getTotalPageCount();
    	
    	//控制首页和尾页
    	if(currentPageNo < 1){
    		currentPageNo = 1;
    	}else if(currentPageNo > totalPageCount){
    		currentPageNo = totalPageCount;
    	}
		
		
		userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
		model.addAttribute("userList", userList);
		List<Role> roleList = null;
		roleList = roleService.getRoleList();
		model.addAttribute("roleList", roleList);
		model.addAttribute("queryUserName", queryUserName);
		model.addAttribute("queryUserRole", queryUserRole);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("currentPageNo", currentPageNo);
		return "forward:jsp/userlist.jsp";
	}
	
	@RequestMapping("/deleteuser")
	@ResponseBody
	private String delUser(@RequestParam("uid")Integer delId,HttpServletResponse response)
			throws ServletException, IOException {
		
		if(delId <= 0){
			resultMap.put("delResult", "notexist");
		}else{
			if(userService.deleteUserById(delId)){
				resultMap.put("delResult", "true");
			}else{
				resultMap.put("delResult", "false");
			}
		}
		
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping("/getrolelist")
	@ResponseBody
	private String getRoleList(){
		List<Role> roleList = roleService.getRoleList();
		//把roleList转换成json对象输出
		return JSONArray.toJSONString(roleList);
	}
	
	
	@RequestMapping("/getuserbyid")
	private String getUserById(@RequestParam("uid")String id,Model model,String url)
			throws ServletException, IOException {
		if(!StringUtils.isNullOrEmpty(id)){
			//调用后台方法得到user对象
			User user = userService.getUserById(id);
			model.addAttribute("user", user);
		}
		return "forward:jsp/"+url;
	}
	
	@RequestMapping("/usercodeexist")
	@ResponseBody
	private String userCodeExist(String userCode)
			throws ServletException, IOException {
		//判断用户账号是否可用
		
		if(StringUtils.isNullOrEmpty(userCode)){
			//userCode == null || userCode.equals("")
			resultMap.put("userCode", "exist");
		}else{
			User user = userService.selectUserCodeExist(userCode);
			if(null != user){
				resultMap.put("userCode","exist");
			}else{
				resultMap.put("userCode", "notexist");
			}
		}
		
		//把resultMap转为json字符串以json的形式输出
		//配置上下文的输出类型
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping("/usermodify")
	public String modify(User user,HttpSession session){
		user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		user.setModifyDate(new Date());
		System.out.println("修改请求........");
		if(userService.modify(user)){
			return "redirect:queryuser";
		}else{
			return "jsp/usermodify.jsp";
		}
	}
	@RequestMapping("/adduser")
	private String add(User user,HttpSession session){
		user.setCreationDate(new Date());
		user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		System.out.println("添加请求....");
		if(userService.add(user)){
			return "redirect:queryuser";
		}else{
			return "forward:jsp/useradd.jsp";
		}
	}
}
