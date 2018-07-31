package com.yuansheng.resutful.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuansheng.resultful.service.WxService;
import com.yuansheng.resultful.util.CheckoutUtil;
import com.yuansheng.resultful.util.NetUtil;
import com.yuansheng.resultful.util.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yuansheng.resultful.core.common.JsonResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/users")
public class UserController {
	@Autowired
    private WxService wxService;

	/**
	 * 微信消息接收和token验证
	 */
	@RequestMapping(value = "/ownerCheck",method=RequestMethod.GET)
	public void ownerCheck(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		wxService.Wxtoken(request,response);
	}

	/**
	 * 微信跳转授权界面
	 */
	@RequestMapping(value = "/authorize",method=RequestMethod.GET)
	@ResponseBody
	public void authorize(HttpServletResponse resp)throws IOException{
		String appid="wxe817dbc86823b97b";
		String url=NetUtil.urlEncodeUTF8("http://lsying.j2eeall.com/users/weixinLogin");
		String urlNameString = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

		resp.sendRedirect(urlNameString);
	}

	/**
	 * 微信授权登录成功执行的业务。。。。。
	 */
	@RequestMapping("/weixinLogin")
	public ModelAndView weixinLogin(HttpServletRequest request,HttpServletResponse response){
		//wxService.weixinLogin(request,response);
		ModelAndView modelAndView=new ModelAndView("lsying");
		modelAndView.addObject("user",wxService.weixinLogin(request,response));
		return modelAndView;
	}

	/**
	 * 获取二维码
	 */
	@RequestMapping(value="/qrcode",method=RequestMethod.GET)
	public ModelAndView  getVCode(HttpServletResponse response) throws IOException {
		QRCode.createQRcode(response);
		return null;
	}


}
