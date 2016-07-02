package com.jean.fiction.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Httpclient {

    static Logger logger = Logger.getLogger(Httpclient.class);

    public static String get(String url, Map<String, String> params) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.setHeader("Accept-Charset", "UTF-8,utf-8;q=0.7,*;q=0.7");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.setHeader("Cache-Control", "max-age=0");
            httpGet.setHeader("Upgrade-Insecure-Requests", "1");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

            if (params != null || !params.isEmpty()) {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs.add(new BasicNameValuePair(key, value));
                }
                String param = EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8), Consts.UTF_8);
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + param));
            }

            // 执行get请求.
            response = httpclient.execute(httpGet);
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String returnInfo = EntityUtils.toString(entity);
                if (logger.isDebugEnabled()) {
                    logger.debug("请求URL " + url);
                    logger.debug("响应内容：\r\n" + returnInfo);
                }
                return returnInfo;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            // 关闭连接,释放资源
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return "";
    }
}
