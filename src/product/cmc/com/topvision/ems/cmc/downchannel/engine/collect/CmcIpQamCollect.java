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
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamBaseInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcDSIpqamStatusInfo;
import com.topvision.ems.cmc.downchannel.domain.CmcFpgaSpecification;
import com.topvision.exception.facade.HttpClientException;

/**
 * 根据FPGA判断是否支持IPQAM及 该类处理下行信道IPQAM信道基本信息及ipqam信道状态信息
 * 
 * 获取并处理采集到的ipqam数据，返回解析后对象
 * 
 * @author bryan
 * @created @2013-10-12-上午11:17:53
 *
 */
public class CmcIpQamCollect {
    public static String osname = System.getProperty("os.name");
    Logger logger = LoggerFactory.getLogger(getClass());

    public List<CmcDSIpqamBaseInfo> getIpqamBaseInfoListResult(CmcHttpClient clientBean) {
        clientBean.setUrl("http://" + clientBean.getDomain() + "/WebContent/asp/downChannelList.asp?referFrom=nm3000");
        List<CmcDSIpqamBaseInfo> ipQamlist = new ArrayList<CmcDSIpqamBaseInfo>();
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
                if ("downChannelIPQAMList".equals(name.trim())) {
                    obj = JSONObject.fromObject(str);
                    break;
                }
            }
            JSONArray jsonArray = obj.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject js = JSONObject.fromObject(jsonArray.get(i));
                CmcDSIpqamBaseInfo ipqam = new CmcDSIpqamBaseInfo();
                int adminStatus = js.getInt("ifAdminStatus");
                ipqam.setChannelType(adminStatus);
                ipqam.setDocsIfDownChannelId(js.getInt("docsIfDownChannelId"));
                ipqam.setDocsIfDownChannelSymRate(js.getInt("docsIfDownChannelSymRate"));
                ipqam.setIpqamTranspStreamID(js.getInt("ipqamTranspStreamID"));
                ipqam.setIpqamOriginalNetworkID(js.getInt("ipqamOriginalNetworkID"));
                ipqam.setIpqamQAMManager(js.getInt("ipqamQAMManager"));
                ipqam.setIpqamQAMGroupName(js.getString("ipqamQAMGroupName"));
                ipqam.setIpqamAtten(js.getString("ipqamAtten"));
                ipqam.setIpqamDtsAdjust(js.getInt("ipqamDtsAdjust"));
                ipQamlist.add(ipqam);
            }
            return ipQamlist;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        }
    }

    public String setIpqamBaseInfoResult(CmcHttpClient clientBean, CmcDSIpqamBaseInfo ipqam) {
        /* ipqam中必须存在的参数channelId、frequency与power */
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String message = "ERROR";
        String urlValue = "/goform/downChannelIPQAMSet?docsIfDownChannelId=" + ipqam.getDocsIfDownChannelId();
        String dataSrc = (ipqam.getDocsIfDownChannelFrequency() == null ? "" : "&docsIfDownChannelFrequency="
                + ipqam.getDocsIfDownChannelFrequency())
                + (ipqam.getIfAdminStatus() == null ? "" : "&ifAdminStatus=" + ipqam.getIfAdminStatus())
                + (ipqam.getDocsIfDownChannelModulation() == null ? "" : "&docsIfDownChannelModulation="
                        + ipqam.getDocsIfDownChannelModulation())
                + (ipqam.getDocsIfDownChannelInterleave() == null ? "" : "&docsIfDownChannelInterleave="
                        + ipqam.getDocsIfDownChannelInterleave())
                + (ipqam.getDocsIfDownChannelPower() == null ? "" : "&docsIfDownChannelPower="
                        + ipqam.getDocsIfDownChannelPower())
                + (ipqam.getDocsIfDownChannelAnnex() == null ? "" : "&docsIfDownChannelAnnex="
                        + ipqam.getDocsIfDownChannelAnnex())
                + (ipqam.getIpqamQAMManager() == null ? "" : "&ipqamQAMManager=" + ipqam.getIpqamQAMManager())
                + (ipqam.getIpqamQAMGroupName() == null ? "" : "&ipqamQAMGroupName=" + ipqam.getIpqamQAMGroupName())
                + (ipqam.getDocsIfDownChannelSymRate() == null ? "" : "&docsIfDownChannelSymRate="
                        + ipqam.getDocsIfDownChannelSymRate())
                + (ipqam.getIpqamAtten() == null ? "" : "&ipqamAtten=" + ipqam.getIpqamAtten())
                + (ipqam.getIpqamTranspStreamID() == null ? "" : "&ipqamTranspStreamID="
                        + ipqam.getIpqamTranspStreamID())
                + (ipqam.getIpqamOriginalNetworkID() == null ? "" : "&ipqamOriginalNetworkID="
                        + ipqam.getIpqamOriginalNetworkID())
                + (ipqam.getIpqamDtsAdjust() == null ? "" : "&ipqamDtsAdjust=" + ipqam.getIpqamDtsAdjust());
        try {
            socket = new Socket(clientBean.getDomain(), 80);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw.println("GET " + urlValue + dataSrc + " HTTP/1.0");
            logger.info("GET " + urlValue + dataSrc + " HTTP/1.0");
            pw.println();
            pw.flush();

            String str = null;
            while ((str = br.readLine()) != null) {
                logger.info(str);
                sb.append(str);
            }
            if (sb.indexOf("success") != -1) {
                message = "SUCCESS";
            } else {
                throw new HttpClientException("CmcIpQamCollect:" + str);
            }
        } catch (UnknownHostException e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        } catch (IOException e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        } finally {
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                throw new HttpClientException("CmcIpQamCollect", e);
            }
        }
        return message;
    }

    public String setAdminStatusResult(CmcHttpClient clientBean, Integer channelIds, Integer ifAdminStatus) {
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String message = "ERROR";
        try {
            socket = new Socket(clientBean.getDomain(), 80);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw.println("GET " + "/goform/batchChangeAdminStatusDownChannel?" + "channelIds=" + channelIds
                    + "&ifAdminStatus=" + ifAdminStatus + " HTTP/1.0");
            pw.println();
            pw.flush();

            String str = null;
            while ((str = br.readLine()) != null) {
                logger.info(str);
                sb.append(str);
            }

            if (sb.indexOf("success") != -1) {
                message = "SUCCESS";
            }
        } catch (UnknownHostException e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        } catch (IOException e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        } finally {
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                throw new HttpClientException("CmcIpQamCollect", e);
            }
        }
        return message;
    }

    public List<CmcDSIpqamStatusInfo> getIpqamStatusInfoListResult(CmcHttpClient clientBean) {
        clientBean.setUrl("http://" + clientBean.getDomain() + "/WebContent/asp/ipqamList.asp");
        List<CmcDSIpqamStatusInfo> ipQamlist = new ArrayList<CmcDSIpqamStatusInfo>();
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
                if ("getIPQAMOutputStatusInfoList".equals(name.trim())) {
                    obj = JSONObject.fromObject(str);
                    break;
                }
            }
            JSONArray jsonArray = obj.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject js = JSONObject.fromObject(jsonArray.get(i));
                CmcDSIpqamStatusInfo ipqamStatus = new CmcDSIpqamStatusInfo();
                ipqamStatus.setIpqamOutputQAMChannel(js.getInt("ipqamOutputQAMChannel"));
                ipqamStatus.setIpqamFrequency(js.getString("ipqamFrequency"));
                ipqamStatus.setIpqamUsedUDPPorts(js.getInt("ipqamUsedUDPPorts"));
                ipqamStatus.setIpqamUsedBandwidth(js.getString("ipqamUsedBandwidth"));
                ipqamStatus.setIpqamBandwidthCapacity(js.getString("ipqamBandwidthCapacity"));
                ipqamStatus.setIpqamPercent(BigDecimal.valueOf(js.getDouble("ipqamPercent")));
                ipqamStatus.setIpqamAtten(js.getInt("ipqamAtten"));
                ipqamStatus.setIpqamSymbolRate(js.getString("ipqamSymbolRate"));
                ipqamStatus.setIpqamModulation(js.getString("ipqamModulation"));
                ipQamlist.add(ipqamStatus);
            }
            return ipQamlist;
        } catch (Exception e) {
            throw new HttpClientException("CmcIpQamCollect", e);
        }
    }

    public CmcFpgaSpecification checkIfIpqamSupported(CmcHttpClient clientBean) {
        Socket socket = null;
        PrintWriter pw = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String urlValue = "/goform/getFpgaSpecification";
        CmcFpgaSpecification fpga = new CmcFpgaSpecification();
        try {
            socket = new Socket(clientBean.getDomain(), 80);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (osname.startsWith("Windows")) {
                pw.println("GET " + urlValue + " HTTP/1.0");
                pw.println("");
            } else if (osname.startsWith("Linux")) {
                pw.println("GET " + urlValue + " HTTP/1.0\r");
                pw.println("\r");
            }
            pw.flush();

            int c;
            while ((c = br.read()) != -1) {
                sb.append((char) c);
            }
            JSONObject obj = JSONObject.fromObject(sb.toString());
            JSONArray js = obj.getJSONArray("data");
            int item_num = obj.getInt("item_num");
            int fpga_ipqam_channel_count = 0;
            fpga.setItemNum(item_num);
            if (item_num != 0) {
                JSONObject tmpJo = JSONObject.fromObject(js.get(0));
                fpga_ipqam_channel_count = tmpJo.getInt("fpga_ipqam_channel_count");
                fpga.setIpqamChannelCount(fpga_ipqam_channel_count);
                fpga.setSubnetVlanCount(tmpJo.getInt("fpga_subnet_vlan_count"));
                fpga.setAclUplinkEgress(tmpJo.getInt("fpga_acl_uplink_egress"));
                fpga.setAclUplinkIngress(tmpJo.getInt("fpga_acl_uplink_ingress"));
                fpga.setAclCableEgress(tmpJo.getInt("fpga_acl_cable_egress"));
                fpga.setAclCableIngress(tmpJo.getInt("fpga_acl_cable_ingress"));
                fpga.setAclUplinkEgressCopy2cpu(tmpJo.getInt("fpga_acl_uplink_egress_copy2cpu"));
                fpga.setAclUplinkIngressCopy2cpu(tmpJo.getInt("fpga_acl_uplink_ingress_copy2cpu"));
                fpga.setAclCableEgressCopy2cpu(tmpJo.getInt("fpga_acl_cable_egress_copy2cpu"));
                fpga.setAclCableIngressCopy2cpu(tmpJo.getInt("fpga_acl_cable_ingress_copy2cpu"));
                fpga.setVipSupportCount(tmpJo.getInt("fpga_vip_support_count"));
                fpga.setCosCmCount(tmpJo.getInt("fpga_cos_cm_count"));
                fpga.setCosCountPerCm(tmpJo.getInt("fpga_cos_count_per_cm"));
                fpga.setMacTblHashLen(tmpJo.getInt("fpga_mac_tbl_hash_len"));
                fpga.setMacTblHashDeep(tmpJo.getInt("fpga_mac_tbl_hash_deep"));
                fpga.setSrcVerifyHashLen(tmpJo.getInt("fpga_src_verify_hash_len"));
                fpga.setSrcVerifyHashDeep(tmpJo.getInt("fpga_src_verify_hash_deep"));
            }
        } catch (UnknownHostException e) {
            logger.error("CmcIpQamCollect", e);
            fpga = null;
        } catch (IOException e) {
            logger.error("CmcIpQamCollect", e);
            fpga = null;
        } catch (JSONException e) {
            logger.error("CmcIpQamCollect", e);
            fpga = null;
        } finally {
            try {
                pw.close();
                br.close();
                socket.close();
            } catch (IOException e) {
                logger.error("CmcIpQamCollect", e);
            }
        }
        return fpga;
    }

    public static void main(String[] args) {
    }
}
