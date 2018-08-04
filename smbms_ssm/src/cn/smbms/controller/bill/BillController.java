package cn.smbms.controller.bill;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;

@Controller
public class BillController {
	@Resource
	private ProviderService providerService;
	@Resource
	private BillService billService;
	private List<Provider> providerList = new ArrayList<Provider>();
	private Map<String, String> resultMap=new HashMap<String, String>();
	private List<Bill> billList=new ArrayList<Bill>();
	
	
	@RequestMapping("/jsp/bill.do")
	public String billdo(String method){
		/*String totalPrice = request.getParameter("totalPrice");
		//23.234   45
		BigDecimal totalPriceBigDecimal = 
				//设置规则，小数点保留两位，多出部分，ROUND_DOWN 舍弃
				//ROUND_HALF_UP 四舍五入(5入) ROUND_UP 进位 
				//ROUND_HALF_DOWN 四舍五入（5不入）
				new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN);*/
		if(method != null && method.equals("query")){
			return "forward:../querybill";
		}else if(method != null && method.equals("add")){
			return "forward:../addbill";
		}else if(method != null && method.equals("view")){
			return "forward:../getBillById?url=billview.jsp";
		}else if(method != null && method.equals("modify")){
			return "forward:../getBillById?url=billmodify.jsp";
		}else if(method != null && method.equals("modifysave")){
			return "forward:../modifybill";
		}else if(method != null && method.equals("delbill")){
			return "forward:../delbill";
		}else if(method != null && method.equals("getproviderlist")){
			return "forward:../getProviderlist";
		}else
			return "error.jsp";
	}
	
	@RequestMapping("/getProviderlist")
	@ResponseBody
	private String getProviderlist() {
		providerList = providerService.getProviderList("","");
		//把providerList转换成json对象输出
		return JSONArray.toJSONString(providerList);
	}
	
	@RequestMapping("/getBillById")
	private String getBillById(String url,@RequestParam("billid")String id,Model model){
		if(!StringUtils.isNullOrEmpty(id)){
			Bill bill = null;
			bill = billService.getBillById(id);
			model.addAttribute("bill", bill);
		}
		return "jsp/"+url;
	}
	
	@RequestMapping("modifybill")
	private String modify(Bill bill,HttpSession session){
		bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setModifyDate(new Date());
		boolean flag = false;
		flag = billService.modify(bill);
		if(flag){
			return "redirect:/querybill";
		}else{
			return "jsp/billmodify.jsp";
		}
	}
	@RequestMapping("delbill")
	@ResponseBody
	private String delBill(@RequestParam("billid")String id){
			if(!StringUtils.isNullOrEmpty(id)){
			boolean flag = billService.deleteBillById(id);
			if(flag){//删除成功
				resultMap.put("delResult", "true");
			}else{//删除失败
				resultMap.put("delResult", "false");
			}
		}else{
			resultMap.put("delResult", "notexit");
		}
		//把resultMap转换成json对象输出
		return JSONArray.toJSONString(resultMap);
	}
	@RequestMapping("addbill")
	private String add(Bill bill,HttpSession session){
		bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
		bill.setCreationDate(new Date());
		boolean flag = false;
		flag = billService.add(bill);
		if(flag){
			return "redirect:/querybill";
		}else{
			return "jsp/billadd.jsp";
		}
		
	}
	
	@RequestMapping("querybill")
	private String query(Bill bill,Model model
			,@RequestParam(required=false)String queryProductName
			,@RequestParam(required=false)String queryProviderId
			,@RequestParam(required=false)String queryIsPayment){
		providerList = providerService.getProviderList("","");
		model.addAttribute("providerList", providerList);
		
		if(StringUtils.isNullOrEmpty(queryProductName)){
			queryProductName = "";
		}
		bill.setProductName(queryProductName);
		
		if(StringUtils.isNullOrEmpty(queryIsPayment)){
			bill.setIsPayment(0);
		}else{
			bill.setIsPayment(Integer.parseInt(queryIsPayment));
		}
		
		if(StringUtils.isNullOrEmpty(queryProviderId)){
			bill.setProviderId(0);
		}else{
			bill.setProviderId(Integer.parseInt(queryProviderId));
		}
		
		billList = billService.getBillList(bill);
		model.addAttribute("billList", billList);
		model.addAttribute("queryProductName", queryProductName);
		model.addAttribute("queryProviderId", queryProviderId);
		model.addAttribute("queryIsPayment", queryIsPayment);
		return "jsp/billlist.jsp";
		
	}
}
