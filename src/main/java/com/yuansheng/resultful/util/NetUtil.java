package com.yuansheng.resultful.util;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import static java.lang.System.out;

/**
 * Created by Song on 2018/7/18
 * 基于HttpClient提供网络访问工具 grt posrq请求
 */
public class NetUtil {
    private static CloseableHttpClient httpClient= HttpClientBuilder.create().build();

    /**
     * Created by Song on 2018/7/18
     * get请求获取String类型数据
     * @param url 请求链接
     */
    public static String get(String url)  {
        StringBuffer sb = new StringBuffer();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        String result = null;
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //把返回的结果转换为JSON对象
                result = EntityUtils.toString(entity, "UTF-8");
                out.println("------------------"+result);
                //jsonObject=new JSONObject();
            }
            //将字节流转换为字符流
//            InputStreamReader reader = new InputStreamReader(entity.getContent(),"utf-8");
//            //指定缓冲区大小
//            char [] charbufer;
//            while (reader.read(charbufer=new char[10])>0){
//                sb.append(charbufer);
//            }
//            out.println("------------------"+sb.toString());
//            return new String(sb.toString().getBytes(),"ISO-8859-1");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
        return null;
//        return sb.toString();
        }

    /**
     * post方式请求数据
     * @param url 请求链接
     * @param data post数据体
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String post(String url,Map<String,String> data){
        StringBuffer sb=new StringBuffer();
        HttpPost httpPost = new HttpPost(url);
        BufferedInputStream in;
        //指定缓冲区大小
        byte [] buffer=new byte[128];
        //设置参数
        List<NameValuePair> valuePairs  = new ArrayList<NameValuePair>();
        if(data!=null){
            for (String key:data.keySet()) {
                valuePairs.addAll((Collection<? extends NameValuePair>) new BasicNameValuePair(key, data.get(key)));
            }
        }
        try {
            //设置请求参数
            httpPost.setEntity(new UrlEncodedFormEntity( valuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            in = new BufferedInputStream(httpEntity.getContent());
            while (in.read(buffer)>0){
                sb.append(new String(buffer,"utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            httpPost.releaseConnection();
            }
            return sb.toString();
    }

    /**
     * URL编码（utf-8）
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
