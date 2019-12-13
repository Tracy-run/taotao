package com.demo.taotao.common.httputil;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtils {

    public static String ip;
    public static String port;
    public static String pmsIp;
    public static String pmsPort;
    public static String httpWebIp;
    public static String webport;
    public static String monitorstart;

    public static String send(String ip, String port, Map<String, String> map, String urls, String type) {
        try {
            HttpClientUtils hd = new HttpClientUtils();

            String url = "http://" + ip + ":"+port + urls;
            if("post".equals(type)){
                return hd.post(url, map);
            }else if("get".equals(type)){
                return hd.get(url);
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String send(Map<String, String> map,String urls, String type) {
        try {
            HttpClientUtils hd = new HttpClientUtils();

            String url = "http://" + ip + ":"+port + urls;
            if("post".equals(type)){
                return hd.post(url, map);
            }else if("get".equals(type)){
                return hd.get(url);
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 转义url的特殊字符.
     *
     * @param url
     *            请求路径
     *
     */
    public String changeURL(String url) {
        char[] urlchar = url.toCharArray();
        return url;
    }

    /**
     * 处理get请求.
     *
     * @param url
     *            请求路径
     * @return json
     */
    public static String get(String url) {
        // 实例化httpclient
        // CloseableHttpClient httpclient = HttpClients.createDefault();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 超时机制30S
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30000);//连接时间
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30000);//数据传输时间
        // 实例化get方法
        HttpGet httpget = new HttpGet(url);
        // 请求结果
        HttpResponse response = null;
        String content = "";
        try {
            // 执行get方法
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 处理post请求.
     *
     * @param url
     *            请求路径
     * @param params
     *            参数
     * @return json
     */
    public String post(String url, Map<String, String> params) {
        // 实例化httpClient
//		CloseableHttpClient httpclient = HttpClients.createDefault();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 超时机制30S
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,30000);//连接时间
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,30000);//数据传输时间
        // 实例化post方法
        HttpPost httpPost = new HttpPost(url);
        // 处理参数
        List<NameValuePair> nvps = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        // 结果
//		CloseableHttpResponse response = null;
        HttpResponse response = null;
        String content = "";
        try {
            // 提交的参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nvps,
                    "UTF-8");
            // 将参数给post方法
            httpPost.setEntity(uefEntity);
            // 执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 处理post请求.
     *
     * @param url
     *            请求路径
     * @param params
     *            参数
     * @return json
     */
    public String post2(String url, Map<String, String> params) {
        // 实例化httpClient
//		CloseableHttpClient httpclient = HttpClients.createDefault();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 超时机制30S
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,300000);//连接时间
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,300000);//数据传输时间
        // 实例化post方法
        HttpPost httpPost = new HttpPost(url);
        // 处理参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }
        // 结果
//		CloseableHttpResponse response = null;
        HttpResponse response = null;
        String content = "";
        try {
            // 提交的参数
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            // 将参数给post方法
            httpPost.setEntity(uefEntity);
            //设置请求头
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            // 执行post方法
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void init(){

        pmsIp = null ;//配置中获取("pmsHttpClientIp").trim();
        pmsPort = null ;//配置中获取("pmsHttpClientPort").trim();
        ip = null ;//配置中获取("httpClientIp").trim();
        port = null ;//配置中获取("httpClientPort").trim();
        httpWebIp = null ;//配置中获取("httpWebIp").trim();
        webport = null ;//配置中获取("httpWebPort").trim();
    }


}
