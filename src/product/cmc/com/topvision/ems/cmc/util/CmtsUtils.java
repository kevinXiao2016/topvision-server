/***********************************************************************
 * $Id: CmtsUtils.java,v1.0 2013-8-13 下午1:49:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author loyal
 * @created @2013-8-13-下午1:49:20
 * 
 */
public class CmtsUtils {
    private static Integer UBR7225_UPCHANNEL = 129;
    private static Integer UBR7225_DOWNCHANNEL = 128;
    private static Integer UBR7225_UPLINK = 6;
    private static Integer CASA_UPCHANNEL = 205;
    private static Integer CASA__DOWNCHANNEL = 128;
    private static Integer CASA_UPLINK = 6;
    private static Integer BSR2000_UPCHANNEL = 205;
    private static Integer BSR2000__DOWNCHANNEL = 128;
    private static Integer BSR2000_UPLINK1 = 6;//上联口
    private static Integer BSR2000_UPLINK2 = 117;//上联口

    /**
     * 获取上行信道索引(x/x/x)
     * @param ifDescr
     * @param typeId
     * @return
     *//*
    public static String getCmtsUpChannelIndex(Long typeId, String ifDescr, EntityTypeService entityTypeService) {
        if(ifDescr==null || ifDescr == ""){
            return "";
        }
        if(ifDescr==null || ifDescr == ""){
            return "";
        }
        if (ifDescr != null && entityTypeService.isUbrCmts(typeId)) {
            return "US " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2) + "/"
                    + ifDescr.substring(ifDescr.length() - 1, ifDescr.length());
        } else if (ifDescr != null && entityTypeService.isCasaCmts(typeId)) {
            return "US " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2).replace(".", "/");
        } else if (ifDescr != null && entityTypeService.isBsrCmts(typeId)) {
            return "US " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else {
            //modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
            return "US " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2).replace(".", "/");
            //return ifDescr;
        }
    }

    *//**
     * 获取下行信道索引(x/x/x)
     * @param ifDescr
     * @return
     *//*
    public static String getCmtsDownChannelIndex(Long typeId, String ifDescr, EntityTypeService entityTypeService) {
        if(ifDescr==null || ifDescr == ""){
            return "";
        }
        if(ifDescr==null || ifDescr == ""){
            return "";
        }
        if (ifDescr != null && entityTypeService.isUbrCmts(typeId)) {
            return "DS " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.length()).replace(":", "/");
        } else if (ifDescr != null && entityTypeService.isCasaCmts(typeId)) {
            return "DS " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else if (ifDescr != null && entityTypeService.isBsrCmts(typeId)) {
            return "DS " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else {
            //modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
            return "DS " + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
            //return ifDescr;
        }
    }*/

    /**
     * 获取cmts端口类型(0:upchannle  1:downchannel  2:upLinkPort)
     * @param typeId
     * @param ifType
     * @return 0:up  1:down  2:upLinkPort
     */
    public static int getCmtsChannelType(Long typeId, Long ifType, EntityTypeService entityTypeService) {
        if (ifType != null && entityTypeService.isCasaCmts(typeId)) {
            if (UBR7225_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (UBR7225_DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (UBR7225_UPLINK.equals(ifType)) {
                return 2;
            }
        } else if (ifType != null && entityTypeService.isCasaCmts(typeId)) {
            if (CASA_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (CASA__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (CASA_UPLINK.equals(ifType)) {
                return 2;
            }
        } else if (ifType != null && entityTypeService.isCasaCmts(typeId)) {
            if (BSR2000_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (BSR2000__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (BSR2000_UPLINK1.equals(ifType) || BSR2000_UPLINK2.equals(ifType)) {
                return 2;
            }
        }
        //modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
        else if (ifType != null) {
            if (CASA_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (CASA__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (CASA_UPLINK.equals(ifType)) {
                return 2;
            }
        }
        return -1;
    }

    public static String getCmtsUpChannelMode(Integer modeValue) {
        switch (modeValue) {
        case 1:
            return "other";
        case 2:
            return "qpsk";
        case 3:
            return "qam16";
        case 4:
            return "qam8";
        case 5:
            return "qam32";
        case 6:
            return "qam64";
        case 7:
            return "qam128";
        default:
            return "other";
        }
    }
}
