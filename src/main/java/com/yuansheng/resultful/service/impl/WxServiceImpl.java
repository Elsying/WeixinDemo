package com.yuansheng.resultful.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuansheng.resultful.domain.Oauth2Token;
import com.yuansheng.resultful.domain.SNSUserInfo;
import com.yuansheng.resultful.service.WxService;
import com.yuansheng.resultful.util.CheckoutUtil;
import com.yuansheng.resultful.util.NetUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@Service
public class WxServiceImpl implements WxService {

    /**
     //get请求微信授权登录
     */
    @Override
    public String Wxget(String url) {
        /**
        这里用HttpClient的get请求微信接口会判断为不是在微信客户端打开，（查找是js中判断，目前没找到解决办法），所以最后
         使用重定向到微信url的方法
         */
        String appID="wxe817dbc86823b97b";
        String urls=NetUtil.urlEncodeUTF8(url);
        out.println("-----------------------url:"+urls);
        String WxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appID+"&redirect_uri="+urls+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

        return NetUtil.get(WxUrl);

    }

    /**
    //授权登录成功 根据code获取access_token和用户信息
     */
    @Override
    public SNSUserInfo weixinLogin(HttpServletRequest request, HttpServletResponse response) {
        // 用户同意授权后，能获取到code
        Map<String, String[]> params = request.getParameterMap();//针对get获取get参数
        SNSUserInfo snsUserInfo = null;
        String[] codes = params.get("code");//拿到code的值
        String code = codes[0];//code
        out.println("****************code:" + code);
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            Oauth2Token oauth2Token = getOauth2AccessToken("wxe817dbc86823b97b", "dcffc0cfcec9dfac184540cc17c133e2", code);
            out.println("***********************************oauth2Token信息："+oauth2Token.toString());
            // 网页授权接口访问凭证
            String accessToken = oauth2Token.getAccessToken();
            // 用户标识
            String openId = oauth2Token.getOpenId();
            // 获取用户信息
             snsUserInfo = getSNSUserInfo(accessToken, openId);
            System.out.println("***********************************用户信息unionId："+snsUserInfo.getUnionid()+"***:"+snsUserInfo.getNickname());

        }
        return snsUserInfo;
    }

    /**
      * 通过code获取网页授权凭证
      *
     */
    @Override
    public Oauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        Oauth2Token wat = null;
        JSONObject jsonObject=null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl =requestUrl.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
        // 获取网页授权凭证
        jsonObject=JSON.parseObject(NetUtil.get(requestUrl));
        if (null != jsonObject) {
            wat=new Oauth2Token();
            wat.setAccessToken(jsonObject.getString("access_token"));
            wat.setExpiresIn(jsonObject.getInteger("expires_in"));
            wat.setRefreshToken(jsonObject.getString("refresh_token"));
            wat.setOpenId(jsonObject.getString("openid"));
            wat.setScope(jsonObject.getString("scope"));
        }
        return wat;
    }


    /**
     * 通过网页授权获取用户信息
     */
    @Override
    public SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        JSONObject jsonObject=null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        jsonObject =  JSON.parseObject(NetUtil.get(requestUrl));
        if (null != jsonObject) {
            snsUserInfo = new SNSUserInfo();
            // 用户的标识
            snsUserInfo.setOpenId(jsonObject.getString("openid"));
            // 昵称
            snsUserInfo.setNickname(jsonObject.getString("nickname"));
            // 性别（1是男性，2是女性，0是未知）
            snsUserInfo.setSex(jsonObject.getInteger("sex"));
            // 用户所在国家
            snsUserInfo.setCountry(jsonObject.getString("country"));
            // 用户所在省份
            snsUserInfo.setProvince(jsonObject.getString("province"));
            // 用户所在城市
            snsUserInfo.setCity(jsonObject.getString("city"));
            // 用户头像
            snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
            // 用户特权信息
            List<String> list = JSON.parseArray(jsonObject.getString("privilege"),String.class);
            snsUserInfo.setPrivilegeList(list);
            //与开放平台共用的唯一标识，只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
            snsUserInfo.setUnionid(jsonObject.getString("unionid"));
        }
        return snsUserInfo;
    }


    /**
     //验证微信接口正确性
     */
    @Override
    public void Wxtoken(HttpServletRequest request, HttpServletResponse response) {
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print;
        System.out.println("---------------RequestURI" + request.getRequestURI());
        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");

            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    System.out.println("---------校验成功"+echostr);
//                    不知道为什么这样写微信提示配置错误
//                    print = response.getWriter();
//                    print.write(echostr);
//                    print.flush();
//                    这样写不会报错
                    response.getOutputStream().println(echostr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("---------校验失败echostr"+echostr);
        }
    }
}
