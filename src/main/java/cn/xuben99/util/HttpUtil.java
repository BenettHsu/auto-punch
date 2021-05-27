package cn.xuben99.util;
 
import cn.xuben99.constants.HttpMethodContant;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 * @author wangmx
 */
@Slf4j
public class HttpUtil {
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static List<String> closingRequest = Arrays.asList(HttpMethodContant.PUT,HttpMethodContant.POST);

    public static String get(String url) throws IOException {
        return get(url, new HashMap<>());
    }
    public static String get(String url, Map<String, String> header) throws IOException {
        return request(HttpMethodContant.GET,url,header,null);
    }
    public static String post(String url) throws IOException {
        return post(url,"{}");
    }
    public static String post(String url,String body) throws IOException {
        return post(url, new HashMap<>(),body);
    }
    public static String post(String url,Map<String, String> header) throws IOException {
        return post(url, header,"{}");
    }

    public static String post(String url, Map<String, String> header,String body) throws IOException {
        return request(HttpMethodContant.POST,url,header,body);
    }

    public static String request(String method,String url, Map<String, String> header,String body) throws IOException {
        CloseableHttpResponse response = null;
        HttpRequestBase httpRequestBase = getHttpRequestBase(method,url);
        //fill header params
        for (Map.Entry<String, String> head : header.entrySet()) {
            httpRequestBase.addHeader(head.getKey(), head.getValue());
        }
        // if method in [put,post], need to HttpRequestBase cast to HttpEntityEnclosingRequestBase
        if (closingRequest.contains(method)){
            HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase = (HttpEntityEnclosingRequestBase) httpRequestBase;
            HttpEntity entity = new StringEntity(body,"Utf-8");
            httpEntityEnclosingRequestBase.setEntity(entity);
            log.info("requestUrl => {} => body {}",httpEntityEnclosingRequestBase,body);
            response = httpClient.execute(httpEntityEnclosingRequestBase);
        }else {
            log.info("requestUrl => {} ",httpRequestBase);
            response = httpClient.execute(httpRequestBase);
        }
        String res = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("response => {}",res);
        return res;
    }
    public static HttpRequestBase getHttpRequestBase(String method,String url) {
        switch (method) {
            case HttpMethodContant.GET: return new HttpGet(url);
            case HttpMethodContant.DELETE: return new HttpDelete(url);
            case HttpMethodContant.POST: return new HttpPost(url);
            case HttpMethodContant.PUT: return new HttpPut(url);
            default: throw new RuntimeException("not support this method"+method);
        }
    }
}