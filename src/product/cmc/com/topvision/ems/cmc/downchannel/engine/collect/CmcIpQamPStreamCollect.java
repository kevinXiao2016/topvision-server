/***********************************************************************
 * $Id: CC8800BCollect.java,v1.0 2013-10-12 上午11:17:53 $
 * 
 * @author:bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.downchannel.engine.collect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamISInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamOSInfo;
import com.topvision.exception.facade.HttpClientException;

/**
 * 解析并处理ipqam节目流信息
 * 
 * 获取并处理采集到的ipqam数据，返回解析后对象
 * @author bryan
 * @created @2013-10-12-上午11:17:53
 *
 */
public class CmcIpQamPStreamCollect{
    Logger logger = LoggerFactory.getLogger(getClass());
    public List<CmcDSIpqamISInfo> getIpqamInputStreamInfoResult(CmcHttpClient clientBean){
        clientBean.setUrl("http://"+clientBean.getDomain()+"/WebContent/asp/programStreamInfoList.asp");
        List<CmcDSIpqamISInfo> ipQamIslist = new ArrayList<CmcDSIpqamISInfo>();
        try {
            String content = clientBean.get();
            //String content = "      var ipqamInputStreamInfoList = {data: [{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '11',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '2',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '99',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '9999',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '77',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '1',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '111',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'}],item_num:5};var ipqamOutputStreamInfoList = {data: [{ipqamType: '2',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '11',ipqamSYNC: '0',ipqamOutputProgramNumber: '8',ipqamOutputPMTID: '144',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '99',ipqamSYNC: '0',ipqamOutputProgramNumber: '1',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '9999',ipqamSYNC: '0',ipqamOutputProgramNumber: '99',ipqamOutputPMTID: '160',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '1',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '77',ipqamSYNC: '0',ipqamOutputProgramNumber: '1',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '99',ipqamSYNC: '0',ipqamOutputProgramNumber: '22',ipqamOutputPMTID: '48',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '111',ipqamSYNC: '0',ipqamOutputProgramNumber: '7',ipqamOutputPMTID: '144',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '9999',ipqamSYNC: '0',ipqamOutputProgramNumber: '2',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'}],item_num:7};";
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
                if("ipqamInputStreamInfoList".equals(name.trim())){
                    obj = JSONObject.fromObject(str);
                    break;
                }
            }
            JSONArray jsonArray = obj.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject js = JSONObject.fromObject(jsonArray.get(i));
                CmcDSIpqamISInfo isInfo= new CmcDSIpqamISInfo();
                isInfo.setIpqamDestinationIP(js.getString("ipqamDestinationIP"));
                isInfo.setIpqamSendMode(js.getInt("ipqamSendMode"));
                isInfo.setIpqamSourceIP(js.getString("ipqamSourceIP"));
                isInfo.setIpqamSourcePort(js.getInt("ipqamSourcePort"));
                isInfo.setIpqamUDPPort(js.getInt("ipqamUDPPort"));
                
                int proType = checkIfNum(js,"ipqamProgType");//js.getString("ipqamProgType");
                isInfo.setIpqamProgType(proType);
                
                isInfo.setIpqamSYNC(js.getInt("ipqamSYNC"));
                isInfo.setIpqamType(js.getInt("ipqamType"));
                
                int iipn = checkIfNum(js,"ipqamInputProgramNumber");
                isInfo.setIpqamInputProgramNumber(iipn);
                
                int iipmtid = checkIfNum(js, "ipqamInputPMTID");
                isInfo.setIpqamInputPMTID(iipmtid);
                
                int iipcrid = checkIfNum(js, "ipqamInputPCRID");
                isInfo.setIpqamInputPCRID(iipcrid);
                
                int itespids = checkIfNum(js, "ipqamTotalESPIDs");
                isInfo.setIpqamTotalESPIDs(itespids);
                
                String ipqamInputBitrate = js.getString("ipqamInputBitrate");
                ipqamInputBitrate = ipqamInputBitrate.replaceAll(" ", "");
                isInfo.setIpqamInputBitrate(BigDecimal.valueOf(Double.parseDouble(ipqamInputBitrate)));
                //logger.info("ipqamInputBitrate="+js.getString("ipqamInputBitrate"));
                ipQamIslist.add(isInfo);
            }
            return ipQamIslist;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamPStreamCollect", e);
        }
    }
    /**
     * 由于输出流与输入流是一行，所以输出流需要做特殊解析
     * @param clientBean
     * @return
     */
    public List<CmcDSIpqamOSInfo> getIpqamOutputStreamInfoResult(CmcHttpClient clientBean){
        clientBean.setUrl("http://"+clientBean.getDomain()+"/WebContent/asp/programStreamInfoList.asp");
        List<CmcDSIpqamOSInfo> ipQamOslist = new ArrayList<CmcDSIpqamOSInfo>();
        try {
            String content = clientBean.get();
            //String content = "      var ipqamInputStreamInfoList = {data: [{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '11',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '2',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '99',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '9999',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '77',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '1',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'},{ipqamDestinationIP: '192.168.2.100',ipqamSendMode: '0',ipqamSourceIP: '0.0.0.0',ipqamSourcePort: '0',ipqamUDPPort: '111',ipqamProgType: '0',ipqamSYNC: '0',ipqamType: '0',ipqamInputProgramNumber: '0',ipqamInputPMTID: '8191',ipqamInputPCRID: '8191',ipqamTotalESPIDs: '0',ipqamInputBitrate: '0.     0'}],item_num:5};var ipqamOutputStreamInfoList = {data: [{ipqamType: '2',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '11',ipqamSYNC: '0',ipqamOutputProgramNumber: '8',ipqamOutputPMTID: '144',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '99',ipqamSYNC: '0',ipqamOutputProgramNumber: '1',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '8',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '9999',ipqamSYNC: '0',ipqamOutputProgramNumber: '99',ipqamOutputPMTID: '160',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '1',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '77',ipqamSYNC: '0',ipqamOutputProgramNumber: '1',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '99',ipqamSYNC: '0',ipqamOutputProgramNumber: '22',ipqamOutputPMTID: '48',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '111',ipqamSYNC: '0',ipqamOutputProgramNumber: '7',ipqamOutputPMTID: '144',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'},{ipqamType: '0',ipqamQAMManager: '0',ipqamOutputQAMChannel: '9',ipqamDestinationIP: '192.168.2.100',ipqamUDPPort: '9999',ipqamSYNC: '0',ipqamOutputProgramNumber: '2',ipqamOutputPMTID: '16',ipqamOutputPCRID: '8191',ipqamOutputBitrate : '0.     0',ipqamActive: '1'}],item_num:7};";
            StringTokenizer token = new StringTokenizer(content, "\n");
            JSONObject obj = new JSONObject();
            //String varName = "ipqamOutputStreamInfoList";
            while (token.hasMoreTokens()) {
                String line = token.nextToken().trim();
                int startIndex=-1;
                if ((startIndex=line.indexOf("ipqamOutputStreamInfoList"))==-1) {
                    continue;
                }
                line = line.substring(startIndex);
                
                int index = line.indexOf('=');//startIndex+
                //String name = line.substring(4, index);
                String str = line.substring(index + 1, line.length() - 1);
                obj = JSONObject.fromObject(str);
                
            }
            JSONArray jsonArray = obj.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject js = JSONObject.fromObject(jsonArray.get(i));
                CmcDSIpqamOSInfo osInfo= new CmcDSIpqamOSInfo();
                osInfo.setIpqamType(js.getInt("ipqamType"));
                osInfo.setIpqamQAMManager(js.getInt("ipqamQAMManager"));
                osInfo.setIpqamOutputQAMChannel(js.getInt("ipqamOutputQAMChannel"));
                osInfo.setIpqamDestinationIP(js.getString("ipqamDestinationIP"));
                osInfo.setIpqamUDPPort(js.getInt("ipqamUDPPort"));
                osInfo.setIpqamSYNC(js.getInt("ipqamSYNC"));
                
                int iopn = checkIfNum(js, "ipqamOutputProgramNumber");
                osInfo.setIpqamOutputProgramNumber(iopn);
                
                int iopmtid = checkIfNum(js, "ipqamOutputPMTID");
                osInfo.setIpqamOutputPMTID(iopmtid);
                
                int iopcrid = checkIfNum(js, "ipqamOutputPCRID");
                osInfo.setIpqamOutputPCRID(iopcrid);
                
                String ipqamOutputBitrate = js.getString("ipqamOutputBitrate");
                ipqamOutputBitrate = ipqamOutputBitrate.replaceAll(" ", "");
                osInfo.setIpqamOutputBitrate(BigDecimal.valueOf(Double.parseDouble(ipqamOutputBitrate)));
                osInfo.setIpqamActive(js.getInt("ipqamActive"));
                
                ipQamOslist.add(osInfo);
            }
            return ipQamOslist;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamPStreamCollect", e);
        }
    }
    private Integer checkIfNum(JSONObject js,String str){
        if("N/A".equals(js.getString(str))){
            return -1;
        }else{
            return js.getInt(str);
        }
    }
    public static void main(String[] args) {
        /*String ip = "172.16.34.111";
        CmcHttpClient client = new CmcHttpClient(ip);
        CmcIpQamPStreamCollect collect = new CmcIpQamPStreamCollect();
        List<CmcDSIpqamISInfo> inlist = collect.getIpqamInputStreamInfoResult(client);
        List<CmcDSIpqamOSInfo> oslist = collect.getIpqamOutputStreamInfoResult(client);
        for (Iterator iterator = inlist.iterator(); iterator.hasNext();) {
            CmcDSIpqamISInfo info = (CmcDSIpqamISInfo) iterator.next();
            System.out.println(info);
            
        }
        for (Iterator iterator = oslist.iterator(); iterator.hasNext();) {
            CmcDSIpqamOSInfo info = (CmcDSIpqamOSInfo) iterator.next();
            System.out.println(info);
            
        }*/
       
    }
}
