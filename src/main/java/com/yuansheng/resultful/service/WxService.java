package com.yuansheng.resultful.service;

import com.alibaba.fastjson.JSONObject;
import com.yuansheng.resultful.domain.Oauth2Token;
import com.yuansheng.resultful.domain.SNSUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxService {
    //get请求微信授权登录
    String Wxget(String url);
    //获取code
    SNSUserInfo weixinLogin(HttpServletRequest request,HttpServletResponse response);
    //通过code获取网页授权凭证
    Oauth2Token getOauth2AccessToken(String appId, String appSecret, String code);
    //通过网页授权获取用户信息
    SNSUserInfo getSNSUserInfo(String accessToken, String openId);
    //验证微信接口正确性
    void Wxtoken(HttpServletRequest request, HttpServletResponse response);

}
