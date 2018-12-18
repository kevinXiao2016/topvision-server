/***********************************************************************
 * $Id: CC8800BCollect.java,v1.0 2013-10-12 上午11:17:53 $
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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamMappings;
import com.topvision.exception.facade.HttpClientException;

/**
 * 解析并处理ipqam节目流映射信息
 * 
 * 获取并处理采集到的ipqam数据，返回解析后对象
 * @author bryan
 * @created @2013-10-12-上午11:17:53
 *
 */
public class CmcIpQamMappingsCollect{
    Logger logger = LoggerFactory.getLogger(getClass());
    public List<CmcDSIpqamMappings> getIpqamMappingsListResult(CmcHttpClient clientBean){
        clientBean.setUrl("http://"+clientBean.getDomain()+"/WebContent/asp/ipqamList.asp");
        List<CmcDSIpqamMappings> ipQamMappingslist = new ArrayList<CmcDSIpqamMappings>();
        try {
            String content = clientBean.get();
            StringTokenizer token = new StringTokenizer(content, "\n");
            JSONObject obj = new JSONObject();
            while (token.hasMoreTokens()) {
                String line = token.nextToken().trim();
                if (!line.startsWith("var ")) {
                    continue;
                }
                int index = line.indexOf('=');
                String name = line.substring(4, index);
                String str = line.substring(index + 1, line.length() - 1);
                if("ipqamStreamMapList".equals(name.trim())){
                    obj = JSONObject.fromObject(str);
                    break;
                }
            }
            JSONArray jsonArray = obj.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject js = JSONObject.fromObject(jsonArray.get(i));
                CmcDSIpqamMappings ipqamMappings= new CmcDSIpqamMappings();
                ipqamMappings.setIpqamPidMapString(js.getString("ipqamPidMapString"));
                ipqamMappings.setIpqamOutputQAMChannel(js.getInt("ipqamOutputQAMChannel"));
                ipqamMappings.setIpqamDestinationIPAddress(js.getString("ipqamDestinationIPAddress"));
                ipqamMappings.setIpqamUDPPort(js.getInt("ipqamUDPPort"));
                ipqamMappings.setIpqamActive(js.getInt("ipqamActive"));
                ipqamMappings.setIpqamStreamType(js.getInt("ipqamStreamType"));
                ipqamMappings.setIpqamProgramNumberInput(js.getInt("ipqamProgramNumberInput"));
                ipqamMappings.setIpqamProgramNumberOutput(js.getInt("ipqamProgramNumberOutput"));
                ipqamMappings.setIpqamPMV(js.getInt("ipqamPMV"));
                ipqamMappings.setIpqamDataRateEnable(js.getInt("ipqamDataRateEnable"));
                ipqamMappings.setIpqamDataRate(js.getInt("ipqamDataRate"));
                
                ipQamMappingslist.add(ipqamMappings);
            }
            return ipQamMappingslist;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamMappingsCollect", e);
        }
    }
    public String setIpqamMappingsListResult(CmcHttpClient clientBean,List<CmcDSIpqamMappings> mappingsList,Integer action){
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String message = "ERROR";
        String urlValue = "/goform/setIPQAMStreamMapList";
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < mappingsList.size(); i++) {
            int j = i+1;
            CmcDSIpqamMappings mappings = mappingsList.get(i);
            //String str = mappings.getIpqamOutputQAMChannel()==null?"":"&ipqamOutputQAMChannel="+mappings.getIpqamOutputQAMChannel();
            
            paramBuilder.append("&"+j+"_ipqamOutputQAMChannel="+mappings.getIpqamOutputQAMChannel());
            paramBuilder.append("&"+j+"_ipqamDestinationIPAddress="+mappings.getIpqamDestinationIPAddress());
            paramBuilder.append("&"+j+"_ipqamOldUDPPort="+mappings.getIpqamOldUDPPort());
            paramBuilder.append("&"+j+"_ipqamUDPPort="+mappings.getIpqamUDPPort());
            paramBuilder.append("&"+j+"_ipqamActive="+mappings.getIpqamActive());
            paramBuilder.append("&"+j+"_ipqamProgramNumberInput="+mappings.getIpqamProgramNumberInput());
            paramBuilder.append("&"+j+"_ipqamProgramNumberOutput="+mappings.getIpqamProgramNumberOutput());
            paramBuilder.append("&"+j+"_ipqamPMV="+mappings.getIpqamPMV());
            paramBuilder.append("&"+j+"_ipqamDataRateEnable="+mappings.getIpqamDataRateEnable());
            paramBuilder.append("&"+j+"_ipqamAction="+action);//
            paramBuilder.append("&"+j+"_ipqamDataRate="+mappings.getIpqamDataRate());
            paramBuilder.append("&"+j+"_ipqamStreamType="+mappings.getIpqamStreamType());
            paramBuilder.append("&"+j+"_ipqamPidMapString="+mappings.getIpqamPidMapString());
        }
        paramBuilder.append("&streamMapNum="+mappingsList.size());
        paramBuilder = paramBuilder.replace(0, 1, "");
        try {
            socket = new Socket(clientBean.getDomain(),80);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            pw.println("GET "+urlValue+"?"+paramBuilder+" HTTP/1.0");
            logger.info("GET "+urlValue+"?"+paramBuilder+" HTTP/1.0");
            pw.println();
            pw.flush();
            
            String str = null;
            
            while ((str=br.readLine())!=null) {
                //logger.info(str);
                sb.append(str);
            }
            logger.info(sb.toString());
            if (sb.indexOf("success")!=-1) {
                message =  "SUCCESS";
            }
         } catch (UnknownHostException e) {
             throw new HttpClientException("CmcIpQamMappingsCollect", e);
         } catch (IOException e) {
             throw new HttpClientException("CmcIpQamMappingsCollect", e);
         }finally{
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                throw new HttpClientException("CmcIpQamMappingsCollect", e);
            }
         }
         return message;
    }
    
    public static void main(String[] args) {
        /*String ip = "172.16.34.111";
        CmcHttpClient client = new CmcHttpClient(ip);
        CmcIpQamMappingsCollect collect = new CmcIpQamMappingsCollect();
        List<CmcDSIpqamMappings> list = collect.getIpqamMappingsListResult(client);
        for (Iterator iterator = list.iterator(); iterator.hasNext();) { 
            CmcDSIpqamMappings mappings = (CmcDSIpqamMappings) iterator.next();
            System.out.println(mappings);
            
        }*/
       
    }
}
