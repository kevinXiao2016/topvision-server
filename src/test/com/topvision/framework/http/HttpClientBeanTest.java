/***********************************************************************
 * $Id: HttpClientBeanTest.java,v1.0 2013-9-18 下午3:00:11 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.http;

import java.util.StringTokenizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Victor
 * @created @2013-9-18-下午3:00:11
 * 
 */
public class HttpClientBeanTest {
    CmcHttpClient client;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        client = new CmcHttpClient("CC8800B");
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.topvision.framework.http.HttpClientBean#get()}.
     */
    @Test
    public void testGet() {
        client.setUrl("http://172.17.2.140/WebContent/asp/downChannelList.asp?referFrom=nm3000");
        String content = client.get();
        // System.out.println(content);
        StringTokenizer token = new StringTokenizer(content, "\n");
        while (token.hasMoreTokens()) {
            String line = token.nextToken().trim();
            if (!line.startsWith("var ")) {
                continue;
            }
            int index = line.indexOf('=');
            String name = line.substring(4, index);
            String str = line.substring(index + 1, line.length() - 1);
            JSONObject obj = JSONObject.fromObject(str);
            int num = obj.getInt("item_num");
            JSONArray data = obj.getJSONArray("data");
            System.out.println(name);
            System.out.println("\t" + num);
            System.out.println("\t" + data);
        }
        client.shutdown();
    }

    /**
     * Test method for {@link com.topvision.framework.http.HttpClientBean#post()}.
     */
    @Test
    public void testPost() {
    }
}

class CmcHttpClient extends HttpClientBean {
    /**
     * @param domain
     */
    public CmcHttpClient(String domain) {
        super(domain);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.http.HttpClientBean#checkAndReConnect()
     */
    @Override
    protected void checkAndReConnect() {
    }
}