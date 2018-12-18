/***********************************************************************
 * $ HttpClientManager.java,v1.0 2011-7-26 14:52:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.framework.exception.engine.HttpClientException;

/**
 * @author jay
 * @created @2011-7-26-14:52:44
 */
public abstract class HttpClientBean {
    protected static Logger logger = LoggerFactory.getLogger(HttpClientBean.class);
    private static Map<String, HttpClient> httpClientMap = Collections
            .synchronizedMap(new HashMap<String, HttpClient>());
    private static Map<String, Object> syMap = Collections.synchronizedMap(new HashMap<String, Object>());
    private String domain;
    private String url;
    private String params;
    private String charType = "utf-8";

    public HttpClientBean(String domain) {
        this.domain = domain;
        if (!syMap.containsKey(domain)) {
            Object syObj = new Object();
            syMap.put(domain, syObj);
        }
    }

    public String getCharType() {
        return charType;
    }

    public void setCharType(String charType) {
        this.charType = charType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    /**
     * 检查连接是否还有效，无效则重连，默认该方法不执行，扩展时可以通过该方法实现连接的有效性检查和重连操作
     */
    protected abstract void checkAndReConnect();

    /**
     * 销毁一个httpclient
     * 
     * @return
     */
    protected void shutdown() {
        if (httpClientMap.containsKey(domain)) {
            HttpClient httpClient = httpClientMap.get(domain);
            if (httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            } else {
                logger.debug("connectionManager is null.");
            }
            httpClientMap.remove(domain);
        }
    }

    /**
     * 封装的Get方法
     * 
     * @return html
     */
    public String get() {
        synchronized (syMap.get(domain)) {
            HttpGet getMethod = new HttpGet(url);
            StatusLine statusCode;
            try {
                HttpClient httpClient = getHttpClient(true);
                HttpResponse response = httpClient.execute(getMethod);
                statusCode = response.getStatusLine();
                if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                    shutdown();
                    throw new HttpClientException("result=99029004");
                }
                HttpEntity entity = response.getEntity();
                // 这里用流来读页面
                InputStream in = entity.getContent();
                if (in != null) {
                    byte[] tmp = new byte[4096];
                    int bytesRead;
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                    while ((bytesRead = in.read(tmp)) != -1) {
                        buffer.write(tmp, 0, bytesRead);
                    }
                    byte[] responseBody = buffer.toByteArray();
                    String html = new String(responseBody, charType);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Get method param[" + url + "," + charType + "]");
                        logger.trace("Get method result:" + html);
                    }
                    return html;
                } else {
                    return "";
                }
            } catch (IllegalStateException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (ClientProtocolException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (UnsupportedEncodingException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (IOException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } finally {
                try {
                    getMethod.abort();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 封装的Post方法
     * 
     * @return html
     */
    public String post() {
        synchronized (syMap.get(domain)) {
            HttpPost postMethod = new HttpPost(url);
            StatusLine statusCode;
            try {
                if (params != null && !params.equalsIgnoreCase("")) {
                    makePostParam(params, postMethod);
                } else {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    BasicNameValuePair basicNameValuePair = new BasicNameValuePair("c", "");
                    nvps.add(basicNameValuePair);
                    postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                }
                HttpClient httpClient = getHttpClient(true);
                HttpResponse response = httpClient.execute(postMethod);
                statusCode = response.getStatusLine();
                if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                    shutdown();
                    throw new HttpClientException("result=99029004");
                }
                HttpEntity entity = response.getEntity();
                // 这里用流来读页面
                InputStream in = entity.getContent();
                if (in != null) {
                    byte[] tmp = new byte[4096];
                    int bytesRead;
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                    while ((bytesRead = in.read(tmp)) != -1) {
                        buffer.write(tmp, 0, bytesRead);
                    }
                    byte[] responseBody = buffer.toByteArray();
                    String html = new String(responseBody, charType);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Post method param[" + url + "," + charType + "]");
                        logger.trace("Post method result:" + html);
                    }
                    return html;
                } else {
                    return "";
                }
            } catch (IllegalStateException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (ClientProtocolException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (UnsupportedEncodingException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (IOException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } finally {
                try {
                    postMethod.abort();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 封装的Post方法
     * 
     * @return html
     */
    public String post(List<NameValuePair> nvps) {
        synchronized (syMap.get(domain)) {
            HttpPost postMethod = new HttpPost(url);
            StatusLine statusCode;
            try {
                postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                HttpClient httpClient = getHttpClient(true);
                HttpResponse response = httpClient.execute(postMethod);
                statusCode = response.getStatusLine();
                if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                    shutdown();
                    throw new HttpClientException("result=99029004");
                }
                HttpEntity entity = response.getEntity();
                // 这里用流来读页面
                InputStream in = entity.getContent();
                if (in != null) {
                    byte[] tmp = new byte[4096];
                    int bytesRead;
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                    while ((bytesRead = in.read(tmp)) != -1) {
                        buffer.write(tmp, 0, bytesRead);
                    }
                    byte[] responseBody = buffer.toByteArray();
                    String html = new String(responseBody, charType);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Post method param[" + url + "," + charType + "]");
                        logger.trace("Post method result:" + html);
                    }
                    return html;
                } else {
                    return "";
                }
            } catch (IllegalStateException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (ClientProtocolException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (UnsupportedEncodingException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } catch (IOException e) {
                shutdown();
                throw new HttpClientException("result=99029004", e);
            } finally {
                try {
                    postMethod.abort();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 封装的Get方法
     * 
     * @return html
     */
    protected String getCheck() {
        HttpGet getMethod = new HttpGet(url);
        StatusLine statusCode;
        try {
            HttpClient httpClient = getHttpClient(false);
            HttpResponse response = httpClient.execute(getMethod);
            statusCode = response.getStatusLine();
            if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                shutdown();
                throw new HttpClientException("result=99029004");
            }
            HttpEntity entity = response.getEntity();
            // 这里用流来读页面
            InputStream in = entity.getContent();
            if (in != null) {
                byte[] tmp = new byte[4096];
                int bytesRead;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                while ((bytesRead = in.read(tmp)) != -1) {
                    buffer.write(tmp, 0, bytesRead);
                }
                byte[] responseBody = buffer.toByteArray();
                String html = new String(responseBody, charType);
                if (logger.isTraceEnabled()) {
                    logger.trace("Get method param[" + url + "," + charType + "]");
                    logger.trace("Get method result:" + html);
                }
                return html;
            } else {
                return "";
            }
        } catch (IllegalStateException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (ClientProtocolException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (UnsupportedEncodingException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (IOException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } finally {
            try {
                getMethod.abort();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * 封装的Post方法
     * 
     * @return html
     */
    protected String postCheck() {
        HttpPost postMethod = new HttpPost(url);
        StatusLine statusCode;
        try {
            makePostParam(params, postMethod);
            HttpClient httpClient = getHttpClient(false);
            HttpResponse response = httpClient.execute(postMethod);
            statusCode = response.getStatusLine();
            if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                shutdown();
                throw new HttpClientException("result=99029004");
            }
            HttpEntity entity = response.getEntity();
            // 这里用流来读页面
            InputStream in = entity.getContent();
            if (in != null) {
                byte[] tmp = new byte[4096];
                int bytesRead;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                while ((bytesRead = in.read(tmp)) != -1) {
                    buffer.write(tmp, 0, bytesRead);
                }
                byte[] responseBody = buffer.toByteArray();
                String html = new String(responseBody, charType);
                if (logger.isTraceEnabled()) {
                    logger.trace("Post method param[" + url + "," + charType + "]");
                    logger.trace("Post method result:" + html);
                }
                return html;
            } else {
                return "";
            }
        } catch (IllegalStateException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (ClientProtocolException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (UnsupportedEncodingException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (IOException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } finally {
            try {
                postMethod.abort();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * 封装的Post方法
     * 
     * @return html
     */
    protected String postCheck(List<NameValuePair> nvps) {
        HttpPost postMethod = new HttpPost(url);
        StatusLine statusCode;
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpClient httpClient = getHttpClient(false);
            HttpResponse response = httpClient.execute(postMethod);
            statusCode = response.getStatusLine();
            if (statusCode.getStatusCode() != HttpStatus.SC_OK) {
                shutdown();
                throw new HttpClientException("result=99029004");
            }
            HttpEntity entity = response.getEntity();
            // 这里用流来读页面
            InputStream in = entity.getContent();
            if (in != null) {
                byte[] tmp = new byte[4096];
                int bytesRead;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(1024);
                while ((bytesRead = in.read(tmp)) != -1) {
                    buffer.write(tmp, 0, bytesRead);
                }
                byte[] responseBody = buffer.toByteArray();
                String html = new String(responseBody, charType);
                if (logger.isTraceEnabled()) {
                    logger.trace("Post method param[" + url + "," + charType + "]");
                    logger.trace("Post method result:" + html);
                }
                return html;
            } else {
                return "";
            }
        } catch (IllegalStateException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (ClientProtocolException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (UnsupportedEncodingException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } catch (IOException e) {
            shutdown();
            throw new HttpClientException("result=99029004", e);
        } finally {
            try {
                postMethod.abort();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * 构造POST参数
     * 
     * @param params
     *            页面传递过来的 url中使用的参数格式 param1=value1&param2=value2&param3=value3
     * @param method
     */
    private void makePostParam(String params, HttpPost method) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String[] pps = params.split("&");
        String[] ops;
        for (String pp : pps) {
            if (pp.charAt(pp.length() - 1) == '=') {
                ops = pp.split("=");
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(ops[0], "");
                nvps.add(basicNameValuePair);
            } else {
                ops = pp.split("=");
                BasicNameValuePair basicNameValuePair = new BasicNameValuePair(ops[0], ops[1]);
                nvps.add(basicNameValuePair);
            }
        }
        method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        if (logger.isTraceEnabled()) {
            for (NameValuePair nameValuePair : nvps) {
                logger.trace(new StringBuilder("name = ").append(nameValuePair.getName()).append("\tvalue = ")
                        .append(nameValuePair.getValue()).toString());
            }
        }
    }

    private HttpClient getHttpClient(boolean ischeck) {
        HttpClient httpClient;
        if (httpClientMap.containsKey(domain)) {
            httpClient = httpClientMap.get(domain);
        } else {
            httpClient = new DefaultHttpClient();
            httpClientMap.put(domain, httpClient);
        }
        if (ischeck) {
            checkAndReConnect();
        }
        return httpClient;
    }
}
