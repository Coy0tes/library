package com.jeeplus.modules.sys.web;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.sms.SMSUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.sys.dao.UserDao;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.Role;
import com.jeeplus.modules.sys.entity.SystemConfig;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.OfficeService;
import com.jeeplus.modules.sys.service.SystemConfigService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.utils.TwoDimensionCode;

/**
 * 用户Controller
 * @author jeeplus
 * @version 2013-8-29
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/register")
public class RegisterController extends BaseController {


	@Autowired
	private SystemConfigService systemConfigService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private UserDao userDao;
	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @param mobile
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "getRegisterCode")
	@ResponseBody
	public AjaxJson getRegisterCode(HttpServletRequest request,HttpServletResponse response, String mobile,
			Model model, RedirectAttributes redirectAttributes) {
		
		SystemConfig config = systemConfigService.get("1");
		
		AjaxJson j = new AjaxJson();
		
		//验证手机号是否已经注册
		if(userDao.findUniqueByProperty("mobile", mobile) != null){
			
				j.setSuccess(false);
				j.setErrorCode("1");
				j.setMsg("手机号已经被使用！");
		        return j;
		}
		
		String randomCode = String.valueOf((int) (Math.random() * 9000 + 1000));
		try {
			String result = UserUtils.sendRandomCode(config.getSmsName(),config.getSmsPassword(), mobile, randomCode);
			if (!result.equals("100")) {
				j.setSuccess(false);
				j.setErrorCode("2");
				j.setMsg("短信发送失败，错误代码："+result+"，请联系管理员。");
			}else{
				j.setSuccess(true);
				j.setErrorCode("-1");
				j.setMsg("短信发送成功!");
				request.getSession().getServletContext().setAttribute(mobile, randomCode);
			}
		} catch (IOException e) {
			j.setSuccess(false);
			j.setErrorCode("3");
			j.setMsg("因未知原因导致短信发送失败，请联系管理员。");
		}
		return j;
	}
	
	
	/**
	 * web端ajax验证手机验证码是否正确
	 */
	@ResponseBody
	@RequestMapping(value = "validateMobileCode")
	public boolean validateMobileCode(HttpServletRequest request,
			String mobile, String randomCode) {
		if (randomCode.equals(request.getSession().getServletContext().getAttribute(mobile))) {
			return true;
		} else {
			return false;
		}
	}

}
