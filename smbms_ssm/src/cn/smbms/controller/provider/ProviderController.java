package cn.smbms.controller.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;

@Controller
public class ProviderController {
	private HashMap<String, String> resultMap = new HashMap<String, String>();
	private List<Provider> providerList = new ArrayList<Provider>();
	@Resource
	private ProviderService providerService;
	
	@RequestMapping("/querypro")
	private String query(@RequestParam(required=false)String queryProName
			,@RequestParam(required=false)String queryProCode
			,Model model){
		if(StringUtils.isNullOrEmpty(queryProName)){
			queryProName = "";
		}
		if(StringUtils.isNullOrEmpty(queryProCode)){
			queryProCode = "";
		}
		providerList = providerService.getProviderList(queryProName,queryProCode);
		model.addAttribute("providerList", providerList);
		model.addAttribute("queryProName", queryProName);
		model.addAttribute("queryProCode", queryProCode);
		return "jsp/providerlist.jsp";
	}
	
	
	@RequestMapping("/delprovider")
	@ResponseBody
	private String delProvider(@RequestParam("proid")String id){
		if(!StringUtils.isNullOrEmpty(id)){
			int flag = providerService.deleteProviderById(id);
			System.out.println(flag);
			if(flag == 0){//删除成功
				resultMap.put("delResult", "true");
			}else if(flag == -1){//删除失败
				resultMap.put("delResult", "false");
			}else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
				resultMap.put("delResult", String.valueOf(flag));
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping("/getprobyid")
	private String getProviderById(@RequestParam("proid")String id,String url,Model model){
		Provider provider = null;
		if(!StringUtils.isNullOrEmpty(id)){
			provider = providerService.getProviderById(id);
			model.addAttribute("provider", provider);
		}
		System.out.println("id是...."+provider.getId());
		return "jsp/"+url;
	}
	@RequestMapping("midifypro")
	private String modify(Provider provider) {
		provider.setModifyDate(new Date());
		boolean flag = false;
		flag = providerService.modify(provider);
		System.out.println("修改"+flag);
		if(flag){
			return "redirect:querypro";
		}else{
			return "jsp/providermodify.jsp";
		}
	}
	@RequestMapping("/addpro")
	private String add(Provider provider,HttpSession session){
		provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		provider.setCreationDate(new Date());
		boolean flag = false;
		flag = providerService.add(provider);
		if(flag){
			return "redirect:querypro";
		}else{
			return "jsp/provideradd.jsp";
		}
	}
}
