/***********************************************************************
 * $Id: HttpClientBeanTest.java,v1.0 2013-9-18 下午3:00:11 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/**
 * @author Victor
 * @created @2013-9-18-下午3:00:11
 * 
 */
public class HttpClientTest{

	//static String str = "http://172.16.34.29/ip.html";
	static String str = "http://172.16.34.112/goform/getFpgaSpecification";

	public void socketGet() throws IOException{
		Socket socket = new Socket("172.16.34.111",80);
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		pw.println("GET /goform/getFpgaSpecification HTTP/1.0");
		
		pw.println();
		pw.flush();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String str = null;
		while ((str=br.readLine())!=null) {
			System.out.println(str);
		}
		pw.close();
		br.close();
		socket.close();
		//System.out.println(getServerTime());
	}
	
    public void testPost(Df client,String url){
//    	 client.setUrl(url);//http://172.16.34.111/goform/getFpgaSpecification");
//         String content = client.get();
//         System.out.println(content);
    	HashMap<String, String> headers = new HashMap<String, String>();  
    	//headers.put("Referer", "");  
    	//headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625Firefox/3.6.6 Greatwqs");  
    	headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
    	headers.put("Accept-Language","zh-cn,zh;q=0.5");  
    	//headers.put("Host","www.yourdomain.com");  
    	//headers.put("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");  
    	//headers.put("Referer", "http://www.yourdomian.com/xxxAction.html");  


    	//HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
    	HttpGet httpGet = new HttpGet(url);
    	httpGet.setHeader("Accept", "*/*");
    	httpGet.setHeader("Accept-Encoding","gzip, deflate");
    	httpGet.setHeader("Connection", "keep-alive");
    	httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
    	httpGet.setHeader("X-Requested-With","XMLHttpRequest");
    	httpGet.setHeader("Content-Type","text/plain; charset=UTF-8");
    	httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:12.0) Gecko/20100101 Firefox/12.0");
    	//HttpParams params = 
    	//client.
    	
    	HttpResponse response1 = null;;
		try {
			/*client.addResponseInterceptor(new HttpResponseInterceptor() {
			    @Override
			    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			        System.out.println(response.getEntity());
			    }
			});*/
			response1 = client.execute(httpGet);
			HttpEntity entity1 = response1.getEntity();
	        System.out.println(EntityUtils.toString(entity1,"UTF-8"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			httpGet.abort();
		}
        
        //String entityStr = EntityUtils.toString(entity1);
    	
    	
    }
    public static void main(String[] args) throws ClientProtocolException, IOException {
    	HttpClientTest test = new HttpClientTest();//new HttpClientTest();
    	Df client =  new Df();
		test.testPost(client, str);
		//test.socketGet();
		
	}
}
class Df extends DefaultHttpClient{
	
	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		System.out.println("ab");
		return super.execute(arg0, arg1, arg2, arg3);
	}

	@Override
	public <T> T execute(HttpHost target, HttpRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException,
			ClientProtocolException {
		System.out.println("abc");
		return super.execute(target, request, responseHandler);
	}

	@Override
	public <T> T execute(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler, HttpContext context)
			throws IOException, ClientProtocolException {
		System.out.println("abcd");
		return super.execute(request, responseHandler, context);
	}

	@Override
	public <T> T execute(HttpUriRequest request,
			ResponseHandler<? extends T> responseHandler) throws IOException,
			ClientProtocolException {
		System.out.println("abcde");
		return super.execute(request, responseHandler);
	}
	
	
}