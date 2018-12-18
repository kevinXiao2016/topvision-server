/***********************************************************************
 * $Id: CmcIpQamConfigCollect.java,v1.0 2013-12-21 下午3:22:41 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.engine.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.downchannel.domain.CmcIpqamInfo;
import com.topvision.exception.facade.HttpClientException;

/**
 * 增加IPQAM功能的IP地址显示设置功能
 * @author bryan
 * @created @2013-12-21-下午3:22:41
 *
 */
public class CmcIpQamConfigCollect {
    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 获取IP地址信息，在ipqamConfig.asp页面中ASP方式获取的数据在页面上以一行数据显示
     * @param clientBean
     * @return
     */
    public CmcIpqamInfo getIpqamIPInfoResult(CmcHttpClient clientBean) {
        clientBean.setUrl("http://"+clientBean.getDomain()+"/WebContent/asp/ipqamConfig.asp");
        CmcIpqamInfo cmcIpqamInfo = new CmcIpqamInfo();
        try {
            String content = clientBean.get();
            StringTokenizer token = new StringTokenizer(content, "\n");
            JSONObject obj = new JSONObject();
            while (token.hasMoreTokens()) {
                String line = token.nextToken().trim();
                if (!line.startsWith("var ")) {
                    continue;
                }
                int startIndex=-1;
                if ((startIndex=line.indexOf("ipqamIpInfo"))==-1) {
                    continue;
                }
                line = line.substring(startIndex);
                
                int index = line.indexOf('=');
                String str = line.substring(index + 1, line.length() - 1);
                obj = JSONObject.fromObject(str);
                break;
            }
            /*var ipqamIpInfo = {data: [{ipqamIpAddr: "192.168.2.111",
             * ipqamIpMask: "255.255.255.0",ipqamGw: "192.168.2.1",
             * ipqamMacAddr: "00:24:68:24:00:23"}],item_num:1};
             * */
            JSONArray jsonArray = obj.getJSONArray("data");
            if(jsonArray.size()>0){
                JSONObject js = JSONObject.fromObject(jsonArray.get(0));
                cmcIpqamInfo.setIpqamIpAddr(js.getString("ipqamIpAddr"));
                cmcIpqamInfo.setIpqamIpMask(js.getString("ipqamIpMask"));
                cmcIpqamInfo.setIpqamGw(js.getString("ipqamGw"));
                cmcIpqamInfo.setIpqamMacAddr(js.getString("ipqamMacAddr"));
            }
            return cmcIpqamInfo;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamConfigCollect", e);
        }
    }
    public String modifyIpqamIPInfo(CmcHttpClient clientBean,CmcIpqamInfo cmcIpqamInfo) {
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String message = "ERROR";
        String urlValue = "/goform/setIPQAMIPInfo";

        String param = "ipqamIpAddr=" + cmcIpqamInfo.getIpqamIpAddr() + "&ipqamIpMask=" +cmcIpqamInfo.getIpqamIpMask();
        try {
            socket = new Socket(clientBean.getDomain(),80);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            pw.println("GET "+urlValue+"?"+param+" HTTP/1.0");
            logger.info("GET "+urlValue+"?"+param+" HTTP/1.0");
            pw.println();
            pw.flush();
            
            String str = null;
            
            while ((str=br.readLine())!=null) {
                sb.append(str);
            }
            logger.info(sb.toString());
            if (sb.indexOf("success")!=-1) {
                message =  "SUCCESS";
            }
         } catch (UnknownHostException e) {
             throw new HttpClientException("CmcIpQamConfigCollect", e);
         } catch (IOException e) {
             throw new HttpClientException("CmcIpQamConfigCollect", e);
         }finally{
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                throw new HttpClientException("CmcIpQamConfigCollect", e);
            }
         }
         return message;
    }
    public static void main(String[] args) {
        String ip = "172.16.34.112";
        CmcHttpClient client = new CmcHttpClient(ip);
        CmcIpQamConfigCollect collect = new CmcIpQamConfigCollect();
        CmcIpqamInfo cmcIpqamInfo = collect.getIpqamIPInfoResult(client);
        System.out.println(cmcIpqamInfo);
        cmcIpqamInfo.setIpqamIpAddr("192.168.2.110");
        collect.modifyIpqamIPInfo(client, cmcIpqamInfo);
        
    }
}
